package com.zoo.code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.zoo.base.Arrs;
import com.zoo.cons.Images;
import com.zoo.se.Chroma;
import com.zoo.se.Colors;
import com.zoo.se.Fonts;
import com.zoo.se.Imgs;

/**
 *验证码生成类
 * @author Administrator
 *
 */
public final class ImgCode {
	private ImgCode(){}
	/**
	 * 生成验证码图像写入到文件，并返回图像
	 * @param width
	 * @param height
	 * @param surface
	 * @param path
	 * @return
	 * @throws IOException 当输出失败时
	 */
	public static BufferedImage imgCodeToPath(int width,int height,String surface,Path path) throws IOException{
		BufferedImage image=imgCode(width, height, surface);
		if (path!=null) {
			if(!ImageIO.write(image, Images.JPEG, path.toFile())){
				throw new IOException("Could not write an image of format JPEG to "+path);
			}
		}
		return image;
	}
	/**
	 * 生成验证码图像输出到输出流，并返回图像
	 * @param width
	 * @param height
	 * @param surface
	 * @param ops
	 * @return
	 * @throws IOException 当输出失败时
	 */
	public static BufferedImage imgCodeOutput(int width,int height,String surface,OutputStream ops) throws IOException{
		BufferedImage image=imgCode(width, height, surface);
		if(ImageIO.write(image, Images.JPEG, ops)){
			return image;
		}else{
			throw new IOException("Could not write an image of format JPEG to "+ops);
		}
	}
	
