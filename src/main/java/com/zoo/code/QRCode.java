package com.zoo.code;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.zoo.base.Strs;
import com.zoo.cons.Charsets;
/**
 * quick response code process tool
 * @author ZOO
 *
 */
public final class QRCode {
	
	private QRCode(){}
	
	
	private static MultiFormatWriter writer=new MultiFormatWriter();
	
	private static MultiFormatReader reader=new MultiFormatReader();
	
	private static Map<EncodeHintType, Object> writerHints=new HashMap<EncodeHintType,Object>(){
		private static final long serialVersionUID = 1L;
	{
		put(EncodeHintType.CHARACTER_SET, Charsets.UTF8);
		put(EncodeHintType.MARGIN, 0);
		put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
	}};
	
	private static Map<DecodeHintType, Object> readerHints=new HashMap<DecodeHintType,Object>(){
		private static final long serialVersionUID = 1L;
	{
		put(DecodeHintType.CHARACTER_SET, Charsets.UTF8);
	}};
	
	
	/**
	 * 通过任何非空字符串生成Quick Response Code 图片
	 * @param content 非空字符串
	 * @param width 图片宽
	 * @param height 图片高
	 * @return
	 */
	public static BufferedImage qrCode(String content,int width,int height){
		return qrCode(content,width,height,Color.BLACK,Color.WHITE);
	}
	
	
	/**
	 * 通过任何非空字符串生成Quick Response Code 图片,背景默认白色
	 * @param content 非空字符串
	 * @param width 图片宽
	 * @param height 图片高
	 * @param color 前景颜色
	 * @return
	 */
	public static BufferedImage qrCodeWithForeground(String content,int width,int height,Color color){
		return qrCode(content,width,height,color,Color.WHITE);
	}
	
	
	/**
	 * 通过任何非空字符串生成Quick Response Code 图片,前景默认黑色
	 * @param content 非空字符串
	 * @param width 图片宽
	 * @param height 图片高
	 * @param bgColor 背景颜色
	 * @return
	 */
	public static BufferedImage qrCodeWithBackground(String content,int width,int height,Color bgColor){
		return qrCode(content,width,height,Color.BLACK,bgColor);
	}
	
	
	/**
	 * 通过任何非空字符串生成Quick Response Code 图片
	 * @param content 非空字符串
	 * @param width 图片宽
	 * @param height 图片高
	 * @param color 前景颜色
	 * @param bgColor 背景颜色
	 * @return
	 */
	public static BufferedImage qrCode(String content,int width,int height,Color color,Color bgColor){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		if (content!=null&&!content.isEmpty()) {
			try {
				BitMatrix matrix= writer.encode(content, BarcodeFormat.QR_CODE, width, height, writerHints);
				image=MatrixBridge.toBufferedImage(matrix, color, bgColor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return image;
	}
	
	
	/**
	 * 通过任何非空字符串生成Quick Response Code 图片
	 * @param content 非空字符串
	 * @param width 图片宽
	 * @param height 图片高
	 * @param foreImage 前景图
	 * @param backImage 背景图
	 * @return
	 */
	public static BufferedImage qrCode(String content,int width,int height,BufferedImage foreImage,BufferedImage backImage){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		if (content!=null&&!content.isEmpty()) {
			try {
				BitMatrix matrix= writer.encode(content, BarcodeFormat.QR_CODE, width, height, writerHints);
				image=MatrixBridge.toBufferedImage(matrix, foreImage, backImage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return image;
	}
	
	
	
	public static BufferedImage qrCode(String content,int size) {
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		if (content!=null&&!content.isEmpty()) {
			try {
				new EyeQRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, writerHints, null, null, null, true, image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return image;
	}
	
	
	
	/**
	 * 从BufferedImage处读取Quick Response Code 并解析成字符串返回,若解析失败将返回空字符串
	 * @param image
	 * @return
	 */
	public static String decode(BufferedImage image){
		try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap bitmap = new BinaryBitmap(binarizer);
            Result result = reader.decode(bitmap, readerHints);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return Strs.empty();
	}
}
