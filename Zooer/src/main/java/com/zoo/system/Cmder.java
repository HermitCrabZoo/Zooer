package com.zoo.system;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Cmder {

    private static ExecutorService pool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 3L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    public Answer execute(String command, long timeout) {
        Process process = null;
        InputStream pIn = null;
        InputStream pErr = null;
        StreamGobbler outputGobbler = null;
        StreamGobbler errorGobbler = null;
        Future<Integer> executeFuture = null;
        try {
            process = Runtime.getRuntime().exec(command);
            final Process p = process;

            // close process's output stream.
            p.getOutputStream().close();

            pIn = process.getInputStream();
            outputGobbler = new StreamGobbler(pIn);
            outputGobbler.start();

            pErr = process.getErrorStream();
            errorGobbler = new StreamGobbler(pErr);
            errorGobbler.start();

            // create a Callable for the command's Process which can be called by an Executor
            Callable<Integer> call = new Callable<Integer>() {
                public Integer call() throws Exception {
                    p.waitFor();
                    return p.exitValue();
                }
            };

            // submit the command's call and get the result from a
            executeFuture = pool.submit(call);
            int exitCode = executeFuture.get(timeout, TimeUnit.MILLISECONDS);
            return new Answer(exitCode, outputGobbler.getContent(), null);
        } catch (IOException ex) {
            String errorMessage = "The command [" + command + "] execute failed.";
//            logger.error(errorMessage, ex);
            return new Answer(-1, null, ex);
        } catch (TimeoutException ex) {
            String errorMessage = "The command [" + command + "] timed out.";
//            logger.error(errorMessage, ex);
            return new Answer(-1, null, ex);
        } catch (ExecutionException ex) {
            String errorMessage = "The command [" + command + "] did not complete due to an execution error.";
//            logger.error(errorMessage, ex);
            return new Answer(-1, null, ex);
        } catch (InterruptedException ex) {
            String errorMessage = "The command [" + command + "] did not complete due to an interrupted error.";
//            logger.error(errorMessage, ex);
            return new Answer(-1, null, ex);
        } finally {
            if (executeFuture != null) {
                try {
                    executeFuture.cancel(true);
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
            }
            if (pIn != null) {
                this.closeQuietly(pIn);
                if (outputGobbler != null && !outputGobbler.isInterrupted()) {
                    outputGobbler.interrupt();
                }
            }
            if (pErr != null) {
                this.closeQuietly(pErr);
                if (errorGobbler != null && !errorGobbler.isInterrupted()) {
                    errorGobbler.interrupt();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
    }

    private void closeQuietly(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException e) {
//            logger.error("exception", e);
        }
    }
    
    
    
    
    class StreamGobbler extends Thread {
        private InputStream inputStream;
        private StringBuilder buf;
        private volatile boolean isStopped = false;

        /**
         * @param inputStream the InputStream to be consumed
         * @param streamType  the stream type (should be OUTPUT or ERROR)
         */
        public StreamGobbler(final InputStream inputStream) {
            this.inputStream = inputStream;
            this.buf = new StringBuilder();
            this.isStopped = false;
        }

        /**
         * Consumes the output from the input stream and displays the lines consumed
         * if configured to do so.
         */
        @Override
        public void run() {
            try {
                // 默认编码为UTF-8，这里设置编码为GBK，因为WIN7的编码为GBK
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    this.buf.append(line + "\n");
                }
            } catch (IOException ex) {
//                logger.trace("Failed to successfully consume and display the input stream of type " + streamType + ".", ex);
            } finally {
                this.isStopped = true;
                synchronized (this) {
                    notify();
                }
            }
        }

        public String getContent() {
            if (!this.isStopped) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException ignore) {
                        ignore.printStackTrace();
                    }
                }
            }
            return this.buf.toString();
        }
    }
    
    
    
    
    static class Answer {
        private int code;
        private String output;
        private Exception exception;

        public Answer(int exitCode, String executeOut, Exception exception) {
            this.code = exitCode;
            this.output = executeOut;
            this.exception = exception;
        }

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getOutput() {
			return output;
		}

		public void setOutput(String output) {
			this.output = output;
		}

		public Exception getException() {
			return exception;
		}

		public void setException(Exception exception) {
			this.exception = exception;
		}
        
		public boolean hasException() {
			return this.exception != null;
		}
		
		
    }
}
