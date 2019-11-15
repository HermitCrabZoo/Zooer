package com.zoo.base;

import java.util.regex.Pattern;
/**
 * 字符串操作类
 * @author ZOO
 *
 */
public final class Strs {
	private Strs(){}

	/**
	 * 空字符串常量
	 */
	public static final String EMPTY = "";
	
	/**
	 * 只包含数字
	 */
	private static final Pattern PATTERN_NUMERIC = Pattern.compile("\\d+");
	
	/**
	 * 正负整数
	 */
	private static final Pattern PATTERN_INTEGER= Pattern.compile("^[-+]?(0|[1-9]+\\d*)");
	
	/**
	 * 正负小数
	 */
	private static final Pattern PATTERN_DOUBLE= Pattern.compile("^[-+]?(0|[1-9]+\\d*)\\.\\d+");
	
	/**
	 * 仅仅是中文,不包含标点符号
	 */
	private static final Pattern PATTERN_CHINESE_ONLY= Pattern.compile("^[\u4E00-\u9FBF]+$");
	
	/**
	 * 包含中文,不计入中文标点符号
	 */
	private static final Pattern PATTERN_CHINESE= Pattern.compile(".*[\u4e00-\u9fa5]+.*");
	
	/**
	 * 生成随机字符串,由大写字母、小写字母、数字组成
	 * @param len 字符串长度
	 * @return 长度为len的随机字符串
	 */
	public static String randString(int len){
		StringBuilder sb = new StringBuilder(Math.max(0, len));
		for (int i = 0; i < len; i++) {
			sb.append(Chars.randChar());
		}
		return sb.toString();
	}
	
	/**
	 * 此方法将String对象引用的null值转换成空字符串返回，传入参数不为null则返回原值
	 * @param str 需要转换的字符串
	 * @return 若str不为null则返回原值。
	 */
	public static String nullToEmpty(String str) {
		return str == null ? EMPTY : str;
	}
	
	/**
	 * 判断字符串是否非null且非空字符串(不包含任何字符)
	 * @param str 字符串
	 * @return null、empty则false
	 */
	public static boolean notEmpty(String str){
		return str != null && !str.isEmpty();
	}
	
	/**
	 * 判断字符串是否非null且非空字符串(不包含任何字符)且非全空白字符
	 * @param str 字符串
	 * @return null、empty、blank则false
	 */
	public static boolean notBlank(String str){
		return str != null && !str.isBlank();
	}

	/**
	 * 移除最后一个出现的子串
	 *
	 * @param str 原始字符串
	 * @param endStr 要被移除的子串
	 * @return 若str和endStr为null或empty则返回str
	 */
	public static String removeEnd(String str, String endStr) {
		if (!notEmpty(str) || !notEmpty(endStr)) {
			return str;
		}
		int i = str.lastIndexOf(endStr);
		return i > -1 ? str.substring(0, i) + str.substring(i + endStr.length()) : str;
	}

	/**
	 * 移除第一个出现的子串
	 *
	 * @param str 原始字符串
	 * @param startStr 要被移除的子串
	 * @return 若str和startStr为null或empty则返回str
	 */
	public static String removeStart(String str, String startStr) {
		if (!notEmpty(str) || !notEmpty(startStr)) {
			return str;
		}
		int i = str.indexOf(startStr);
		return i > -1 ? str.substring(0, i) + str.substring(i + startStr.length()) : str;
	}
	
	/**
	 * 返回0个元素的字符串数组
	 * @return 长度为0空字符串数组
	 */
	public static String[] empties() {
		return new String[] {};
	}
	
	/**
	 * 返回一个空字符串
	 * @return 长度为0的空字符串
	 */
	public static String empty() {
		return EMPTY;
	}

	/**
	 * 判断字符串长度是否大于len，str为null或长度小于len都会返回false
	 *
	 * @param str 要判断的字符串
	 * @param len 比较的长度
	 * @return str.length() > len
	 */
	public static boolean lenGt(String str, int len) {
		return str != null && str.length() > len;
	}
	
	/**
	 * 判断字符串长度是否小于len，str为null或长度大于len都会返回false
	 * @param str 要判断的字符串
	 * @param len 比较的长度
	 * @return str.length() < len
	 */
	public static boolean lenLt(String str, int len) {
		return str != null && str.length() < len;
	}
	
