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

	/**
	 * 判断是否是中文字符
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}
}
