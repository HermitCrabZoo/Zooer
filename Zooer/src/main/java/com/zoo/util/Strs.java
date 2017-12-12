package com.zoo.util;

import java.util.Optional;
import java.util.regex.Pattern;
/**
 * 字符串操作类
 * @author ZOO
 *
 */
public final class Strs {
	private Strs(){}
	
	/**
	 * 只包含数字
	 */
	private static final Pattern PATTERN_NUMERIC = Pattern.compile("\\d+");
	
	/**
	 * 正负整数
	 */
	private static final Pattern PATTERN_INTEGER= Pattern.compile("^[-\\+]?(0{1}|[1-9]+\\d*)");
	
	/**
	 * 正负小数
	 */
	private static final Pattern PATTERN_DOUBLE= Pattern.compile("^[-\\+]?(0{1}|[1-9]+\\d*)\\.{1}\\d+");
	
	/**
	 * 生成随机字符串,由大写字母、小写字母、数字组成
	 * @param len 字符串长度
	 * @return
	 */
	public static String randString(int len){
		StringBuffer stringBuffer=new StringBuffer(10);
		for(int i=0;i<len;i++){
			stringBuffer.append(Chars.randChar());
		}
		return stringBuffer.toString();
	}
	
	/**
	 * 此方法将String对象引用的null值转换成空字符串返回，传入参数不为null则返回原值
	 * @param str
	 * @return
	 */
	public static String nullToEmpty(String str){
		return Optional.ofNullable(str).orElse(empty());
	}
	
	/**
	 * 判断字符串是否非null且非空字符串(不包含任何字符)
	 * @param str
	 * @return
	 */
	public static boolean notEmpty(String str){
		return Optional.ofNullable(str).map(s->!s.isEmpty()).orElse(false);
	}
	
	/**
	 * 判断字符串是否非null且非空字符串(不包含任何字符)且非全空白字符
	 * @param str
	 * @return
	 */
	public static boolean notBlank(String str){
		return Optional.ofNullable(str).map(s->!s.trim().isEmpty()).orElse(false);
	}
	
	/**
	 * 移除最后一个出现的子串
	 * @param str
	 * @param endStr
	 * @return
	 */
	public static String removeEnd(String str,String endStr){
		return Optional.ofNullable(str).map(
				s->Optional.ofNullable(endStr).map(
						es->{
								int i=s.lastIndexOf(es);
								return i>-1?s.substring(0, i)+s.substring(i+es.length()):s;
							}
						).orElse(s)
				).orElse(str);
	}
	
	/**
	 * 移除第一个出现的子串
	 * @param str
	 * @param startStr
	 * @return
	 */
	public static String removeStart(String str,String startStr){
		return Optional.ofNullable(str).map(
				s->Optional.ofNullable(startStr).map(
						ss->{
								int i=s.indexOf(ss);
								return i>-1?s.substring(0, i)+s.substring(i+ss.length()):s;
							}
						).orElse(s)
				).orElse(str);
	}
	
	/**
	 * 返回0个元素的字符串数组
	 * @return
	 */
	public static String[] emptys() {
		return new String[] {};
	}
	
	/**
	 * 返回一个空字符串
	 * @return
	 */
	public static String empty() {
		return "";
	}
	
	/**
	 * 判断字符串长度是否大于len，str为null或长度小于len都会返回false
	 * @param str
	 * @param len
	 * @return
	 */
	public static boolean lenGt(String str,int len) {
		return Optional.ofNullable(str).map(s->s.length()>len).orElse(false);
	}
	
	/**
	 * 判断字符串长度是否小于len，str为null或长度大于len都会返回false
	 * @param str
	 * @param len
	 * @return
	 */
	public static boolean lenLt(String str,int len) {
		return Optional.ofNullable(str).map(s->s.length()<len).orElse(false);
	}
	
	/**
	 * 传入开始索引startIndex和截取的长度len，获取str的子串
	 * @param str 
	 * @param startIndex 开始索引
	 * @param len 截取的长度
	 * @return 若str为null，则返回null。若startIndex大于等于str的长度或startIndex小于0或len小于1，则返回空的(empty)字符串，若从startIndex开始，后面没有len长度的字符串，那么将尽量截取接近len长度的字符串(除了传入str为null外，其他条件下都不会返回null)。
	 */
	public static String sub(String str,int startIndex,int len) {
		return Optional.ofNullable(str).map(
				s->{
					int l=str.length(),ei=startIndex+len;
					return startIndex>=l||startIndex<0||len<1?empty():str.substring(startIndex,(ei>l||ei<startIndex?l:ei));
				}
		).orElse(str);
	}
	
