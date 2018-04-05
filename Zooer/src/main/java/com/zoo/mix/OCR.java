package com.zoo.mix;

import java.awt.image.BufferedImage;

import com.zoo.base.Strs;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

public final class OCR {

	private OCR() {}
	
	private static String tessdataFolder=tessdataFolder();
	
	private static String language=language();
	
	private static final String tessdataFolder() {
		return LoadLibs.extractTessResources("tessdata").toString();
	}
	
	private static final String language() {
		return "eng";
	}
	
	
	/**
	 * 设置全局的tessdata存放的目录
	 * @param path
	 */
	public static final void setTessdataFolder(String path) {
		tessdataFolder=path;
	}
	
	
	/**
	 * 设置全局的language
	 * @param language
	 */
	public static final void setLanguage(String language) {
		OCR.language=language;
	}
	
	
	/**
	 * 读取图片中的文字,返回读取结果,若读取失败则返回空字符串
	 * @param image
	 * @return 不会返回null
	 */
	public static String read(BufferedImage image) {
		Tesseract instance = new Tesseract();
		instance.setDatapath(tessdataFolder);
		instance.setLanguage(language);
		try {
			String result=instance.doOCR(image);
			return result;
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		return Strs.empty();
	}
}
