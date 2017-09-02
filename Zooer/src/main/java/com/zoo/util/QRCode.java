package com.zoo.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Dimension;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.zoo.cons.Charsets;
import com.zoo.cons.Images;
/**
 * quick response code process tool
 * @author ZOO
 *
 */
public final class QRCode {
	private QRCode(){}
	
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	private static final MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
	private static final MultiFormatReader multiFormatReader=new MultiFormatReader();
	private static final Map<EncodeHintType, Object> enHints=new HashMap<EncodeHintType,Object>(){
		private static final long serialVersionUID = 1L;
	{
		put(EncodeHintType.CHARACTER_SET, Charsets.UTF8);
		put(EncodeHintType.MARGIN, 0);
		put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
	}};
	private static final Map<DecodeHintType, Object> deHints=new HashMap<DecodeHintType,Object>(){
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
	public static BufferedImage qrCodeFC(String content,int width,int height,Color color){
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
	public static BufferedImage qrCodeBC(String content,int width,int height,Color bgColor){
		return qrCode(content,width,height,Color.BLACK,bgColor);
	}
	/**
	 * 通过任何非空字符串生成Quick Response Code 图片
	 * @param content 非空字符串
	 * @param dimension 图片大小
	 * @param color 前景颜色
	 * @param bgColor 背景颜色
	 * @return
	 */
	public static BufferedImage qrCode(String content,Dimension dimension,Color color,Color bgColor){
		return qrCode(content,dimension.getWidth(),dimension.getHeight(),color,bgColor);
	}
	/**
	 * 通过任何非空字符串生成Quick Response Code 图片
	 * @param content 非空字符串
	 * @param dimension 图片大小
	 * @return
	 */
	public static BufferedImage qrCode(String content,Dimension dimension){
		return qrCode(content,dimension.getWidth(),dimension.getHeight(),Color.BLACK,Color.WHITE);
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
		int c=Optional.ofNullable(color).map(co->co.getRGB()).orElse(BLACK);
		int bgc=Optional.ofNullable(bgColor).map(bgco->bgco.getRGB()).orElse(WHITE);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		if (content!=null&&!content.isEmpty()) {
			try {
				BitMatrix matrix= multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, enHints);
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						image.setRGB(x, y, matrix.get(x, y) ? c : bgc);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return image;
	}
	/**
	 * 通过任何非空字符串生成Quick Response Code 图片返回，并将图片写入文件中
	 * @param content 非空字符串
	 * @param width 图片宽
	 * @param height 图片高
	 * @param path 目标图片文件
	 * @return 返回图片
	 * @throws Exception 若写入失败则抛异常
	 */
	public static BufferedImage writeToFile(String content,int width,int height,Path path) throws Exception{
		return writeToFile(content, new Dimension(width, height), path);
	}
	/**
	 * 通过任何非空字符串生成Quick Response Code 图片返回，并将图片写入文件中
	 * @param content 非空字符串
	 * @param dimension 图片大小
	 * @param path 目标图片文件
	 * @return 返回图片
	 * @throws Exception 若写入失败则抛异常
	 */
	public static BufferedImage writeToFile(String content,Dimension dimension,Path path) throws Exception{
		BufferedImage image =qrCode(content, dimension);
		if (ImageIO.write(image, Images.JPEG, path.toFile())) {
			return image;
		}else{
			throw new IOException("Could not write an image of format JPEG to " + path);
		}
	}
	/**
	 * 通过任何非空字符串生成Quick Response Code 图片返回，并将图片写入输出流
	 * @param content 非空字符串
	 * @param width 图片宽
	 * @param height 图片高
	 * @param stream 目标输出流
	 * @return 返回图片
	 * @throws Exception 若写入失败则抛异常
	 */
	public static BufferedImage writeToStream(String content,int width,int height, OutputStream stream) throws Exception{
		return writeToStream(content, new Dimension(width, height), stream);
	}
	/**
	 * 通过任何非空字符串生成Quick Response Code 图片返回，并将图片写入输出流
	 * @param content 非空字符串
	 * @param dimension 图片大小
	 * @param stream 目标输出流
	 * @return 返回图片
	 * @throws Exception 若写入失败则抛异常
	 */
	public static BufferedImage writeToStream(String content,Dimension dimension, OutputStream stream) throws Exception {
		BufferedImage image = qrCode(content,dimension);
		if (ImageIO.write(image, Images.JPEG, stream)) {
			return image;
		}else {
			throw new IOException("Could not write an image of format JPEG");
		}
	}
	
	/**
	 * 从URL处读取Quick Response Code 并解析成字符串返回,若解析失败将返回空字符串
	 * @param url
	 * @return
	 */
	public static String distinguish(URL url) {
		if (url!=null) {
			try {
				return distinguish(ImageIO.read(url));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	/**
	 * 从输入流处读取Quick Response Code 并解析成字符串返回,若解析失败将返回空字符串
	 * @param is
	 * @return
	 */
	public static String distinguish(InputStream is) {
		if (is!=null) {
			try {
				return distinguish(ImageIO.read(is));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	/**
	 * 从文件处读取Quick Response Code 并解析成字符串返回,若解析失败将返回空字符串
	 * @param path
	 * @return
	 */
	public static String distinguish(Path path) {
        if (Filer.isReadableFile(path)) {
	        try {
				return distinguish(ImageIO.read(path.toFile()));
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return "";
        
    }
	/**
	 * 从BufferedImage处读取Quick Response Code 并解析成字符串返回,若解析失败将返回空字符串
	 * @param image
	 * @return
	 */
	public static String distinguish(BufferedImage image){
		String str = "";
		try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap bitmap = new BinaryBitmap(binarizer);
            Result result = multiFormatReader.decode(bitmap, deHints);
            str = result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return str;
	}
}
