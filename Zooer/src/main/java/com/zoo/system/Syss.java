package com.zoo.system;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import com.zoo.base.Strs;

/**
 * 系统工具类
 * @author ZOO
 *
 */
public final class Syss {
	private Syss(){}

	private static Toolkit defaultToolKit;

	static{
		try {
			defaultToolKit = Toolkit.getDefaultToolkit();
		} catch (Throwable e) {}
	}
	
	
	
	/**
	 * 获取屏幕分辨率对象
	 * @return
	 */
	public static Optional<Dimension> screenDimension() {
		try {
			return Optional.ofNullable(defaultToolKit.getScreenSize());
		} catch (Exception e) {}
		return Optional.empty();
	}
	
	
	/**
	 * 获取Insets对象
	 * @return
	 */
	public static Optional<Insets> insets(){
		try {
			Insets insets = defaultToolKit.getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
			return Optional.ofNullable(insets);
		} catch (Exception e) {}
		return Optional.empty();
	}
	
	
	
	/**
	 * 获取屏幕截图
	 * @return 
	 */
	public static Optional<BufferedImage> screenCatpure() {
		if(!GraphicsEnvironment.isHeadless()) {
			try {
				Robot robot=new Robot();
				return Optional.ofNullable(robot.createScreenCapture(new Rectangle(screenWidth(), screenHeight())));
			} catch (AWTException e) {
			}
		}
		return Optional.empty();
	}
	
	
	/**
	 * 获取屏幕的宽的分辨率
	 * 
	 * @return
	 */
	public static int screenWidth() {
		return screenDimension().map(sd->sd.width).orElse(0);
	}

	
	/**
	 * 获取屏幕高的分辨率
	 * 
	 * @return
	 */
	public static int screenHeight() {
		return screenDimension().map(sd->sd.height).orElse(0);
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
		return Windows.is;
	}
	
	
	/**
	 * 判断当前系统是否是mac平台
	 * @return
	 */
	public static boolean isMac() {
		return Mac.is;
	}
	
	
	/**
	 * 判断当前系统是否是unix平台
	 * @return
	 */
	public static boolean isUnix() {
		return Unix.is;
	}
	
	
	
	/**
	 * 判断当前系统是否是solaris平台
	 * @return
	 */
	public static boolean isSolaris() {
		return Solaris.is;
	}
	
	
	/**
	 * 判断当前系统平台是否是64位
	 * @return
	 */
	public static boolean is64() {
		return Archer.IS64;
	}
	
	/**
	 * 判断当前系统平台是否是32位
	 * @return
	 */
	public static boolean is32() {
		return Archer.IS32;
	}
	
	/**
	 * 获取当前系统的os name.
	 * @return
	 */
	public static String osName(){
		return osNamer.NAME;
	}
	
	
	private static class osNamer{
    	private static final String NAME = System.getProperty("os.name",Strs.empty());
    	private static final String NAME_LOWER = NAME.toLowerCase();
    }
	
	
	private static class Windows{
		private static final boolean is=osNamer.NAME_LOWER.contains("windows");
	}
	
	
	private static class Mac{
		private static final boolean is=osNamer.NAME_LOWER.contains("mac");
	}
	
	
	private static class Unix{
		private static final boolean is=osNamer.NAME_LOWER.contains("nix") || osNamer.NAME_LOWER.contains("nux") ||osNamer.NAME_LOWER.contains("aix");
	}
	
	
	private static class Solaris{
		private static final boolean is=osNamer.NAME_LOWER.contains("sunos");
	}
	
	
	private static class Archer{
    	private static final String ARCH = System.getProperty("sun.arch.data.model",Strs.empty());
    	private static final boolean IS64=ARCH.equals("64");
    	private static final boolean IS32=ARCH.equals("32");
    }
	
	
	private static class Desktoper{
		private static final Desktop desktop=getDesktop();
		private static Desktop getDesktop() {
			if (!GraphicsEnvironment.isHeadless() && Desktop.isDesktopSupported()) {
				return Desktop.getDesktop();
			}
			return null;
		}
	}
	
	
	/**
	 * 向系统发送浏览该url的指令
	 * @param url
	 * @return 发送指令成功为true,发送失败为false
	 */
	public static boolean browse(String url){
		try {
			return browse(new URI(url));
		} catch (Exception e) {}
		return false;
	}
	
	
	/**
	 * 向系统发送浏览该uri的指令
	 * @param uri
	 * @return 发送指令成功为true,发送失败为false
	 */
	public static boolean browse(URI uri){
		Desktop desktop = Desktoper.desktop;
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}

	/**
	 * 用默认的应用打开文件
	 * @param filename
	 * @return
	 */
	public static boolean open(String filename) {
		return open(new File(filename));
	}
	
	/**
	 * 用默认的应用打开文件
	 * @param file
	 * @return
	 */
	public static boolean open(Path file) {
		return Objects.nonNull(file)?open(file.toFile()):false;
	}
	
	
	/**
	 * 用默认的应用打开文件
	 * @param filename
	 * @return
	 */
	public static boolean open(File file) {
		Desktop desktop = Desktoper.desktop;
		if (Objects.nonNull(file) && desktop.isSupported(Desktop.Action.OPEN)) {
			try {
				desktop.open(file);
			} catch (Exception e) {}
		}
		return false;
	}
	
	
	/**
	 * 用默认的文件浏览器打开文件夹,并选中文件(若传入的是文件)
	 * @param filename
	 * @return
	 */
	public static boolean browseFileDirectory(String filename) {
		return browseFileDirectory(new File(filename));
	}
	
	
	/**
	 * 用默认的文件浏览器打开文件夹,并选中文件(若传入的是文件)
	 * @param path
	 * @return
	 */
	public static boolean browseFileDirectory(Path path) {
		return Objects.nonNull(path)?browseFileDirectory(path.toFile()):false;
	}
	
	/**
	 * 用默认的文件浏览器打开文件夹,并选中文件(若传入的是文件)
	 * @param file
	 * @return
	 */
	public static boolean browseFileDirectory(File file) {
		Desktop desktop = Desktoper.desktop;
		if (desktop.isSupported(Desktop.Action.BROWSE_FILE_DIR)) {
			try {
				desktop.browseFileDirectory(file);
			} catch (Exception e) {}
		}
		return false;
	}
	
	
}