	/**
	 * 传入开始索引startIndex和截取的长度len，获取str的子串
	 * @param str 原始字符串
	 * @param startIndex 开始索引(包含)
	 * @param len 截取的长度
	 * @return 若startIndex大于等于str的长度或startIndex小于0或len小于1，则返回空的(empty)字符串，若从startIndex开始，后面没有len长度的字符串，那么将尽量截取接近len长度的字符串(不会返回null)。
	 */
	public static String sub(String str,int startIndex,int len) {
		if (str == null || startIndex < 0 || startIndex >= str.length() || len < 1) {
			return EMPTY;
		}
		return str.substring(startIndex, Math.min(startIndex + len, str.length()));
	}
	
	/**
	 * 传入开始索引startIndex和截取的长度len，获取str的子串，若str的长度大于len，则返回子串+addition否则只返回子串。
	 * @param str 原始字符串
	 * @param startIndex 开始索引(包含)
	 * @param len 需要判断的长度临界值
	 * @param addition 当截取的子串大于len时附加在子串后面的字符串
	 * @return 若startIndex大于等于str的长度或startIndex小于0或len小于1，则返回空的(empty)字符串<br>
	 * 若从startIndex开始，后面没有len长度的字符串，那么将尽量截取接近len长度的字符串(不会返回null)。
	 */
	public static String subIf(String str, int startIndex, int len, String addition) {
		if (str != null) {
			String sub = sub(str, startIndex, len);
			return str.length() > len ? sub + addition : sub;
		}
		return EMPTY;
	}
	
	/**
	 * "正向查找",找到str中第一个出现在start与end之间的子串,start与end之间无先后顺序
	 * @param str 原始字符串
	 * @param start 第一个出现的子串
	 * @param end 第二个出现的子串
	 * @return 若未找到或str、start、end中任一为null,则返回空字符串
	 */
	public static String subBetween(String str, String start, String end) {
		return subBetween(str, start, 1, end, 1);
	}
	
	
	/**
	 * "正向查找",从str中找到第startTimes次出现的start子串,与str中找到第endTimes次出现的end子串之间的子串,start与end之间无先后顺序
	 * @param str 原始字符串
	 * @param start 第一个出现的字符串
	 * @param startTimes 从1开始，表示第几次出现的start子串
	 * @param end 第二个出现的字符串
	 * @param endTimes 从1开始，表示第几次出现的end子串
	 * @return 若未找到或str、start、end中任一为null,或startTimes、endTimes中任一小于1,则返回空字符串
	 */
	public static String subBetween(String str, String start, int startTimes, String end, int endTimes) {
		if (str!=null && start!=null && end!=null) {
			int si = indexOfTimes(str, start, startTimes);
			int ei = indexOfTimes(str, end, endTimes);
			int offset = si < ei ? start.length() : end.length();
			if (si != ei && si != -1 && ei != -1 && Math.abs(ei - si) > offset) {
				return str.substring(Math.min(si, ei) + offset, Math.max(si, ei));
			}
		}
		return EMPTY;
	}
	
	/**
	 * "反向查找",找到str中第一个出现在start与end之间的子串,start与end之间无先后顺序
	 * @param str 原始字符串
	 * @param start 第一个出现的子串
	 * @param end 第二个出现的子串
	 * @return 若未找到或str、start、end中任一为null,则返回空字符串
	 */
	public static String lastSubBetween(String str,String start,String end) {
		return lastSubBetween(str, start, 1, end, 1);
	}
	
	
	/**
	 * "反向查找",从str中找到第startTimes次出现的start子串,与str中找到第endTimes次出现的end子串之间的子串,start与end之间无先后顺序
	 * @param str 原始字符串
	 * @param start 第一个出现的字符串
	 * @param startTimes 从1开始，表示第几次出现的start子串
	 * @param end 第二个出现的字符串
	 * @param endTimes 从1开始，表示第几次出现的end子串
	 * @return 若未找到或str、start、end中任一为null,或startTimes、endTimes中任一小于1,则返回空字符串
	 */
	public static String lastSubBetween(String str, String start, int startTimes, String end, int endTimes) {
		if (str!=null && start!=null && end!=null) {
			int si = lastIndexOfTimes(str, start, startTimes);
			int ei = lastIndexOfTimes(str, end, endTimes);
			int offset = si < ei ? start.length() : end.length();
			if (si != ei && si != -1 && ei != -1 && Math.abs(ei - si) > offset) {
				return str.substring(Math.min(si, ei) + offset, Math.max(si, ei));
			}
		}
		return EMPTY;
	}
	
	
	/**
	 * 正向查找substr在str中第times次出现的起始索引
	 * @param str 原始字符串
	 * @param substr 要查找的子串
	 * @param times 从1开始，表示第几次出现的substr子串
	 * @return 返回substr子串在str中第times次出现的起始索引，若未找到或str、substr为null或times小于0则返回-1
	 */
	public static int indexOfTimes(String str, String substr, int times) {
		if (str != null && substr != null) {
			int index = -1, i = -1, t = 0;
			while (t < times && (i = str.indexOf(substr, i + 1)) != -1) {
				index = i;
				t++;
			}
			return t == times ? index : -1;
		}
		return -1;
	}
	
	
	