	/**
	 * 传入开始索引startIndex和截取的长度len，获取str的子串，
	 * @param str
	 * @param startIndex
	 * @param len
	 * @param addition
	 * @return 若str为null，则返回null。若startIndex大于等于str的长度或startIndex小于0或len小于1，则返回空的(empty)字符串<br>
	 * 若从startIndex开始，后面没有len长度的字符串，那么将尽量截取接近len长度的字符串(除了传入str为null外，其他条件下都不会返回null)。<br>
	 * 若str的长度大于len，那么返回的字符串是str的子串+addition
	 */
	public static String subIf(String str,int startIndex,int len,String addition) {
		return Optional.ofNullable(str).map(s->{
			String sub=sub(str, startIndex, len);
			return s.length()>len?sub+addition:sub;
		}).orElse(str);
	}
	
	/**
	 * 将对象转换为字符串，若对象为null则返回空(empty)字符串。
	 * @param t
	 * @return
	 */
	public static <T>String toStr(T t) {
		return Optional.ofNullable(t).map(s->String.valueOf(s)).orElse(empty());
	}
	
	/**
	 * str不为null并且以prefix开头则返回true
	 * @param str
	 * @param prefix
	 * @return
	 */
	public static boolean startsWith(String str,String prefix) {
		return Optional.ofNullable(str).map(s->s.startsWith(prefix)).orElse(false);
	}
	
	/**
	 * str不为null并且以suffix结尾则返回true
	 * @param str
	 * @param suffix
	 * @return
	 */
	public static boolean endsWith(String str,String suffix) {
		return Optional.ofNullable(str).map(s->s.endsWith(suffix)).orElse(false);
	}
	
	/**
	 * 字符串分隔，其中任何一个参数为null则返回一个长度为0的数组
	 * @param str
	 * @param regex
	 * @return
	 * @see {@link String#split(String)}
	 */
	public static String[] split(String str,String regex){
		if (str==null || regex==null) {
			return new String[]{};
		}
		return str.split(regex);
	}
	
	/**
	 * 字符串分隔，其中任何一个参数为null则返回一个长度为0的数组
	 * @param str
	 * @param regex
	 * @param limit
	 * @return
	 * @see {@link String#split(String, int)}
	 */
	public static String[] split(String str,String regex,int limit){
		if (str==null || regex==null) {
			return new String[]{};
		}
		return str.split(regex,limit);
	}
	
	/**
	 * str字符串中的target替换成replacement，三个参数若有一个为null则返回传入的str
	 * @param str
	 * @param target
	 * @param replacement
	 * @return
	 * @see {@link String#replace(CharSequence, CharSequence)}
	 */
	public static String replace(String str,CharSequence target,CharSequence replacement) {
		if (str==null || target==null || replacement==null) {
			return str;
		}
		return str.replace(target, replacement);
	}
	
	/**
	 * str字符串中按正则表达式regex替换第一个匹配的为replacement，三个参数若有一个为null则返回传入的str
	 * @param str
	 * @param regex
	 * @param replacement
	 * @return
	 * @see {@link String#replaceFirst(String, String)}
	 */
	public static String replaceFirst(String str,String regex,String replacement) {
		if (str==null || regex==null || replacement==null) {
			return str;
		}
		return str.replaceFirst(regex, replacement);
	}
	
	/**
	 * str字符串中按正则表达式regex替换匹配的为replacement，三个参数若有一个为null则返回传入的str
	 * @param str
	 * @param regex
	 * @param replacement
	 * @return
	 * @see {@link String#replaceAll(String, String)}
	 */
	public static String replaceAll(String str,String regex,String replacement) {
		if (str==null || regex==null || replacement==null) {
			return str;
		}
		return str.replaceAll(regex, replacement);
	}
	
	/**
	 * 判断字符串是否只包含数字，不管是否是合法的整数值。
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		return Optional.ofNullable(str).map(s->PATTERN_NUMERIC.matcher(s).matches()).orElse(false);
	}
	
	/**
	 * 判断字符串是否是合法的整数结构，支持+、-前缀。
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		return Optional.ofNullable(str).map(s->PATTERN_INTEGER.matcher(s).matches()).orElse(false);
	}
	
	/**
	 * 判断字符串是否是一个小数类型的结构，支持+、-前缀
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str) {
		return Optional.ofNullable(str).map(s->PATTERN_DOUBLE.matcher(s).matches()).orElse(false);
	}
	
	/**
	 * 判断是否是中文字符串，包括标点符号
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		return Optional.ofNullable(str).map(s->{
			char[] cs=s.toCharArray();
			int t=0,len=cs.length;
			for (char c : cs) {
				if (Chars.isChinese(c)) {
					t++;
				}
			}
			return len>0 && t==len;
		}).orElse(false);
	}
	
	/**
	 * 是否至少包含一个中文字符
	 * @param str
	 * @return
	 */
	public static boolean hasChinese(String str) {
		return Optional.ofNullable(str).map(s->{
			for (char c : s.toCharArray()) {
				if (Chars.isChinese(c)) {
					return true;
				}
			}
			return false;
		}).orElse(false);
	}
	
}
