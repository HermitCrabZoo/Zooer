package com.zoo.util;

import java.util.Optional;
/**
 * 基本类型工具
 *
 */
public final class Typer {
	private  Typer(){}
	
	/**
	 * 判断obj是否是基本类型的包装类的实例，若是返回true，不是返回false
	 * @param obj
	 * @return 
	 */
	public static boolean isWrap(Object obj) {
		if(obj instanceof Integer){
			return true;
		}else if (obj instanceof Double) {
			return true;
		}else if(obj instanceof Long){
			return true;
		}else if(obj instanceof Float){
			return true;
		}else if(obj instanceof Short){
			return true;
		}else if(obj instanceof Byte){
			return true;
		}else if(obj instanceof Character){
			return true;
		}else if(obj instanceof Boolean){
			return true;
		}
		return false;
	}
	/**
	 * 判断obj是否是String的实例
	 * @param obj
	 * @return
	 */
	public static boolean isStr(Object obj) {
		if (obj instanceof String) {
			return true;
		}
		return false;
	}
	
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
		return Strs.emptys();
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
	/**
	 * int数组转long数组
	 * @param ints
	 * @return
	 */
	public static long[] longs(int[] ints) {
		if(ints!=null && ints.length>0) {
			int len=ints.length;
			long[] longs=new long[len];
			for(int i=0;i<len;i++) {
				longs[i]=ints[i];
			}
			return longs;
		}
		return longs();
	}
	/**
	 * short数组转long数组
	 * @param shorts
	 * @return
	 */
	public static long[] longs(short[] shorts) {
		if(shorts!=null && shorts.length>0) {
			int len=shorts.length;
			long[] longs=new long[len];
			for(int i=0;i<len;i++) {
				longs[i]=shorts[i];
			}
			return longs;
		}
		return longs();
	}
	/**
	 * byte数组转long数组
	 * @param bytes
	 * @return
	 */
	public static long[] longs(byte[] bytes) {
		if(bytes!=null && bytes.length>0) {
			int len=bytes.length;
			long[] longs=new long[len];
			for(int i=0;i<len;i++) {
				longs[i]=bytes[i];
			}
			return longs;
		}
		return longs();
	}
	/**
	 * long数组转int数组
	 * @param longs
	 * @return
	 */
	public static int[] ints(long[] longs) {
		if(longs!=null && longs.length>0) {
			int len=longs.length;
			int[] ints=new int[len];
			for(int i=0;i<len;i++) {
				ints[i]=(int) longs[i];
			}
			return ints;
		}
		return ints();
	}
	/**
	 * short数组转int数组
	 * @param shorts
	 * @return
	 */
	public static int[] ints(short[] shorts) {
		if(shorts!=null && shorts.length>0) {
			int len=shorts.length;
			int[] ints=new int[len];
			for(int i=0;i<len;i++) {
				ints[i]=shorts[i];
			}
			return ints;
		}
		return ints();
	}
	/**
	 * byte数组转int数组
	 * @param bytes
	 * @return
	 */
	public static int[] ints(byte[] bytes) {
		if(bytes!=null && bytes.length>0) {
			int len=bytes.length;
			int[] ints=new int[len];
			for(int i=0;i<len;i++) {
				ints[i]=bytes[i];
			}
			return ints;
		}
		return ints();
	}
	/**
	 * long数组转short数组
	 * @param longs
	 * @return
	 */
	public static short[] shorts(long[] longs) {
		if(longs!=null && longs.length>0) {
			int len=longs.length;
			short[] shorts=new short[len];
			for(int i=0;i<len;i++) {
				shorts[i]=(short) longs[i];
			}
			return shorts;
		}
		return shorts();
	}
	/**
	 * int数组转short数组
	 * @param ints
	 * @return
	 */
	public static short[] shorts(int[] ints) {
		if(ints!=null && ints.length>0) {
			int len=ints.length;
			short[] shorts=new short[len];
			for(int i=0;i<len;i++) {
				shorts[i]=(short) ints[i];
			}
			return shorts;
		}
		return shorts();
	}
	/**
	 * byte数组转short数组
	 * @param bytes
	 * @return
	 */
	public static short[] shorts(byte[] bytes) {
		if(bytes!=null && bytes.length>0) {
			int len=bytes.length;
			short[] shorts=new short[len];
			for(int i=0;i<len;i++) {
				shorts[i]=bytes[i];
			}
			return shorts;
		}
		return shorts();
	}
	/**
	 * long数组转byte数组
	 * @param longs
	 * @return
	 */
	public static byte[] bytes(long[] longs) {
		if(longs!=null && longs.length>0) {
			int len=longs.length;
			byte[] bytes=new byte[len];
			for(int i=0;i<len;i++) {
				bytes[i]=(byte) longs[i];
			}
			return bytes;
		}
		return bytes();
	}
	/**
	 * int数组转byte数组
	 * @param ints
	 * @return
	 */
	public static byte[] bytes(int[] ints) {
		if(ints!=null && ints.length>0) {
			int len=ints.length;
			byte[] bytes=new byte[len];
			for(int i=0;i<len;i++) {
				bytes[i]=(byte) ints[i];
			}
			return bytes;
		}
		return bytes();
	}
	/**
	 * short数组转byte数组
	 * @param shorts
	 * @return
	 */
	public static byte[] bytes(short[] shorts) {
		if(shorts!=null && shorts.length>0) {
			int len=shorts.length;
			byte[] bytes=new byte[len];
			for(int i=0;i<len;i++) {
				bytes[i]=(byte) shorts[i];
			}
			return bytes;
		}
		return bytes();
	}
	/**
	 * float数组转double数组
	 * @param floats
	 * @return
	 */
	public static double[] doubles(float[] floats) {
		if(floats!=null && floats.length>0) {
			int len=floats.length;
			double[] doubles=new double[len];
			for(int i=0;i<len;i++) {
				doubles[i]=floats[i];
			}
			return doubles;
		}
		return doubles();
	}
	/**
	 * double数组转float数组
	 * @param doubles
	 * @return
	 */
	public static float[] floats(double[] doubles) {
		if(doubles!=null && doubles.length>0) {
			int len=doubles.length;
			float[] floats=new float[len];
			for(int i=0;i<len;i++) {
				floats[i]=(float) doubles[i];
			}
			return floats;
		}
		return floats();
	}
	
	/**
	 * 判断传入参数是否都不为null,若是返回true,只要有一个是null就返回false.
	 * @param objs
	 * @return
	 */
	public static boolean notNull(Object...objs) {
		if (objs!=null) {
			for(Object obj:objs) {
				if (obj==null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