	/**
	 * 从末尾反向查找substr在str中第times次出现的起始索引
	 * @param str 原始字符串
	 * @param substr 要查找的子串
	 * @param times 从1开始，表示第几次出现的substr子串
	 * @return 返回substr子串在str中第times次出现的起始索引，若未找到或str、substr为null或times小于0则返回-1
	 */
	public static int lastIndexOfTimes(String str, String substr, int times) {
		if (str != null && substr != null) {
			int index = -1, i = str.length() - 1, t = 0;
			while (t < times && (i = str.lastIndexOf(substr, i - 1)) != -1) {
				index = i;
				t++;
			}
			return t == times ? index : -1;
		}
		return -1;
	}


	/**
	 * 将对象转换为字符串
	 * @param t 转为字符串的对象
	 * @return 当t为null时返回空字符串，否则返回t.toString()
	 */
	public static <T> String toStr(T t) {
		return t == null ? EMPTY : String.valueOf(t);
	}
	
	/**
	 *  判断str是否是以prefix开头
	 * @param str 要被判断的字符串
	 * @param prefix 前缀
	 * @return str、prefix不为null并且以prefix开头则返回true，否则返回false
	 */
	public static boolean startsWith(String str, String prefix) {
		return str != null && prefix != null && str.startsWith(prefix);
	}
	
	/**
	 * 判断str是否是以suffix结尾
	 * @param str 要被判断的字符串
	 * @param suffix 后缀
	 * @return str、suffix不为null并且以suffix结尾则返回true
	 */
	public static boolean endsWith(String str, String suffix) {
		return str != null && suffix != null && str.endsWith(suffix);
	}
	
	/**
	 * 字符串分隔
	 * @param str 要被分割的原始字符串
	 * @param regex 匹配分割符的正则
	 * @return str或regex为null则返回一个长度为0的数组
	 * @see String#split(String)
	 */
	public static String[] split(String str, String regex) {
		if (str!= null && regex!= null) {
			return str.split(regex);
		}
		return empties();
	}
	
	/**
	 * 字符串分隔
	 * @param str 要被分割的原始字符串
	 * @param regex 匹配分割符的正则
	 * @param limit 限制分割的次数
	 * @return str或regex为null则返回一个长度为0的数组
	 * @see String#split(String, int)
	 */
	public static String[] split(String str, String regex, int limit) {
		if (str!= null && regex!= null) {
			return str.split(regex, limit);
		}
		return empties();
	}
	
	/**
	 * 替换字符串中出现的每一个子串为指定的字符串
	 * @param str 原始字符串
	 * @param target 要被替换的子串
	 * @param replacement 替换的字符串
	 * @return 三个参数若有一个为null则返回str，若str为null则返回空字符串
	 * @see String#replace(CharSequence, CharSequence)
	 */
	public static String replace(String str, CharSequence target, CharSequence replacement) {
		if (str!= null && target!= null && replacement!= null) {
			return str.replace(target, replacement);
		}
		return str == null ? EMPTY : str;
	}
	
	/**
	 * 替换字符串中第一个被正则匹配的子串为指定的字符串
	 * @param str 原始字符串
	 * @param regex 要被替换的子串匹配的正则
	 * @param replacement 替换的字符串
	 * @return 三个参数若有一个为null则返回str，若str为null则返回空字符串
	 * @see String#replaceFirst(String, String)
	 */
	public static String replaceFirst(String str, String regex, String replacement) {
		if (str!= null && regex!= null && replacement!= null) {
			return str.replaceFirst(regex, replacement);
		}
		return str == null ? EMPTY : str;
	}
	
