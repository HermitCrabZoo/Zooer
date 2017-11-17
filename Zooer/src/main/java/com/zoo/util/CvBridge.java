package com.zoo.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

public final class CvBridge {

	private CvBridge() {}
	private static final OpenCVFrameConverter.ToMat matConverter=new OpenCVFrameConverter.ToMat();
	
	/**
	 * 图片翻转，flipCode为0、1、-1分别代表垂直翻转(x轴)、水平翻转(y轴)、垂直水平都翻转
	 * @param image
	 * @param flipCode
	 * @return
	 */
	public static BufferedImage flip(BufferedImage image,int flipCode) {
		BufferedImage image2=null;
		Frame frame=frame(image);
		if (frame!=null) {
			org.bytedeco.javacpp.opencv_core.Mat mat=matConverter.convert(frame);
			org.bytedeco.javacpp.opencv_core.Mat dst=new org.bytedeco.javacpp.opencv_core.Mat(mat.size(), mat.type());
			opencv_core.flip(mat, dst, flipCode);//翻转
			frame=matConverter.convert(dst);
			image2=Videor.getFrameconverter().convert(frame);
		}
		return image2;
	}
	public static BufferedImage flipImage(final BufferedImage bufferedimage) {
		BufferedImage img=null;
		if (bufferedimage!=null) {
			int w = bufferedimage.getWidth();
			int h = bufferedimage.getHeight();
			img=new BufferedImage(w, h, bufferedimage.getColorModel().getTransparency());
			Graphics2D graphics2d=img.createGraphics();
			graphics2d.drawImage(bufferedimage, 0, 0, w, h, w, 0, 0, h, null);
			graphics2d.dispose();
		}
        return img;
    }
	
	public static Frame frame(BufferedImage image) {
		return Optional.ofNullable(image).map(img->Videor.getFrameconverter().convert(img)).orElse(null);
	}
	
	public static BufferedImage image(Frame frame) {
		return Optional.ofNullable(frame).map(f->Videor.getFrameconverter().convert(f)).orElse(null);
	}
	
	public static Mat mat(BufferedImage image) {
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat mat=Mat.eye(new Size(image.getWidth(), image.getHeight()), matType(image));
		mat.put(0, 0, pixels);
		return mat;
	}
	
	public static BufferedImage image(Mat mat) {
		BufferedImage image=null;
		MatOfByte buf=new MatOfByte();
		Imgcodecs.imencode(mat.type()==CvType.CV_8UC4?".png":".jpg", mat, buf);
		byte[] arr=buf.toArray();
		ByteArrayInputStream in = new ByteArrayInputStream(arr);
		try {
			image=ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	private static int imgType(Mat mat) {
		mat.type();
		if (mat.type()==CvType.CV_8UC4) {
			return BufferedImage.TYPE_4BYTE_ABGR;
		}
		return BufferedImage.TYPE_3BYTE_BGR;
	}
	private static int matType(BufferedImage image) {
		if (image.getType()==BufferedImage.TYPE_4BYTE_ABGR) {
			return CvType.CV_8UC4;
		}
		return CvType.CV_8UC3;
	}
}