	/**
	 * 生成验证码图片,默认字体超轻色系,背景深色系
	 * @param width 图片宽
	 * @param height 图片高
	 * @param surface 验证码
	 * @return
	 */
	public static BufferedImage imgCode(int width,int height,String surface){
		return imgCode(width, height, surface, Chroma.lightest, Chroma.heavy,null);
	}
	
	
	/**
	 * 生成验证码图片,默认字体超轻色系,背景深色系
	 * @param dimension 图片大小
	 * @param surface 验证码
	 * @return
	 */
	public static BufferedImage imgCode(Dimension dimension,String surface){
		return imgCode(dimension.width,dimension.height, surface, Chroma.lightest, Chroma.heavy,null);
	}
	/**
	 * 生成验证码图片,默认超轻色系,背景深色系
	 * @param dimension 图片大小
	 * @param surface 验证码
	 * @param font 字体
	 * @return
	 */
	public static BufferedImage imgCode(Dimension dimension,String surface,Font font){
		return imgCode(dimension.width, dimension.height, surface, Chroma.lightest, Chroma.heavy,font);
	}
	/**
	 * 生成验证码图片,默认超轻色系,背景深色系
	 * @param width 图片宽
	 * @param height 图片高
	 * @param surface 验证码
	 * @param font 字体
	 * @return
	 */
	public static BufferedImage imgCode(int width,int height,String surface,Font font){
		return imgCode(width, height, surface, Chroma.lightest, Chroma.heavy,font);
	}
	/**
	 * 生成验证码图片,使用默认字体
	 * @param dimension 图片大小
	 * @param surface 验证码
	 * @param bgChroma 图片背景色系
	 * @param fgChroma 验证码色系
	 * @return
	 */
	public static BufferedImage imgCode(Dimension dimension,String surface,Chroma bgChroma,Chroma fgChroma){
		return imgCode(dimension.width, dimension.height, surface, bgChroma, fgChroma, null);
	}
	/**
	 * 生成验证码图片,使用默认字体
	 * @param w 图片宽
	 * @param h 图片高
	 * @param surface 验证码
	 * @param bgChroma 图片背景色系
	 * @param fgChroma 验证码色系
	 * @return
	 */
	public static BufferedImage imgCode(int w,int h,String surface,Chroma bgChroma,Chroma fgChroma){
		return imgCode(w, h, surface, bgChroma, fgChroma, null);
	}
	/**
	 * 生成验证码图片
	 * @param dimension 图片大小
	 * @param surface 验证码
	 * @param bgChroma 图片背景色系
	 * @param fgChroma 验证码色系
	 * @param font 验证码字体
	 * @return
	 */
	public static BufferedImage imgCode(Dimension dimension,String surface,Chroma bgChroma,Chroma fgChroma,Font font) {
		return imgCode(dimension.width, dimension.height, surface, bgChroma, fgChroma, font);
	}
	/**
	 * 生成验证码图片
	 * @param w 图片宽
	 * @param h 图片高
	 * @param surface 验证码
	 * @param bgChroma 图片背景色系
	 * @param fgChroma 验证码色系
	 * @param font 验证码字体
	 * @return
	 */
	public static BufferedImage imgCode(int w,int h,String surface,Chroma bgChroma,Chroma fgChroma,Font font){
		Imgs imgs=Imgs.ofNew(w,h, Colors.randColor(bgChroma));
		BufferedImage image=imgs.get();
		if (surface!=null && surface.length()>0) {
			font=Optional.ofNullable(font).orElse(image.getGraphics().getFont());
			Dimension dimen=Fonts.size(surface, font);
			int[] units=xInWidth(dimen.width, surface.length(), image.getWidth());
			int x=0,y=(image.getHeight()-dimen.height)/2;
			for(int i=0;i<units.length;i++){
				x+=units[i];
				imgs.pile(surface.charAt(i)+"",x,y, Colors.randColor(fgChroma), font);
			}
		}
		return image;
	}
	/**
	 * 生成验证码图片,使用默认字体
	 * @param dimension 图片大小
	 * @param surface 验证码
	 * @param bgColor 图片背景色
	 * @param fgColor 验证码颜色
	 * @return
	 */
	public static BufferedImage imgCode(Dimension dimension,String surface,Color bgColor,Color fgColor){
		return imgCode(dimension.width,dimension.height, surface, bgColor, fgColor, null);
	}
	/**
	 * 生成验证码图片,使用默认字体
	 * @param w 图片宽
	 * @param h 图片高
	 * @param surface 验证码
	 * @param bgColor 图片背景色
	 * @param fgColor 验证码颜色
	 * @return
	 */
	public static BufferedImage imgCode(int w,int h,String surface,Color bgColor,Color fgColor){
		return imgCode(w, h, surface, bgColor, fgColor, null);
	}
	/**
	 * 生成验证码图片
	 * @param dimension 图片大小
	 * @param surface 验证码
	 * @param bgColor 图片背景色
	 * @param fgColor 验证码颜色
	 * @param font 验证码字体
	 * @return
	 */
	public static BufferedImage imgCode(Dimension dimension,String surface,Color bgColor,Color fgColor,Font font){
		return imgCode(dimension.width,dimension.height, surface , bgColor, fgColor, font);
	}
	/**
	 * 生成验证码图片
	 * @param w 图片宽
	 * @param h 图片高
	 * @param surface 验证码
	 * @param bgColor 图片背景色
	 * @param fgColor 验证码颜色
	 * @param font 验证码字体
	 * @return
	 */
	public static BufferedImage imgCode(int w,int h,String surface,Color bgColor,Color fgColor,Font font){
		Imgs imgs=Imgs.ofNew(w,h, bgColor);
		BufferedImage image=imgs.get();
		if (surface!=null && surface.length()>0) {
			font=Optional.ofNullable(font).orElse(image.getGraphics().getFont());
			Dimension dimen=Fonts.size(surface, font);
			int[] units=xInWidth(dimen.width, surface.length(), image.getWidth());
			int x=0,y=(image.getHeight()-dimen.height)/2;
			for(int i=0;i<units.length;i++){
				x+=units[i];
				imgs.pile(surface.charAt(i)+"",x,y, fgColor, font);
			}
		}
		return image;
	}
	/**
	 * 将width为x轴的长度,尝试让长度和为total,各元素长度相等的一组长度为len的元素数组中的每个元素在长度为width的x轴内尽量优雅的站立,最终返回各个元素在长度为width的x轴中的横坐标.
	 * 优雅的定义:各元素间的距离为单个元素本身的长度,元素x轴坐标在width范围内.
	 * @param total
	 * @param len
	 * @param width
	 * @return
	 */
	private static int[] xInWidth(int total,int len,int width){
		int any=total/len;
		int[] units=new int[len];
		if (width>total*2) {
			units=Arrs.avgs(total*2,len);
			units[0]=(width-total*2)/2+any/2;
		}else if(width>total){
			units=Arrs.avgs(width, len);
			units[0]=(units[0]-any)/2;
		}else {
			if (len>1) {
				System.arraycopy(Arrs.raise(Arrs.avgs(width-total, len-1), any), 0, units, 1, len-1);
			}
		}
		return units;
	}
}