	/**
	 * 替换字符串中每一个被正则匹配的子串为指定的字符串
	 * @param str 原始字符串
	 * @param regex 要被替换的子串匹配的正则
	 * @param replacement 替换的字符串
	 * @return 三个参数若有一个为null则返回str，若str为null则返回空字符串
	 * @see String#replaceAll(String, String)
	 */
	public static String replaceAll(String str, String regex, String replacement) {
		if (str!= null && regex!= null && replacement!= null) {
			return str.replaceAll(regex, replacement);
		}
		return str == null ? EMPTY : str;
	}
	
	/**
	 * 判断字符串是否只包含数字，不管是否是合法的整数值。
	 * @param str 要被判断的字符串
	 * @return 若所有字符都是数字则返回true
	 */
	public static boolean isNumeric(String str) {
		return str != null && PATTERN_NUMERIC.matcher(str).matches();
	}
	
	/**
	 * 判断字符串是否是合法的整数结构，支持+、-前缀。
	 * @param str 要被判断的字符串
	 * @return 若字符串是合法的整数表示形式则返回true
	 */
	public static boolean isInteger(String str) {
		return str != null && PATTERN_INTEGER.matcher(str).matches();
	}
	
	/**
	 * 判断字符串是否是一个小数类型的结构，支持+、-前缀
	 * @param str 要被判断的字符串
	 * @return 若字符串是合法的小树结构则返回true
	 */
	public static boolean isDouble(String str) {
		return str != null && PATTERN_DOUBLE.matcher(str).matches();
	}
	
	/**
	 * 判断是否是中文字符串，包括标点符号
	 * @param str 要被判断的字符串
	 * @return 若只包含中文字符，则返回true
	 */
	public static boolean isChinese(String str) {
		if (str != null) {
			for (char c : str.toCharArray()) {
				if (!Chars.isChinese(c)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 是否至少包含一个中文字符，包括中文标点符号
	 * @param str 要被判断的字符串
	 * @return 若str至少包含一个中文字符则返回true
	 */
	public static boolean hasChinese(String str) {
		if (str != null) {
			for (char c : str.toCharArray()) {
				if (Chars.isChinese(c)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断是否仅仅是一个中文字符串，不包含中文标点符号，若有中文标点符号或者其他非中文字符，那么返回false
	 * @param str 要被判断的字符串
	 * @return 若包含中英文标点符号或者包含非中文字符则返回false
	 */
	public static boolean isChineseOnly(String str) {
		return str != null && PATTERN_CHINESE_ONLY.matcher(str).matches();
	}
	
	/**
	 * 判断是否包含中文字符，不计入中文标点符号，即就算有中文标点符号但没有中文文字，那么也返回false
	 * @param str 要被判断的字符串
	 * @return 不包含中文字符(中文标点符号不算)则返回false
	 */
	public static boolean hasChineseOnly(String str) {
		return str != null && PATTERN_CHINESE.matcher(str).matches();
	}
	
	/**
	 * 获取字符串的长度
	 * @param str 要获取长度的字符串
	 * @return 若传入参数str为null,则返回0
	 */
	public static int len(String str) {
		return str == null ? 0 : str.length();
	}
	
	/**
	 * 字符串左对齐,用fillChar从右边补足字符串长度到len.
	 * @param str 原始字符串
	 * @param len 长度阈值，str小于该长度则需要填充fillChar
	 * @param fillChar 填充的字符
	 * @return 若传入参数str为null则返回空字符串
	 */
	public static String ljust(String str, int len, char fillChar) {
		if (str == null) {
			return EMPTY;
		}
		int repetition = len - str.length();
		if (repetition > 0) {
			return str + String.valueOf(fillChar).repeat(repetition);
		}
		return str;
	}
	
	/**
	 * 字符串右对齐,用fillChar从左边补足字符串长度到len.
	 * @param str 原始字符串
	 * @param len 长度阈值，str小于该长度则需要填充fillChar
	 * @param fillChar 填充的字符
	 * @return 若传入参数str为null则返回空字符串
	 */
	public static String rjust(String str, int len, char fillChar) {
		if (str == null) {
			return EMPTY;
		}
		int repetition = len - str.length();
		if (repetition > 0) {
			return String.valueOf(fillChar).repeat(repetition) + str;
		}
		return str;
	}
	
	
	/**
	 * 返回strs中第一个不为null的字符串
	 * @param strs 要判断的字符串数组
	 * @return 返回第一个非null字符串，若没有则返回空字符串
	 */
	public static String firstNotNull(String... strs) {
		if (strs != null) {
			for (String s : strs) {
				if (s != null) {
					return s;
				}
			}
		}
		return EMPTY;
	}
	
}
