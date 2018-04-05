package com.zoo.mix;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;

import com.zoo.cons.Charsets;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

public final class Charsetor {

	private Charsetor() {}
	
	public static final Charset UTF8=Charset.forName(Charsets.UTF8);
	public static final Charset GBK=Charset.forName(Charsets.GBK);
	public static final Charset GB2312=Charset.forName(Charsets.GB2312);
	/**
	 * 初始化文件字符集识别对象
	 */
	private static final CodepageDetectorProxy DETECTOR = CodepageDetectorProxy.getInstance();
	
	static {
		DETECTOR.add(new ParsingDetector(false));
		DETECTOR.add(JChardetFacade.getInstance());
		DETECTOR.add(ASCIIDetector.getInstance());
		DETECTOR.add(UnicodeDetector.getInstance());
	}
	
	/**
	 * 判断是否是java支持的字符集。
	 * @param charsetName
	 * @return
	 */
	public static boolean isSupported(String charsetName) {
		try {
			return charsetName!=null && Charset.isSupported(charsetName);
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 如果java支持该字符集则返回Charset对象，否则返回java默认的Charset对象。
	 * @param charsetName
	 * @return
	 */
	public static Charset elseDefault(String charsetName) {
		return elseGet(charsetName,Charset.defaultCharset());
	}
	
	/**
	 * 如果java支持字符集charsetName则返回该字符集的Charset对象，否则返回other。
	 * @param charsetName
	 * @param other
	 * @return
	 */
	public static Charset elseGet(String charsetName,Charset other) {
		return isSupported(charsetName)?Charset.forName(charsetName):other;
	}
	
	/**
	 * 识别path的字符集，若识别失败则返回defChartset
	 * @param path
	 * @param defChartset
	 * @return
	 * @see #discern(Path)
	 */
	public static Charset discern(Path path,Charset defChartset) {
		Charset charset=discern(path);
		return (charset==null||"void".equals(charset.displayName()))?defChartset:charset;
	}
	
	/**
	 * 识别url的字符集，若识别失败则返回defChartset
	 * @param url
	 * @param defChartset
	 * @return
	 * @see #discern(URL)
	 */
	public static Charset discern(URL url,Charset defChartset) {
		Charset charset=discern(url);
		return (charset==null||"void".equals(charset.displayName()))?defChartset:charset;
	}
	
	/**
	 * 识别is的字符集，若识别失败则返回defChartset
	 * @param is
	 * @param defChartset
	 * @return
	 * @see #discern(InputStream)
	 */
	public static Charset discern(InputStream is,Charset defChartset) {
		Charset charset=discern(is);
		return (charset==null||"void".equals(charset.displayName()))?defChartset:charset;
	}
	
	/**
	 * 识别文件的字符集并返回Charset对象，未成功识别将返回null。
	 * @param path
	 * @return
	 */
	public static Charset discern(Path path) {
		Charset charset = null;
		if (Filer.isReadableFile(path)) {
			try {
				charset = DETECTOR.detectCodepage(path.toUri().toURL());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return charset;
	}
	
	/**
	 * 识别url的字符集并返回Charset对象，未成功识别将返回null。
	 * @param url
	 * @return
	 */
	public static Charset discern(URL url) {
		Charset charset = null;
		if (url!=null) {
			try {
				charset = DETECTOR.detectCodepage(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return charset;
	}
	
	/**
	 * 识别一个输入流的字符集并返回Charset对象，未成功识别将返回null。
	 * @param is
	 * @return
	 */
	public static Charset discern(InputStream is) {
		Charset charset = null;
		if (is!=null) {
			try {
				charset = DETECTOR.detectCodepage(is,6);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return charset;
	}
}
