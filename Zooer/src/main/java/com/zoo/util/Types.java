package com.zoo.util;

import java.util.Optional;
/**
 * 基本类型工具
 *
 */
public final class Types {
	private  Types(){}
	/**
	 * 验证字符串的有效性(不为null并且不为空字符串)
	 * @param s 验证字符串
	 * @return 是否有效的布尔值
	 */
	public static boolean isString(String s){
		return Optional.ofNullable(s).map(i->i.length()>0).orElse(false);
	}
	/**
	 * 验证浮点对象的有效性(不为null并且大于0)
	 * @param f 浮点对象
	 * @return 是否有效的布尔值
	 */
	public static boolean isFloat(Float f){
		return Optional.ofNullable(f).map(i->i>0).orElse(false);
	}
	/**
	 * 验证双精度浮点对象的有效性(不为null并且大于0)
	 * @param d 浮点对象
	 * @return 是否有效的布尔值
	 */
	public static boolean isDouble(Double d){
		return Optional.ofNullable(d).map(i->i>0).orElse(false);
	}
	/**
	 * 验证整数对象的有效性(不为null并且大于0)
	 * @param i 整数对象
	 * @return 是否有效的布尔值
	 */
	public static boolean isInteger(Integer i){
		return Optional.ofNullable(i).map(ii->ii>0).orElse(false);
	}
	/**
	 * 验证长整数对象的有效性(不为null并且大于0)
	 * @param l 长整数对象
	 * @return 是否有效的布尔值
	 */
	public static boolean isLong(Long l){
		return Optional.ofNullable(l).map(i->i>0).orElse(false);
	}
	/**
	 * 验证短整数对象的有效性(不为null并且大于0)
	 * @param l 短整数对象
	 * @return 是否有效的布尔值
	 */
	public static boolean isShort(Short s){
		return Optional.ofNullable(s).map(i->i>0).orElse(false);
	}
	/**
	 * 验证字节对象的有效性(不为null并且大于0)
	 * @param l 字节对象
	 * @return 是否有效的布尔值
	 */
	public static boolean isByte(Byte b){
		return Optional.ofNullable(b).map(i->i>0).orElse(false);
	}
	/**
	 * 0个元素的字符串数组
	 * @return
	 */
	public static String[] strings() {
		return Strings.emptys();
	}
	/**
	 * 0个元素的单精度浮点数组
	 * @return
	 */
	public static float[] floats() {
		return new float[] {};
	}
	/**
	 * 0个元素的双精度浮点数组
	 * @return
	 */
	public static double[] doubles() {
		return new double[] {};
	}
	/**
	 * 0个元素的整型数组
	 * @return
	 */
	public static int[] ints() {
		return new int[] {};
	}
	/**
	 * 0个元素的长整型数组
	 * @return
	 */
	public static long[] longs() {
		return new long[] {};
	}
	/**
	 * 0个元素的短整型数组
	 * @return
	 */
	public static short[] shorts() {
		return new short[] {};
	}
	/**
	 * 0个元素的字节数组
	 * @return
	 */
	public static byte[] bytes() {
		return new byte[] {};
	}
	/**
	 * 0个元素的字符数组
	 * @return
	 */
	public static char[] chars() {
		return new char[] {};
	}
	/**
	 * 0个元素的布尔型数组
	 * @return
	 */
	public static boolean[] booleans() {
		return new boolean[] {};
	}
}
