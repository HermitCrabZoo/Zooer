package com.zoo.system;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 命令行操作类，提供执行系统command的能力，并获取输出结果。
 *
 */
public class Cmder {

    private static ExecutorService pool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 3L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    
    
    /**
     * 执行控制台命令，直到结束，或抛异常，并返回{@link Answer}实例，该实例包含命令执行的exit code、stdout、stderr以及异常(若有的话)。
     * @param command 执行的命令
     * @return 返回{@link Answer}实例，该实例包含命令执行的exit code、stdout、stderr以及异常(若有的话)。
     */
    public static Answer execute(String command) {
    	return execute(command, Future::get, null);
    }
    
    
    /**
     * 执行命令，不指定超时，知道命令执行完成。
     * @param command 执行的命令
     * @param charset 指定执行的命令的标准输出流所用的字符集
     * @return 返回{@link Answer}实例，该实例包含命令执行的exit code、stdout、stderr以及异常(若有的话)。
     */
    public static Answer execute(String command, Charset charset) {
    	return execute(command, Future::get, charset);
    }
    
    
    /**
     * 执行控制台命令，直到结束。
     * @param command 执行的命令
     * @param timeout 指定超时的时长，若命令执行超时，则会返回的{@link Answer}实例会包含TimeoutException实例。
     * @param unit timeout参数的单位
     * @return 返回{@link Answer}实例，该实例包含命令执行的exit code、stdout、stderr以及异常(若有的话)。
     */
    public static Answer execute(String command, long timeout, TimeUnit unit) {
    	return execute(command, t->t.get(timeout, unit), null);
    }
    
    
    /**
     * 执行控制台命令，直到结束，或抛异常，并返回{@link Answer}实例，该实例包含命令执行的exit code、stdout、stderr以及异常(若有的话)。
     * 指定超时，若命令执行超时，则会返回的{@link Answer}实例会包含TimeoutException实例。
     * @param command 执行的命令
     * @param timeout 超时时长
     * @param unit timeout参数的单位
     * @param charset 指定执行的命令的标准输出流所用的字符集
     * @return
     */
    public static Answer execute(String command, long timeout, TimeUnit unit, Charset charset) {
    	return execute(command, t->t.get(timeout, unit), charset);
    }
    
    
    private static Answer execute(String command, Function<Future<Integer>, Integer> getter, Charset charset) {
    	charset = charset == null ? Charset.defaultCharset() : charset;
        try(CloseableProcess cp = CloseableProcess.of(command);
        		InputStream stdout = cp.getProcess().getInputStream();
        		InputStream stderr = cp.getProcess().getErrorStream();
        		OutputStream stdin = cp.getProcess().getOutputStream();
        		StreamGobbler stdoutGobbler = new StreamGobbler(stdout, charset);
        		StreamGobbler stderrGobbler = new StreamGobbler(stderr, charset);
        		CloseableFuture<Integer> cf = new CloseableFuture<>(null)) {
        	
            final Process p = cp.getProcess();

            stdoutGobbler.start();
            stderrGobbler.start();

            // create a Callable for the command's Process which can be called by an Executor
//            Callable<Integer> call = new Callable<Integer>() {
//                public Integer call() throws Exception {
//                    p.waitFor();
//                    return p.exitValue();
//                }
//            };

            // submit the command's call and get the result from a
            cf.setFuture(pool.submit(()->p.waitFor()));
            int exitCode = getter.apply(cf.getFuture());
            return new Answer(exitCode, stdoutGobbler.getContent(), stderrGobbler.getContent(), null);
        } catch (Exception e) {
            return new Answer(-1, null, null, e);
        }
    }
    
    
    @FunctionalInterface
    private static interface Function<T, R>{
    	/**
         * Applies this function to the given argument.
         *
         * @param t the function argument
         * @return the function result
         */
        R apply(T t) throws Exception;
    }
    
    
    /**
     * 可关闭的Process，实现了Closeable接口，为了兼容Try With Resources.
     *
     */
    static class CloseableProcess implements Closeable{
    	
    	public static CloseableProcess of(String command) throws IOException {
    		return new CloseableProcess(command);
    	}
    	
    	private Process process = null;
    	
    	public CloseableProcess(String command) throws IOException {
    		this.process = Runtime.getRuntime().exec(command);
    	}
    	
		public Process getProcess() {
			return process;
		}


		public void setProcess(Process process) {
			this.process = process;
		}

		@Override
		public void close() throws IOException {
			if (process!=null)
				process.destroy();
		}
    	
    }
    
    
    /**
     * 可关闭的Future，实现了Closeable接口，为了兼容Try With Resources.
     *
     */
    static class CloseableFuture<T> implements Closeable{
    	
    	private Future<T> future = null;
    	
    	public CloseableFuture(Future<T> future){
    		this.future = future;
    	}

		public Future<T> getFuture() {
			return future;
		}

		public void setFuture(Future<T> future) {
			this.future = future;
		}

		@Override
		public void close() throws IOException {
			if (future!=null)
				future.cancel(true);
		}
    	
    }
    
    
    public static class StreamGobbler extends Thread implements Closeable{
        private InputStream inputStream;
        private StringBuilder buf = new StringBuilder();
        private volatile boolean isStopped = false;
        private Charset charset = Charset.defaultCharset();

        /**
         * @param inputStream the InputStream to be consumed
         * @param streamType  the stream type (should be OUTPUT or ERROR)
         */
        public StreamGobbler(InputStream inputStream) {
            this.inputStream = inputStream;
        }
        
        public StreamGobbler(InputStream inputStream, Charset charset) {
            this.inputStream = inputStream;
            this.charset = charset;
        }

        /**
         * Consumes the output from the input stream and displays the lines consumed
         * if configured to do so.
         */
        @Override
        public void run() {
            try (
            		InputStreamReader isr = new InputStreamReader(inputStream, this.charset);
            		BufferedReader br = new BufferedReader(isr)){
                String line = null;
                while ((line = br.readLine()) != null) {
                    this.buf.append(line).append("\n");
                }
            } catch (IOException ex) {
            	ex.printStackTrace();
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

		@Override
		public void close() throws IOException {
			if (!isInterrupted()) {
                interrupt();
            }
		}
    }
    
    
    
    /**
     * 命令行的执行结果。包含结果码、标准输出内容、错误输出内容、异常信息。
     *
     */
    public static class Answer {
        private int code;
        private String stdout;
        private String stderr;
        private Exception exception;

        public Answer(int exitCode, String stdout, String stderr, Exception exception) {
            this.code = exitCode;
            this.stdout = stdout;
            this.stderr = stderr;
            this.exception = exception;
        }
        
        /**
         * 命令执行的exit code
         * @return
         */
		public int getCode() {
			return code;
		}

		/**
		 * 标准输出内容
		 * @return
		 */
		public String getStdout() {
			return stdout;
		}

		/**
		 * 标准错误输出内容
		 * @return
		 */
		public String getStderr() {
			return stderr;
		}
		
		/**
		 * 异常，可能为null
		 * @return
		 */
		public Exception getException() {
			return exception;
		}

		public boolean hasException() {
			return this.exception != null;
		}
		
    }
}
