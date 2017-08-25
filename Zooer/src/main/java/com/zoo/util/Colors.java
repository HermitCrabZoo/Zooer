package com.zoo.util;

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
        return color!=null&&chroma!=null?chroma.in(getYuv(color)):false;
	}
	/**
	 * 返回颜色的YUV值的封装对象
	 */
	public static Yuv getYuv(Color color){
		Yuv yuv=new Yuv();
		return Optional.ofNullable(color).map(c->{
			yuv.setY(c.getRed()*0.299 + c.getGreen()*0.587 + c.getBlue()*0.114);
			yuv.setU(c.getRed()*-0.147 - c.getGreen()*0.289 + c.getBlue()*0.436);
			yuv.setV(c.getRed()*0.615 - c.getGreen()*0.515 - c.getBlue()*0.100);
			return yuv;
		}).orElse(yuv);
	}
	/**
	 * 将颜色的YUV值转换为RGB值储存到Color对象中,并返回Color对象
	 * @param yuv
	 * @return
	 */
	public static Color getColor(Yuv yuv) {
		return new Color((float) (yuv.getY()+yuv.getV()*1.140),(float) (yuv.getY()- yuv.getU()*0.394 - yuv.getV()*0.581),(float) (yuv.getY() + yuv.getU()*2.032));
	}
}
