package com.zoo.se;

import java.awt.Color;
import java.util.Optional;
import java.util.Random;

public final class Colors {
	
	private Colors(){}
	
	private static Random random=new Random();
	
	/**
	 * 白透明
	 */
	public static Color wTransparent=new Color(255, 255, 255, 0);
	/**
	 * 黑透明
	 */
	public static Color bTransparent=new Color(0,0,0,0);
	
	
	/**
	 * 生成一种随机颜色
	 * @return
	 */
	public static Color randColor()
	{
		return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}
	
	
	/**
	 * 随机生成给定色系中的一种颜色
	 * @return
	 */
	public static Color randColor(Chroma chroma){
		Color color=randColor();
		if (chroma==null) {
			return color;
		}
		while (!isChroma(color, chroma)) {
			color=randColor();
		}
		return color;
	}
	
	/**
	 * 判断给定颜色是否是给定色系
	 * @param color
	 * @return
	 */
	public static boolean isChroma(Color color,Chroma chroma){
        return color!=null&&chroma!=null?chroma.in(getYuv(color).getY()):false;
	}
	
	
	/**
	 * 返回颜色的YUV值的封装对象
	 */
	public static Yuv getYuv(Color color){
		Yuv yuv=new Yuv();
		return Optional.ofNullable(color).map(c->{
			yuv.setY(c.getRed()*0.299 + c.getGreen()*0.587 + c.getBlue()*0.114);//Y
			yuv.setU(c.getRed()*-0.147 - c.getGreen()*0.289 + c.getBlue()*0.436);//U
			yuv.setV(c.getRed()*0.615 - c.getGreen()*0.515 - c.getBlue()*0.100);//V
			return yuv;
		}).orElse(yuv);
	}
	
	
	/**
	 * 返回颜色的YCbCr值的封装对象
	 */
	public static YCbCr getYCbCr(Color color){
		YCbCr yCbCr=new YCbCr();
		return Optional.ofNullable(color).map(c->{
			yCbCr.setY(c.getRed()*0.257 + c.getGreen()*0.504 + c.getBlue()*0.098 + 16);//Y
			yCbCr.setCb(c.getRed()*-0.148 - c.getGreen()*0.291 + c.getBlue()*0.439 + 128);//Cb
			yCbCr.setCr(c.getRed()*0.439 - c.getGreen()*0.368 - c.getBlue()*0.071 + 128);//Cr
			return yCbCr;
		}).orElse(yCbCr);
	}
	
	
	/**
	 * 将颜色的YUV值转换为RGB值储存到Color对象中,并返回Color对象
	 * @param yuv
	 * @return
	 */
	public static Color getColor(Yuv yuv) {
		return new Color((int) (yuv.getY()+yuv.getV()*1.140),(int) (yuv.getY()- yuv.getU()*0.394 - yuv.getV()*0.581),(int) (yuv.getY() + yuv.getU()*2.032));
	}
	
	
	/**
	 * 将颜色的YCbCr值转换为RGB值储存到Color对象中,并返回Color对象
	 * @param yCbCr
	 * @return
	 */
	public static Color getColor(YCbCr yCbCr) {
		return new Color((int) (1.164*(yCbCr.getY()-16) + 1.596*(yCbCr.getCr()-128)),
				(int) (1.164*(yCbCr.getY()-16) - 0.392*(yCbCr.getCb()-128) - 0.813*(yCbCr.getCr()-128)),
				(int) (1.164*(yCbCr.getY()-16) + 2.017*(yCbCr.getCb()-128)));
	}
	
	/**
	 * 获取Alpha、Red、Green、Blue各分量的值
	 * @param argb
	 * @return
	 */
	public static int[] argb(int argb) {
		int[] v=new int[4];
		v[0] = (argb & 0xff000000) >>> 24;
		v[1] = (argb & 0xff0000) >> 16;
		v[2] = (argb & 0xff00) >> 8;
		v[3] = (argb & 0xff);
		return v;
	}
	
	/**
	 * 获取Blue、Green、Red、Alpha各分量的值
	 * @param argb
	 * @return
	 */
	public static int[] bgra(int argb) {
		int[] v=new int[4];
		v[3] = (argb & 0xff000000) >>> 24;
		v[2] = (argb & 0xff0000) >> 16;
		v[1] = (argb & 0xff00) >> 8;
		v[0] = (argb & 0xff);
		return v;
	}
	
}
