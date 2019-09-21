package com.zoo.mix;

import java.math.BigDecimal;
import java.util.Objects;

import com.zoo.base.Arrs;

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
	
	/**
	 * 判断整数是否是一个质数
	 * @param x
	 * @return
	 */
	public static boolean isPrime(long x) {
	    long i, s = (long) (Math.sqrt((double) x) + 0.01), step = 4L;
	    if (x == 2L || x == 3L) return true;
	    if (x < 2L || x % 2L == 0L || x % 3L ==0L) return false;
	    for (i = 5L; i <= s; i += step) {  
	        if (x % i == 0L)  
	            return false;  
	        step ^= 6L;  
	    }  
	    return true;  
	}
	
	
	/**
	 * 计算数组的方差:每个元素与所有元素的平均数之差的平方值的平均数
	 * @param arr
	 * @return
	 */
	public static double variance(double[] arr) {
		double c=0.0;
		if (Objects.nonNull(arr)) {
			int len=arr.length;
			double[] squares=new double[len];
			double avg=Arrs.avg(arr);
			for(int i=0;i<len;i++) {
				squares[i]=Math.pow(arr[i]-avg,2);
			}
			c=Arrs.avg(squares);
		}
		
		return c;
	}
	
	
	/**
	 * 计算数组的标准差:方差的算术平方根
	 * @param arr
	 * @return
	 */
	public static double standardDeviation(double[] arr) {
		return Math.sqrt(variance(arr));
	}
	
	
	/**
	 * 限制value在max与min之间,若value大于max则返回max,若value小于min则返回min,否则返回value
	 * @param value
	 * @param max
	 * @param min
	 * @return
	 */
	public static double limit(double value,double max,double min) {
		return Math.max(Math.min(value,max),min);
	}
	
	
	/**
	 * 限制value在max与min之间,若value大于max则返回max,若value小于min则返回min,否则返回value
	 * @param value
	 * @param max
	 * @param min
	 * @return
	 */
	public static float limit(float value,float max,float min) {
		return Math.max(Math.min(value,max),min);
	}
	
	
	/**
	 * 限制value在max与min之间,若value大于max则返回max,若value小于min则返回min,否则返回value
	 * @param value
	 * @param max
	 * @param min
	 * @return
	 */
	public static long limit(long value,long max,long min) {
		return Math.max(Math.min(value,max),min);
	}
	
	
	/**
	 * 限制value在max与min之间,若value大于max则返回max,若value小于min则返回min,否则返回value
	 * @param value
	 * @param max
	 * @param min
	 * @return
	 */
	public static int limit(int value,int max,int min) {
		return Math.max(Math.min(value,max),min);
	}
	
	
	/**
	 * 限制value在max与min之间,若value大于max则返回max,若value小于min则返回min,否则返回value
	 * @param value
	 * @param max
	 * @param min
	 * @return
	 */
	public static short limit(short value,short max,short min) {
		return value<max?value>min?value:min:max;
	}
	
	
	/**
	 * 限制value在max与min之间,若value大于max则返回max,若value小于min则返回min,否则返回value
	 * @param value
	 * @param max
	 * @param min
	 * @return
	 */
	public static byte limit(byte value,byte max,byte min) {
		return value<max?value>min?value:min:max;
	}
	
	
}
