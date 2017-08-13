package com.zoo.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

import com.zoo.cons.Charsetc;

public class FileCharset {
	
	private boolean found = false;
    private String encoding = null;

    
    /**
     * 传入一个文件(File)对象，检查文件编码
     * 
     * @param file
     *            File对象实例
     * @return 返回确定或可能的文件编码字符串数组，若无则返回空字符串
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String[] getCharsetsProbable(File file) throws FileNotFoundException, IOException {
    	return getCharset(file, new nsDetector());
    }
    /**
     * 传入一个文件(File)对象，检查文件编码
     * 
     * @param file
     *            File对象实例
     * @return 文件编码，若无，则返回null
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String getCharset(File file) throws FileNotFoundException, IOException {
        String[] charsets=getCharset(file, new nsDetector());
        return charsets.length>0?charsets[0]:null;
    }

    /**
     * <pre>
     * 获取文件的编码
     * @param file
     *            File对象实例
     * @param languageHint
     *            语言提示区域代码 @see #nsPSMDetector ,取值如下：
     *             1 : Japanese
     *             2 : Chinese
     *             3 : Simplified Chinese
     *             4 : Traditional Chinese
     *             5 : Korean
     *             6 : Dont know(default)
     * </pre>
     * 
     * @return 文件编码，eg：UTF-8,GBK,GB2312形式(不确定的时候，返回可能的字符编码序列)；若无，则返回null
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String getCharset(File file, int languageHint) throws FileNotFoundException, IOException {
    	String[] charsets=getCharset(file, new nsDetector(languageHint));
        return charsets.length>0?charsets[0]:null;
    }

    /**
     * 获取文件的编码
     * 
     * @param file
     * @param det
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String[] getCharset(File file, nsDetector det) throws FileNotFoundException, IOException {
        // Set an observer...
        // The Notify() will be called when a matching charset is found.
        det.Init(new nsICharsetDetectionObserver() {
            public void Notify(String charset) {
                encoding = charset;
                found = true;
            }
        });

        BufferedInputStream imp = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[1024];
        int bufLen=buf.length;
        int len;
        boolean done = false;
        boolean isAscii = false;

        while ((len = imp.read(buf, 0, bufLen)) != -1) {
            // Check if the stream is only ascii.
            isAscii = det.isAscii(buf, len);
            if (isAscii) {
                break;
            }
            // DoIt if non-ascii and not done yet.
            done = det.DoIt(buf, len, false);
            if (done) {
                break;
            }
        }
        imp.close();
        det.DataEnd();

        if (isAscii) {
            encoding = Charsetc.ASCII;
            found = true;
        }

        if (!found) {
        	return det.getProbableCharsets();
        }
        return new String[]{encoding};
    }
}
