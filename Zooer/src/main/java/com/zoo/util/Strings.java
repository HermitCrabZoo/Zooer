package com.zoo.util;

import java.util.Optional;
/**
 * 字符串操作类
 * @author ZOO
 *
 */
public final class Strings {
	private Strings(){}
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
}
