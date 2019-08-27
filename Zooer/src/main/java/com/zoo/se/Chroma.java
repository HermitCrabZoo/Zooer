package com.zoo.se;

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
	HEAVIEST(0, 1),
	/**
	 * 较深色系
	 */
	HEAVIER(1, 2),
	/**
	 * 深色系
	 */
	HEAVY(2, 3),
	/**
	 * 中等色系
	 */
	MIDDLE(3, 4),
	/**
	 * 轻色系
	 */
	LIGHT(4, 5),
	/**
	 * 较轻色系
	 */
	LIGHTER(5, 6),
	/**
	 * 超轻色系
	 */
	LIGHTEST(6, 7);
	/**
	 * 颜色系梯度
	 */
	private static final double UNIT = 256 / 7.0;
	private final double start;
	private final double end;

	private Chroma(double start, double end) {
		this.start = start * UNIT;
		this.end = end * UNIT;
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
