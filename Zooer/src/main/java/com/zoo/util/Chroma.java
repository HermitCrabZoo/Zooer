package com.zoo.util;

/**
 * 色系类,由浅至深共7个色系，YUV范围0-255
 * 
 * @author ZOO
 *
 */
public enum Chroma {
	/**
	 * 超深色系
	 */
	heaviest(0, 1),
	/**
	 * 较深色系
	 */
	heavier(1, 2),
	/**
	 * 深色系
	 */
	heavy(2, 3),
	/**
	 * 中等色系
	 */
	middle(3, 4),
	/**
	 * 轻色系
	 */
	light(4, 5),
	/**
	 * 较轻色系
	 */
	lighter(5, 6),
	/**
	 * 超轻色系
	 */
	lightest(6, 7);
	/**
	 * 颜色系梯度
	 */
	private static final double unit = 256 / 7.0;
	private final double start;
	private final double end;

	private Chroma(double start, double end) {
		this.start = start * unit;
		this.end = end * unit;
	}

	/**
	 * 判断value是否在当前色系内
	 * 
	 * @param value
	 * @return
	 */
	public boolean in(double value) {
		return value >= this.start && value < end;
	}
}
