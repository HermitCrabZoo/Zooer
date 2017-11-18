package com.zoo.util;

import java.math.BigDecimal;

public final class Maths {
	private Maths(){}
	/**
	 * 获取传入值的位数
	 * @param num
	 * @return
	 */
	public static int digit(long num){
		return String.valueOf(num).length();
	}
	/**
	 * 获取传入值的位数
	 * @param num
	 * @return
	 */
	public static int digit(double num){
		return new BigDecimal(num).toPlainString().split(".")[0].length();
	}
	
	/**
	 * 通过数量级获取该数量级最大的值,
	 * 传入值不能超过18，否则将返回long型的最大值
	 * @param digit
	 * @return
	 */
	public static long maxByDigit(int digit){
		if (digit>18) {
			return Long.MAX_VALUE;
		}
		long max=1L;
		while (digit(max)<=digit) {
			max*=10L;
		}
		return max-1L;
	}
	/**
	 * 判断是否是正数
	 * @param num
	 * @return
	 */
	public static boolean isPositive(long num){
		return num>0;
	}
	
	
	/**
	 * 返回0与传入值之间整数的连加和
	 * @param max
	 * @return
	 */
	public static long sigma(long max){
		boolean isPositive=isPositive(max);
		max=Math.abs(max);
		long sum=0;
		for (long i = 0L; i <=max; i++) {
			sum+=i;
		}
		return isPositive?sum:-sum;
	}
	/**
	 * 返回0(不包含)与传入值之间整数的阶乘值
	 * @param max
	 * @return
	 */
	public static long factorial(long max){
		if (max==0L) {
			return 0L;
		}
		boolean isPositive=isPositive(max);
		max=Math.abs(max);
		long sum=1L;
		for (long i = 1L; i <=max; i++) {
			sum*=i;
		}
		return isPositive?sum:-sum;
	}
	
}
