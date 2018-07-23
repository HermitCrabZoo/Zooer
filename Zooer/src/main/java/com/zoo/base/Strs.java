package com.zoo.base;

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
		return str==null?empty():str;
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
				).orElse(empty());
	}
	
	/**
	 * 移除第一个出现的子串
	 * @param str
	 * @param startStr
	 * @return 若str为null则返回空字符串
	 */
	public static String removeStart(String str,String startStr){
		return Optional.ofNullable(str).map(
				s->Optional.ofNullable(startStr).map(
						ss->{
								int i=s.indexOf(ss);
								return i>-1?s.substring(0, i)+s.substring(i+ss.length()):s;
							}
						).orElse(s)
				).orElse(empty());
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
	 * @return 若startIndex大于等于str的长度或startIndex小于0或len小于1，则返回空的(empty)字符串，若从startIndex开始，后面没有len长度的字符串，那么将尽量截取接近len长度的字符串(不会返回null)。
	 */
	public static String sub(String str,int startIndex,int len) {
		return Optional.ofNullable(str).map(
				s->{
					int l=str.length(),ei=startIndex+len;
					return startIndex>=l||startIndex<0||len<1?empty():str.substring(startIndex,(ei>l||ei<startIndex?l:ei));
				}
		).orElse(empty());
	}
	
	/**
	 * 传入开始索引startIndex和截取的长度len，获取str的子串，
	 * @param str
	 * @param startIndex
	 * @param len
	 * @param addition
	 * @return 若startIndex大于等于str的长度或startIndex小于0或len小于1，则返回空的(empty)字符串<br>
	 * 若从startIndex开始，后面没有len长度的字符串，那么将尽量截取接近len长度的字符串(不会返回null)。<br>
	 * 若str的长度大于len，那么返回的字符串是str的子串+addition
	 */
	public static String subIf(String str,int startIndex,int len,String addition) {
		return Optional.ofNullable(str).map(s->{
			String sub=sub(str, startIndex, len);
			return s.length()>len?sub+addition:sub;
		}).orElse(empty());
	}
	
	/**
	 * "正向查找",找到str中第一个出现在start与end之间的子串,start与end之间无先后顺序
	 * @param str
	 * @param start
	 * @param end
	 * @return 若未找到或str、start、end中任一为null,则返回空字符串
	 */
	public static String subBetween(String str,String start,String end) {
		return subBetween(str, start, 1, end, 1);
	}
	
	
	/**
	 * "正向查找",从str中找到第startTimes次出现的start子串,与str中找到第endTimes次出现的end子串之间的子串,start与end之间无先后顺序
	 * @param str
	 * @param start
	 * @param startTimes 从1开始
	 * @param end
	 * @param endTimes 从1开始
	 * @return 若未找到或str、start、end中任一为null,或startTimes、endTimes中任一小于1,则返回空字符串
	 */
	public static String subBetween(String str,String start,int startTimes,String end,int endTimes) {
		if (Typer.notNull(str,start,end)) {
			int si=indexOfTimes(str, start, startTimes);
			int ei=indexOfTimes(str, end, endTimes);
			int offset=si<ei?start.length():end.length();
			if (si != ei && si !=-1 && ei != -1 && Math.abs(ei-si)>offset) {
				return str.substring(Math.min(si, ei)+offset, Math.max(si, ei));
			}
		}
		
		return empty();
	}
	
	/**
	 * "反向查找",找到str中第一个出现在start与end之间的子串,start与end之间无先后顺序
	 * @param str
	 * @param start
	 * @param end
	 * @return 若未找到或str、start、end中任一为null,则返回空字符串
	 */
	public static String lastSubBetween(String str,String start,String end) {
		return lastSubBetween(str, start, 1, end, 1);
	}
	
	
	/**
	 * "反向查找",从str中找到第startTimes次出现的start子串,与str中找到第endTimes次出现的end子串之间的子串,start与end之间无先后顺序
	 * @param str
	 * @param start
	 * @param startTimes 从1开始
	 * @param end
	 * @param endTimes 从1开始
	 * @return 若未找到或str、start、end中任一为null,或startTimes、endTimes中任一小于1,则返回空字符串
	 */
	public static String lastSubBetween(String str,String start,int startTimes,String end,int endTimes) {
		if (Typer.notNull(str,start,end)) {
			int si=lastIndexOfTimes(str, start, startTimes);
			int ei=lastIndexOfTimes(str, end, endTimes);
			int offset=si<ei?start.length():end.length();
			if (si != ei && si !=-1 && ei != -1 && Math.abs(ei-si)>offset) {
				return str.substring(Math.min(si, ei)+offset, Math.max(si, ei));
			}
		}
		
		return empty();
	}
	
	
	/**
	 * 正向查找substr在str中第times次出现的索引,若未找到,或str、substr为null,或times小于1,则返回-1
	 * @param str
	 * @param substr
	 * @param times
	 * @return
	 */
	public static int indexOfTimes(String str,String substr,int times) {
		if (Typer.notNull(str,substr)) {
			int index=-1,i = -1,t=0;
	        while (t<times && (i = str.indexOf(substr, i + 1)) != -1) {
	        	index=i;
	        	t++;
	        }
	        return t==times?index:-1;
		}
		return -1;
	}
	
	
	
	/**
	 * 从末尾反向查找substr在str中第times次出现的索引,若未找到,或str、substr为null,或times小于1,则返回-1
	 * @param str
	 * @param substr
	 * @param times
	 * @return
	 */
	public static int lastIndexOfTimes(String str,String substr,int times) {
		if (Typer.notNull(str,substr)) {
			int i=indexOfTimes(new String(Arrs.reverse(str.toCharArray(), false)), new String(Arrs.reverse(substr.toCharArray(), false)), times);
			if (i!=-1) {
				return str.length()-i-substr.length();
			}
		}
		return -1;
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
		if (Typer.notNull(str,regex)) {
			return str.split(regex);
		}
		return emptys();
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
		if (Typer.notNull(str,regex)) {
			return str.split(regex,limit);
		}
		return emptys();
	}
	
	/**
	 * str字符串中的target替换成replacement
	 * @param str
	 * @param target
	 * @param replacement
	 * @return 三个参数若有一个为null则返回传入的str(若此参数不为null,否则返回空字符串)
	 * @see {@link String#replace(CharSequence, CharSequence)}
	 */
	public static String replace(String str,CharSequence target,CharSequence replacement) {
		if (Typer.notNull(str,target,replacement)) {
			return str.replace(target, replacement);
		}
		return firstNotNull(str);
	}
	
	/**
	 * str字符串中按正则表达式regex替换第一个匹配的为replacement
	 * @param str
	 * @param regex
	 * @param replacement
	 * @return 三个参数若有一个为null则返回传入的str(若此参数不为null,否则返回空字符串)
	 * @see {@link String#replaceFirst(String, String)}
	 */
	public static String replaceFirst(String str,String regex,String replacement) {
		if (Typer.notNull(str,regex,replacement)) {
			return str.replaceFirst(regex, replacement);
		}
		return firstNotNull(str);
	}
	
	/**
	 * str字符串中按正则表达式regex替换匹配的为replacement
	 * @param str
	 * @param regex
	 * @param replacement
	 * @return 三个参数若有一个为null则返回传入的str(若此参数不为null,否则返回空字符串)
	 * @see {@link String#replaceAll(String, String)}
	 */
	public static String replaceAll(String str,String regex,String replacement) {
		if (Typer.notNull(str,regex,replacement)) {
			return str.replaceAll(regex, replacement);
		}
		return firstNotNull(str);
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
	 * 是否至少包含一个中文字符，包括中文标点符号
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
	
	/**
	 * 判断是否仅仅是一个中文字符串，不包含中文标点符号，若有中文标点符号或者其他非中文字符，那么返回false
	 * @param str
	 * @return
	 */
	public static boolean isChineseOnly(String str) {
		return Optional.ofNullable(str).map(s->PATTERN_CHINESE_ONLY.matcher(s).matches()).orElse(false);
	}
	
	/**
	 * 判断是否包含中文字符，不计入中文标点符号，即就算有中文标点符号但没有中文文字，那么也返回false
	 * @param str
	 * @return
	 */
	public static boolean hasChineseOnly(String str) {
		return Optional.ofNullable(str).map(s->PATTERN_CHINESE.matcher(s).matches()).orElse(false);
	}
	
	/**
	 * 获取字符串的长度
	 * @param str
	 * @return 若传入参数str为null,则返回0
	 */
	public static int len(String str) {
		return str==null?0:str.length();
	}
	
	/**
	 * 字符串左对齐,用fillchar从右边补足字符串长度到len.
	 * @param str
	 * @param len
	 * @param fillchar
	 * @return 若传入参数str为null则返回空字符串
	 */
	public static String ljust(String str,int len,char fillchar) {
		int l=len-len(str);
		if (Typer.notNull(str,fillchar) && l>0) {
			StringBuilder sb=new StringBuilder(str);
			for(int i=0;i<l;i++) {
				sb.append(fillchar);
			}
			str=sb.toString();
		}
		return firstNotNull(str);
	}
	
	/**
	 * 字符串右对齐,用fillchar从左边补足字符串长度到len.
	 * @param str
	 * @param len
	 * @param fillchar
	 * @return 若传入参数str为null则返回空字符串
	 */
	public static String rjust(String str,int len,char fillchar) {
		int l=len-len(str);
		if (Typer.notNull(str,fillchar) && l>0) {
			StringBuilder sb=new StringBuilder();
			for(int i=0;i<l;i++) {
				sb.append(fillchar);
			}
			sb.append(str);
			str=sb.toString();
			
		}
		return firstNotNull(str);
	}
	
	
	/**
	 * 返回strs中第一个不为null的字符串,若strs为null,或里面的元素都为null,则返回空字符串
	 * @param strs
	 * @return
	 */
	public static String firstNotNull(String...strs) {
		if (strs!=null) {
			for (String s : strs) {
				if (s!=null) {
					return s;
				}
			}
		}
		return empty();
	}
	
}
