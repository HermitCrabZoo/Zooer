package com.zoo.code;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;

import com.google.zxing.common.BitMatrix;
import com.zoo.se.Imgs;

public final class MatrixBridge {
	private MatrixBridge() {
	}

	private static final int BLACK = 0xFF000000;
	
	private static final int WHITE = 0xFFFFFFFF;

	/**
	 * 矩阵转换为BufferedImage
	 * @param matrix
	 * @return
	 */
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		return toBufferedImage(matrix, Color.BLACK, Color.WHITE);
	}
	
	/**
	 * 矩阵转换为BufferedImage,并指定前景和背景色
	 * @param matrix
	 * @param foreColor 前景色
	 * @param backColor 背景色
	 * @return
	 */
	public static BufferedImage toBufferedImage(BitMatrix matrix,Color foreColor,Color backColor) {
		if (Objects.isNull(matrix)) {
			return null;
		}
		int fc=foreColor==null?BLACK:foreColor.getRGB();
		int bc=backColor==null?WHITE:backColor.getRGB();
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? fc : bc);
			}
		}
		return image;
	}
	
	
	/**
	 * 矩阵转换为BufferedImage,并指定前景图和背景图
	 * @param matrix
	 * @param foreImage 前景图
	 * @param backImage 背景图
	 * @return
	 */
	public static BufferedImage toBufferedImage(BitMatrix matrix,BufferedImage foreImage,BufferedImage backImage) {
		if (Objects.isNull(matrix)) {
			return null;
		}
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		boolean hasFore=foreImage!=null;
		boolean hasBack=backImage!=null;
		//宽高与矩阵不同则调整为相同的
		if (hasFore && (foreImage.getWidth()!=width || foreImage.getHeight()!=height)) {
			foreImage=Imgs.of(foreImage).cutLeftTop(width, height).abgr().get();
		}
		if (hasBack && (backImage.getWidth()!=width || backImage.getHeight()!=height)) {
			backImage=Imgs.of(backImage).cutLeftTop(width, height).abgr().get();
		}
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? (hasFore?foreImage.getRGB(x, y):BLACK) : (hasBack?backImage.getRGB(x, y):WHITE));
			}
		}
		return image;
	}
}
