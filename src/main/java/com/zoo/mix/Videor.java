package com.zoo.mix;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import com.zoo.cons.Images;
import com.zoo.cons.Videos;

public final class Videor {
	private Videor(){}
	
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
		if (ImageIO.write(image, Images.PNG, file)) {
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
		if (ImageIO.write(image, Images.PNG, ops)) {
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
		try {
			FrameGrabber grabber = FFmpegFrameGrabber.createDefault(videoFile);
			try {
				grabber.start();
			} catch (Exception e) {
				grabber.restart();
			}
			grabber.setFrameNumber(position);
			frame = grabber.grab();
		} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
			e.printStackTrace();
		}
		return frame;
	}
	
	/**
	 * 转换到MP4的封装格式,视音频编码分别为h264、aac
	 * @param file
	 * @param target
	 */
	public static void toMP4(File file,File target) {
		try {
			FFmpegFrameGrabber frameGrabber = FFmpegFrameGrabber.createDefault(file);
			Frame captured_frame = null;
			frameGrabber.start();
			FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(target, frameGrabber.getImageWidth(), frameGrabber.getImageHeight(),frameGrabber.getAudioChannels());
			//avcodec.AV_CODEC_ID_H264  //AV_CODEC_ID_MPEG4
			recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);//视频编码格式
			recorder.setFormat(Videos.mp4);//封装格式
			System.out.println(frameGrabber.getVideoBitrate()/2);
			recorder.setVideoBitrate(frameGrabber.getVideoBitrate()/2);
			recorder.setFrameRate(frameGrabber.getFrameRate());//视频帧率
			//recorder.setSampleFormat(frameGrabber.getSampleFormat());  //
			recorder.setSampleRate(frameGrabber.getSampleRate());//音频采样率
			recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);//音频编码格式
			recorder.setAudioBitrate(frameGrabber.getAudioBitrate());
			recorder.start();
			while (true) {
				try {
					captured_frame = frameGrabber.grab();
					if (captured_frame == null) {
						break;
					}
					recorder.setTimestamp(frameGrabber.getTimestamp());
					recorder.record(captured_frame); 
				} catch (Exception e) {
				}
			}
			recorder.close();
			frameGrabber.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Java2DFrameConverter getFrameconverter() {
		return frameConverter;
	}

}
