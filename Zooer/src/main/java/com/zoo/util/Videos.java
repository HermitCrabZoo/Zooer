package com.zoo.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import com.zoo.cons.Imagec;

import org.bytedeco.javacv.FrameGrabber.Exception;

public final class Videos {
	private Videos(){}
	
	private static final Java2DFrameConverter frameConverter=new Java2DFrameConverter();
	
	/**
	 * 截取视频文件中指定位置的一帧图像,写入的输出流,并返回这一帧图像
	 * @param videoFile
	 * @param position
	 * @param ops
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage imageToFile(File videoFile,int position,File file) throws IOException{
		BufferedImage image=image(videoFile, position);
		if (ImageIO.write(image, Imagec.JPEG, file)) {
			return image;
		}else {
			throw new IOException("Could not write an image of format JPEG to "+file);
		}
	}
	/**
	 * 截取视频文件中指定位置的一帧图像,写入的输出流,并返回这一帧图像
	 * @param videoFile
	 * @param position
	 * @param ops
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage imageToStream(File videoFile,int position,OutputStream ops) throws IOException{
		BufferedImage image=image(videoFile, position);
		if (ImageIO.write(image, Imagec.JPEG, ops)) {
			return image;
		}else {
			throw new IOException("Could not write an image of format JPEG to "+ops);
		}
	}
	/**
	 * 截取视频文件中的一帧图像返回
	 * @param videoFile
	 * @param position
	 * @return
	 */
	public static BufferedImage image(File videoFile,int position){
		return Optional.ofNullable(frame(videoFile, position)).map(f->frameConverter.convert(f)).orElse(null);
	}
	
	/**
	 * 获取视频文件中指定位置中的一帧的帧对象返回
	 * @param videoFile
	 * @param position
	 * @return
	 */
	public static Frame frame(File videoFile,int position){
		Frame frame = null;
		try (FrameGrabber grabber = FFmpegFrameGrabber.createDefault(videoFile)){
			try {
				grabber.start();
			} catch (Exception e) {
				grabber.restart();
			}
			grabber.setFrameNumber(position);
			frame = grabber.grab();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return frame;
	}
	

}
