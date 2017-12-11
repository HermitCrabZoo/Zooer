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
	 * long型数组转成其包装类的数组
	 * @param ls
	 * @return
	 */
	public static Long[] longs(long[] ls) {
		Long[] nls=null;
		if (ls!=null) {
			int len=ls.length;
			nls=new Long[len];
			for(int i=0;i<len;i++) {
				nls[i]=ls[i];
			}
		}else {
			nls=new Long[0];
		}
		return nls;
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
	 * int型数组转成其包装类型数组
	 * @param is
	 * @return
	 */
	public static Integer[] ints(int[] is) {
		Integer[] nis=null;
		if (is!=null) {
			int len=is.length;
			nis=new Integer[len];
			for(int i=0;i<len;i++) {
				nis[i]=is[i];
			}
		}else {
			nis=new Integer[0];
		}
		return nis;
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
	 * short型数组转换成其包装类型数组
	 * @param ss
	 * @return
	 */
	public static Short[] shorts(short[] ss) {
		Short[] nss=null;
		if (ss!=null) {
			int len=ss.length;
			nss=new Short[len];
			for(int i=0;i<len;i++) {
				nss[i]=ss[i];
			}
		}else {
			nss=new Short[0];
		}
		return nss;
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
	 * byte型数组转换成其包装类的数组
	 * @param bs
	 * @return
	 */
	public static Byte[] bytes(byte[] bs) {
		Byte[] nbs=null;
		if (bs!=null) {
			int len=bs.length;
			nbs=new Byte[len];
			for(int i=0;i<len;i++) {
				nbs[i]=bs[i];
			}
		}else {
			nbs=new Byte[0];
		}
		return nbs;
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
	 * double型数组转换成其包装类型的数组
	 * @param ds
	 * @return
	 */
	public static Double[] doubles(double[] ds) {
		Double[] nds=null;
		if (ds!=null) {
			int len=ds.length;
			nds=new Double[len];
			for(int i=0;i<len;i++) {
				nds[i]=ds[i];
			}
		}else {
			nds=new Double[0];
		}
		return nds;
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
	 * float型数组转换成其包装类型数组
	 * @param fs
	 * @return
	 */
	public static Float[] floats(float[] fs) {
		Float[] nfs=null;
		if (fs!=null) {
			int len=fs.length;
			nfs=new Float[len];
			for(int i=0;i<len;i++) {
				nfs[i]=fs[i];
			}
		}else {
			nfs=new Float[0];
		}
		return nfs;
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
	 * char型数组转换成其包装类型的数组
	 * @param cs
	 * @return
	 */
	public static Character[] chars(char[] cs) {
		Character[] ncs=null;
		if (cs!=null) {
			int len=cs.length;
			ncs=new Character[len];
			for(int i=0;i<len;i++) {
				ncs[i]=cs[i];
			}
		}else {
			ncs=new Character[0];
		}
		return ncs;
	}
	
	/**
	 * boolean型数组转换成其包装类型的数组
	 * @param bs
	 * @return
	 */
	public static Boolean[] booleans(boolean[] bs) {
		Boolean[] nbs=null;
		if (bs!=null) {
			int len=bs.length;
			nbs=new Boolean[len];
			for(int i=0;i<len;i++) {
				nbs[i]=bs[i];
			}
		}else {
			nbs=new Boolean[0];
		}
		return nbs;
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
