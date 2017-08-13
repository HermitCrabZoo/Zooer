package com.zoo.util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Random;

public final class Fonts {
	private Fonts(){}
	private static Random random=new Random();
	/**
	 * 随机生成一个系统支持的字体,颜色字号默认
	 * @return
	 */
	public static Font randFont() {
		return fontNames!=null&&fontNames.length>0?new Font(fontNames[random.nextInt(fontNames.length)], naive.getStyle(), naive.getSize()):naive;
	}
	/**
	 * 系统支持的字体
	 */
	public static final String[] fontNames=GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	//public static final Font defaultFont=(Font) UIManager.getDefaults().values().stream().filter(v->v instanceof Font).findFirst().get();
	/**
	 * 不支持中文
	 */
	public static final Font consolas=new Font("Consolas", Font.PLAIN, 20);
	/**
	 * 幼圆(默认)
	 */
	public static final Font naive=new Font("幼圆", Font.PLAIN, 20);
}
