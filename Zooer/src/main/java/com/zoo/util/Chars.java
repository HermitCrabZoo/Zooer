package com.zoo.util;

public final class Chars {
	private Chars() {
	}

	/**
	 * 生成1个随机字符，可能是大写字母、小写字母、数字
	 * 
	 * @return
	 */
	public static String randChar() {
		switch ((int) Math.round(Math.random() * 2)) {
		case 1:
			return String.valueOf((char) Math.round(Math.random() * 25 + 65));
		case 2:
			return String.valueOf((char) Math.round(Math.random() * 25 + 97));
		default:
			return String.valueOf(Math.round(Math.random() * 9));
		}
	}
}
