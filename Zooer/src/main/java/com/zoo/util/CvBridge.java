package com.zoo.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import javax.imageio.ImageIO;

import org.bytedeco.javacv.Frame;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

public final class CvBridge {

	private CvBridge() {}
	
	/**
	 * 是否
	 */
	private static boolean unload=true;
	
	static {loadOpenCv();}
	
	/**
	 * 加载OpenCV库
	 */
	public static final void loadOpenCv() {
		if (unload) {
			try {
				//windows平台的库文件
//				System.load("E:\\GitRepositories\\Zooer\\Zooer\\lib\\opencv_java331.dll");
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				unload=false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 使用opencv将图片翻转保存到新图片，其中图片读写部分较快，单纯的图片翻转操作建议使用此方法。
	 * 图片翻转，flipCode为0、1、-1分别代表垂直翻转(x轴)、水平翻转(y轴)、垂直水平都翻转
	 * @param sourceFileName 原始文件路径
	 * @param destFileName 反转后的输出文件路径
	 * @param flipCode
	 * @return
	 */
	public static boolean flip(String sourceFileName,String destFileName,int flipCode) {
		Mat mat=Imgcodecs.imread(sourceFileName);
		Mat dst=new Mat(mat.size(), mat.type());
		Core.flip(mat, dst, flipCode);
		return Imgcodecs.imwrite(destFileName, dst);
	}
	
	/**
	 * 将BufferedImage对象转换为JavaCV的Frame对象
	 * @param image
	 * @return
	 */
	public static Frame frame(BufferedImage image) {
		return Optional.ofNullable(image).map(img->Videor.getFrameconverter().convert(img)).orElse(null);
	}
	
	/**
	 * 将JavaCV的Frame对象转换为BufferedImage对象
	 * @param frame
	 * @return
	 */
	public static BufferedImage image(Frame frame) {
		return Optional.ofNullable(frame).map(f->Videor.getFrameconverter().convert(f)).orElse(null);
	}
	
	/**
	 * 将BufferedImage对象转换为OpenCV的Mat对象
	 * @param image
	 * @return
	 */
	public static Mat mat(BufferedImage image) {
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat mat=Mat.eye(new Size(image.getWidth(), image.getHeight()), matType(image));
		mat.put(0, 0, pixels);
		return mat;
	}
	
	/**
	 * 将OpenCV的Mat对象转换为BufferedImage对象
	 * @param frame
	 * @return
	 */
	public static BufferedImage image(Mat mat) {
		BufferedImage image=null;
		MatOfByte buf=new MatOfByte();
		Imgcodecs.imencode(imgExt(mat), mat, buf);
		byte[] arr=buf.toArray();
		try (ByteArrayInputStream in = new ByteArrayInputStream(arr)){
			image=ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	/**
	 * 通过Mat的图片类型返回输入图片的后缀
	 * @param mat
	 * @return
	 */
	private static String imgExt(Mat mat) {
		mat.type();
		if (mat.type()==CvType.CV_8UC4) {
			return ".png";
		}
		return ".jpg";
	}
	
	/**
	 * 通过BufferedImage的图片类型返回对应的OpenCV里Mat对应的类型
	 * @param image
	 * @return
	 */
	private static int matType(BufferedImage image) {
		if (image.getType()==BufferedImage.TYPE_4BYTE_ABGR) {
			return CvType.CV_8UC4;
		}
		return CvType.CV_8UC3;
	}
}
