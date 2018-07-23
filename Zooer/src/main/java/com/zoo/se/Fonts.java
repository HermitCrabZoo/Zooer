package com.zoo.se;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.util.Random;

import sun.font.FontDesignMetrics;

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
	
	/**
	 * 微软雅黑Light
	 */
	public static final Font YaHeiLight=new Font("微软雅黑Light", Font.PLAIN, 20);
	
	/**
	 * 获取字符串str在字体为font下所占用的宽高像素值.
	 * @param str 不能为空
	 * @param font 不能为空
	 * @return 宽高像素值,若str或font中有一个为null,那么返回的宽高值均为0
	 */
	public static Dimension size(String str,Font font) {
		Dimension dimension=new Dimension();
		if (str!=null && font!=null) {
			FontMetrics fm=FontDesignMetrics.getMetrics(font);
			dimension.setSize(fm.stringWidth(str), fm.getDescent()-fm.getAscent());
		}
		return dimension;
	}
	
	
}
