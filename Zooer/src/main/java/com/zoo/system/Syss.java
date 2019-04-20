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
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import com.zoo.mix.Charsetor;

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

		if (Objects.isNull(file)) {
			return false;
		}
		
		Desktop desktop = Desktoper.desktop;
		try {
			if (Platform.isWindows()) {
				if (desktop.isSupported(Desktop.Action.BROWSE_FILE_DIR)) {
					desktop.browseFileDirectory(file);
				} else {
					cmdImmediate("explorer.exe", "/select,", file.getAbsolutePath());
				}
			} else if (Platform.isMac()) {
				cmdImmediate("open", "-R", file.getAbsolutePath());
			} else if (Platform.isUnix()) {
				if (cmdSneak("kde-open", file.getParentFile().getAbsolutePath())) {}
				else if (cmdSneak("gnome-open", file.getParentFile().getAbsolutePath())) {}
				else {
					cmdSneak("xdg-open", file.getParentFile().getAbsolutePath());
				}
			}
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	
	/**
	 * 执行cmd命令，不阻塞当前线程，不报错则返回true。
	 * @param cmdarray
	 * @return
	 */
	public static boolean cmdSneak(String... cmdarray){
		try {
			Runtime.getRuntime().exec(cmdarray);
			return true;
		} catch (IOException e) {}
		return false;
	}
	
	
	/**
	 * 执行cmd命令，不阻塞当前线程，直接返回。
	 * @param cmdarray
	 * @throws IOException
	 */
	public static void cmdImmediate(String... cmdarray) throws IOException{
		Runtime.getRuntime().exec(cmdarray);
	}
	
	
	/**
	 * 执行cmd命令，阻塞当前线程。
	 * @param cmdarray
	 * @throws IOException
	 */
	public static void cmdBlocking(String... cmdarray) throws IOException {
		Process process = Runtime.getRuntime().exec(cmdarray);
		try (InputStream inputStream = process.getInputStream()) {
			byte[] bytes = new byte[8192];
			while ((inputStream.read(bytes)) != -1) {
			}
		} finally {
			process.destroy();
		}
	}
	
	
	/**
	 * 执cmd命令，返回标准输出的额文本内容。
	 * @param cmdarray
	 * @return
	 * @throws IOException
	 */
	public static String cmd(String... cmdarray) throws IOException {
		Process process = Runtime.getRuntime().exec(cmdarray);
		StringBuilder sb = new StringBuilder();
		Charset charset = Platform.isWindows() ? Charsetor.GBK : Charset.defaultCharset();
		try (
				InputStream inputStream = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(inputStream, charset);
				BufferedReader reader = new BufferedReader(isr)) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line + System.lineSeparator());
			}
		} finally {
			process.destroy();
		}
		return sb.toString();
	}

}
