package com.zoo.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Optional;
import org.bytedeco.javacv.Frame;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

public final class CvBridge {

	private CvBridge() {}
	
	/**
	 * 存在4通道的BufferedImage类型
	 */
	private static final int[] JAVA_IMG_TYPE_4CHANNELS= {BufferedImage.TYPE_4BYTE_ABGR,BufferedImage.TYPE_4BYTE_ABGR_PRE,BufferedImage.TYPE_INT_ARGB,BufferedImage.TYPE_INT_ARGB_PRE};
	
	/**
	 * 存在4通道的Mat类型
	 */
	private static final int[] OPENCV_MAT_TYPE_4CHANNELS= {CvType.CV_8UC4,CvType.CV_8SC4,CvType.CV_16UC4,CvType.CV_16SC4,CvType.CV_32SC4,CvType.CV_32FC4,CvType.CV_64FC4};
	
	/**
	 * 是否未加载opencv库文件
	 */
	private static boolean unload=true;
	
	
	static {loadOpenCv();}
	
	/**
	 * 加载OpenCV库
	 */
	public static final void loadOpenCv() {
		if (unload) {
			try {
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
	
	
	public static Mat toMat(BufferedImage image) {
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
		
		int channel=mat.channels(),row=mat.rows(),col=mat.cols();
		int bufferSize = channel*row*col;
		
		int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( channel > 3 ) {
        	type = BufferedImage.TYPE_4BYTE_ABGR;
        }else if (channel>1) {
        	type = BufferedImage.TYPE_3BYTE_BGR;
		}
        
        byte [] b = new byte[bufferSize];
        mat.get(0,0,b); // get all the pixels
        
        BufferedImage image = new BufferedImage(col,row, type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        
        if (type == BufferedImage.TYPE_4BYTE_ABGR) {
        	//拷贝像素值,并将每个像素分量排布从BGRA转为ABGR
            for(int i=0;i<bufferSize;i+=4) {
            	targetPixels[i]=b[i+3];
            	System.arraycopy(b, i, targetPixels, i+1, 3);
            }
		}else {//直接拷贝
        	System.arraycopy(b, 0, targetPixels, 0, bufferSize);
		}
        return image;
	}
	
	
	/**
	 * 通过Mat的图片类型返回输入图片的后缀
	 * @param mat
	 * @return
	 */
	private static String imgExt(Mat mat) {
		if (Arrs.contains(OPENCV_MAT_TYPE_4CHANNELS, mat.type())) {
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
		if (Arrs.contains(JAVA_IMG_TYPE_4CHANNELS, image.getType())) {
			return CvType.CV_8UC4;
		}
		return CvType.CV_8UC3;
	}
}
