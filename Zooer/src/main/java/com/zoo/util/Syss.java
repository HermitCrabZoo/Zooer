package com.zoo.util;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.Properties;

/**
 * 系统工具类
 * @author ZOO
 *
 */
public final class Syss {
	private Syss(){}

	private static Properties systemProperties;
	private static Toolkit defaultToolKit;
	private static Dimension screenDimension;
	private static Boolean isWindows;
	private static String osName;

	static{
		try {
			systemProperties = System.getProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			defaultToolKit = Toolkit.getDefaultToolkit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			screenDimension = defaultToolKit.getScreenSize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取屏幕截图
	 * @return
	 */
	public static BufferedImage screenCatpure() {
		if(!GraphicsEnvironment.isHeadless()) {
			try {
				Robot robot=new Robot();
				return robot.createScreenCapture(new Rectangle(screenWidth(), screenHeight()));
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 获取屏幕的宽的分辨率
	 * 
	 * @return
	 */
	public static int screenWidth() {
		return Optional.ofNullable(screenDimension).map(sd->sd.width).orElse(0);
	}

	/**
	 * 获取屏幕高的分辨率
	 * 
	 * @return
	 */
	public static int screenHeight() {
		return Optional.ofNullable(screenDimension).map(sd->sd.height).orElse(0);
	}

	/**
	 * 获取屏幕对角线的分辨率
	 * 
	 * @return
	 */
	public static double screenDiagonal() {
		return Math.sqrt(Math.pow(screenWidth(), 2) + Math.pow(screenHeight(), 2));
	}

	/**
	 * 获取屏幕的DPI
	 * 
	 * @return
	 */
	public static int screenDpi() {
		return Optional.ofNullable(defaultToolKit).map(dt->dt.getScreenResolution()).orElse(0);
	}

	/**
	 * 获取屏幕宽的英寸值
	 * 
	 * @return
	 */
	public static double screenWidthInch() {
		return screenWidth() / (double) screenDpi();
	}

	/**
	 * 获取屏幕高的英寸值
	 * 
	 * @return
	 */
	public static double screenHeightInch() {
		return screenHeight() / (double) screenDpi();
	}

	/**
	 * 获取屏幕对角线的英寸值
	 * 
	 * @return
	 */
	public static double screenDiagonalInch() {
		return screenDiagonal() / screenDpi();
	}
	
	/**
	 * 判断当前系统是否是windows平台
	 * @return
	 */
	public static boolean isWindows() {
		isWindows=Optional.ofNullable(isWindows).orElse(osName().toUpperCase().startsWith("WINDOWS")?true:false);
		return isWindows;
	}
	
	/**
	 * 获取当前系统的os name.
	 * @return
	 */
	public static String osName(){
		osName=Optional.ofNullable(osName).orElse(Optional.ofNullable(systemProperties).map(sp->sp.getProperty("os.name")).orElse(null));
		return Optional.ofNullable(osName).orElse(Strs.empty());
	}
}
