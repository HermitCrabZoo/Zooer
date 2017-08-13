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
        return color!=null&&chroma!=null?chroma.in(getYUV(color)):false;
	}
	/**
	 * 返回颜色深度值
	 */
	public static double getYUV(Color color){
		return Optional.ofNullable(color).map(c->c.getRed()*0.299 + c.getGreen()*0.587 + c.getBlue()*0.114).orElse(0.0);
	}
}
