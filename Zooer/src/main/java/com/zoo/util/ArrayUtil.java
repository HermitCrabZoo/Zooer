package com.zoo.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ArrayUtil {
	private ArrayUtil(){}
	
	/**
	 * 将total平均分成len份的数组返回,若total不是len的整数倍,
	 * 那么数组值将尽量平均(数组中元素和仍然等于total).
	 * @param total
	 * @param len 必须大于0
	 * @return
	 */
	public static long[] avgs(long total,int len){
		if (len>0) {
			long[] avg=new long[len];
			long unit=total/len;
			long remain=total%len;
			long one=remain<0?-1:1;
			remain=Math.abs(remain);
			if (total!=0) {
				for(int i=0;i<len;i++){
					avg[i]=unit;
				}
				for(int i=0;i<remain;i++) {
					avg[i]+=one;
				}
			}
			return avg;
		}
		return Types.longs();
	}
	
	/**
	 * 为数组中的每个元素增step
	 * @param array
	 * @param step
	 * @return
	 */
	public static long[] raise(long[] longs,long step){
		if (longs!=null) {
			for(int i=0;i<longs.length;i++){
				longs[i]+=step;
			}
			return longs;
		}
		return Types.longs();
	}
	/**
	 * 为数组中的每个元素增step
	 * @param array
	 * @param step
	 * @return
	 */
	public static int[] raise(int[] ints,int step){
		if (ints!=null) {
			for(int i=0;i<ints.length;i++){
				ints[i]+=step;
			}
			return ints;
		}
		return Types.ints();
	}
	/**
	 * 为数组中的每个元素增step
	 * @param array
	 * @param step
	 * @return
	 */
	public static short[] raise(short[] shorts,short step){
		if (shorts!=null) {
			for(int i=0;i<shorts.length;i++){
				shorts[i]+=step;
			}
			return shorts;
		}
		return Types.shorts();
	}
	/**
	 * 为数组中的每个元素增step
	 * @param array
	 * @param step
	 * @return
	 */
	public static byte[] raise(byte[] bytes,byte step){
		if (bytes!=null) {
			for(int i=0;i<bytes.length;i++){
				bytes[i]+=step;
			}
			return bytes;
		}
		return Types.bytes();
	}
	/**
	 * 为数组中的每个元素增step
	 * @param array
	 * @param step
	 * @return
	 */
	public static double[] raise(double[] doubles,double step){
		if (doubles!=null) {
			for(int i=0;i<doubles.length;i++){
				doubles[i]+=step;
			}
			return doubles;
		}
		return Types.doubles();
	}
	/**
	 * 为数组中的每个元素增step
	 * @param array
	 * @param step
	 * @return
	 */
	public static float[] raise(float[] floats,float step){
		if (floats!=null) {
			for(int i=0;i<floats.length;i++){
				floats[i]+=step;
			}
			return floats;
		}
		return Types.floats();
	}
	/**
	 * 为数组中的每个元素减step
	 * @param array
	 * @param step
	 * @return
	 */
	public static long[] reduce(long[] longs,long step){
		if (longs!=null) {
			for(int i=0;i<longs.length;i++){
				longs[i]-=step;
			}
			return longs;
		}
		return Types.longs();
	}
	/**
	 * 为数组中的每个元素减step
	 * @param array
	 * @param step
	 * @return
	 */
	public static int[] reduce(int[] ints,int step){
		if (ints!=null) {
			for(int i=0;i<ints.length;i++){
				ints[i]-=step;
			}
			return ints;
		}
		return Types.ints();
	}
	/**
	 * 为数组中的每个元素减step
	 * @param array
	 * @param step
	 * @return
	 */
	public static short[] reduce(short[] shorts,short step){
		if (shorts!=null) {
			for(int i=0;i<shorts.length;i++){
				shorts[i]-=step;
			}
			return shorts;
		}
		return Types.shorts();
	}
	/**
	 * 为数组中的每个元素减step
	 * @param array
	 * @param step
	 * @return
	 */
	public static byte[] reduce(byte[] bytes,byte step){
		if (bytes!=null) {
			for(int i=0;i<bytes.length;i++){
				bytes[i]-=step;
			}
			return bytes;
		}
		return Types.bytes();
	}
	/**
	 * 为数组中的每个元素减step
	 * @param array
	 * @param step
	 * @return
	 */
	public static double[] reduce(double[] doubles,double step){
		if (doubles!=null) {
			for(int i=0;i<doubles.length;i++){
				doubles[i]-=step;
			}
			return doubles;
		}
		return doubles;
	}
	/**
	 * 为数组中的每个元素减step
	 * @param array
	 * @param step
	 * @return
	 */
	public static float[] reduce(float[] floats,float step){
		if (floats!=null) {
			for(int i=0;i<floats.length;i++){
				floats[i]-=step;
			}
			return floats;
		}
		return  Types.floats();
	}
	/**
	 * 为数组中的每个元素增step,返回新数组
	 * @param longs
	 * @param step
	 * @return
	 */
	public static long[] raiseNew(long[] longs,long step){
		return Optional.ofNullable(longs).map(ls->Arrays.stream(ls).parallel().map(l->l+step).toArray()).orElse(longs);
	}
	/**
	 * 为数组中的每个元素增step,返回新数组
	 * @param ints
	 * @param step
	 * @return
	 */
	public static int[] raiseNew(int[] ints,int step){
		return Optional.ofNullable(ints).map(is->Arrays.stream(is).parallel().map(i->i+step).toArray()).orElse(ints);
	}
	/**
	 * 为数组中的每个元素减step,返回新数组
	 * @param shorts
	 * @param step
	 * @return
	 */
	public static short[] raiseNew(short[] shorts,short step){
		return Optional.ofNullable(shorts).map(ss->Types.shorts(raiseNew(Types.ints(ss), step))).orElse(Types.shorts());
	}
	/**
	 * 为数组中的每个元素减step,返回新数组
	 * @param bytes
	 * @param step
	 * @return
	 */
	public static byte[] raiseNew(byte[] bytes,byte step){
		return Optional.ofNullable(bytes).map(bs->Types.bytes(raiseNew(Types.ints(bs), step))).orElse(Types.bytes());
	}
	/**
	 * 为数组中的每个元素增step,返回新数组
	 * @param doubles
	 * @param step
	 * @return
	 */
	public static double[] raiseNew(double[] doubles,double step){
		return Optional.ofNullable(doubles).map(ds->Arrays.stream(ds).parallel().map(d->d+step).toArray()).orElse(doubles);
	}
	/**
	 * 为数组中的每个元素增step,返回新数组
	 * @param floats
	 * @param step
	 * @return
	 */
	public static float[] raiseNew(float[] floats,float step){
		return Optional.ofNullable(floats).map(fs->Types.floats(raiseNew(Types.doubles(fs), step))).orElse(Types.floats());
	}
	/**
	 * 为数组中的每个元素减step,返回新数组
	 * @param longs
	 * @param step
	 * @return
	 */
	public static long[] reduceNew(long[] longs,long step){
		return Optional.ofNullable(longs).map(ls->Arrays.stream(ls).parallel().map(l->l-step).toArray()).orElse(longs);
	}
	/**
	 * 为数组中的每个元素减step,返回新数组
	 * @param ints
	 * @param step
	 * @return
	 */
	public static int[] reduceNew(int[] ints,int step){
		return Optional.ofNullable(ints).map(is->Arrays.stream(is).parallel().map(i->i-step).toArray()).orElse(ints);
	}
	/**
	 * 为数组中的每个元素减step,返回新数组
	 * @param shorts
	 * @param step
	 * @return
	 */
	public static short[] reduceNew(short[] shorts,short step){
		return Optional.ofNullable(shorts).map(ss->Types.shorts(reduceNew(Types.ints(ss), step))).orElse(Types.shorts());
	}
	/**
	 * 为数组中的每个元素减step,返回新数组
	 * @param bytes
	 * @param step
	 * @return
	 */
	public static byte[] reduceNew(byte[] bytes,byte step){
		return Optional.ofNullable(bytes).map(bs->Types.bytes(reduceNew(Types.ints(bs), step))).orElse(Types.bytes());
	}
	/**
	 * 为数组中的每个元素减step,返回新数组
	 * @param doubles
	 * @param step
	 * @return
	 */
	public static double[] reduceNew(double[] doubles,double step){
		return Optional.ofNullable(doubles).map(ds->Arrays.stream(ds).parallel().map(d->d-step).toArray()).orElse(doubles);
	}
	/**
	 * 为数组中的每个元素减step,返回新数组
	 * @param floats
	 * @param step
	 * @return
	 */
	public static float[] reduceNew(float[] floats,float step){
		return Optional.ofNullable(floats).map(fs->Types.floats(reduceNew(Types.doubles(fs), step))).orElse(Types.floats());
	}
	
	/**
	 * 去重复
	 * @param longs
	 * @return
	 */
	public static long[] distinct(long[] longs){
		return Optional.ofNullable(longs).map(ls->Arrays.stream(ls).parallel().distinct().toArray()).orElse(Types.longs());
	}
	
	/**
	 * 去重复
	 * @param ints
	 * @return
	 */
	public static int[] distinct(int[] ints){
		return Optional.ofNullable(ints).map(is->Arrays.stream(is).parallel().distinct().toArray()).orElse(Types.ints());
	}
	/**
	 * 去重复
	 * @param shorts
	 * @return
	 */
	public static short[] distinct(short[] shorts){
		return Optional.ofNullable(shorts).map(ss->Types.shorts(distinct(Types.ints(ss)))).orElse(Types.shorts());
	}
	/**
	 * 去重复
	 * @param bytes
	 * @return
	 */
	public static byte[] distinct(byte[] bytes){
		return Optional.ofNullable(bytes).map(bs->Types.bytes(distinct(Types.ints(bs)))).orElse(Types.bytes());
	}
	
	/**
	 * 去重复
	 * @param doubles
	 * @return
	 */
	public static double[] distinct(double[] doubles){
		return Optional.ofNullable(doubles).map(ds->Arrays.stream(ds).parallel().distinct().toArray()).orElse(Types.doubles());
	}
	/**
	 * 去重复
	 * @param floats
	 * @return
	 */
	public static float[] distinct(float[] floats){
		return Optional.ofNullable(floats).map(fs->Types.floats(distinct(Types.doubles(fs)))).orElse(Types.floats());
	}
	
	/**
	 * 去重复
	 * @param strings
	 * @return
	 */
	public static String[] distinct(String[] strings){
		return Optional.ofNullable(strings).map(ds->Arrays.stream(ds).parallel().distinct().toArray(String[]::new)).orElse(Types.strings());
	}
	
	/**
	 * 求平均值
	 * @param longs
	 * @return
	 */
	public static double avg(long[] longs){
		return Optional.ofNullable(longs).map(ls->Arrays.stream(ls).parallel().average().orElse(0.0)).orElse(0.0);
	}
	
	/**
	 * 求平均值
	 * @param ints
	 * @return
	 */
	public static double avg(int[] ints){
		return Optional.ofNullable(ints).map(is->Arrays.stream(is).parallel().average().orElse(0.0)).orElse(0.0);
	}
	/**
	 * 求平均值
	 * @param shorts
	 * @return
	 */
	public static double avg(short[] shorts){
		return Optional.ofNullable(shorts).map(ss->avg(Types.ints(ss))).orElse(0.0);
	}
	/**
	 * 求平均值
	 * @param bytes
	 * @return
	 */
	public static double avg(byte[] bytes){
		return Optional.ofNullable(bytes).map(bs->avg(Types.ints(bs))).orElse(0.0);
	}
	/**
	 * 求平均值
	 * @param doubles
	 * @return
	 */
	public static double avg(double[] doubles){
		return Optional.ofNullable(doubles).map(ds->Arrays.stream(ds).parallel().average().orElse(0.0)).orElse(0.0);
	}
	/**
	 * 求平均值
	 * @param floats
	 * @return
	 */
	public static double avg(float[] floats){
		return Optional.ofNullable(floats).map(fs->avg(Types.doubles(fs))).orElse(0.0);
	}
	
	/**
	 * 求和
	 * @param longs
	 * @return
	 */
	public static long sum(long[] longs){
		return Optional.ofNullable(longs).map(ls->Arrays.stream(ls).parallel().sum()).orElse(0L);
	}
	
	/**
	 * 求和
	 * @param ints
	 * @return
	 */
	public static long sum(int[] ints){
		return Optional.ofNullable(ints).map(is->Arrays.stream(is).parallel().sum()).orElse(0);
	}
	/**
	 * 求和
	 * @param shorts
	 * @return
	 */
	public static long sum(short[] shorts){
		return Optional.ofNullable(shorts).map(ss->sum(Types.ints(ss))).orElse(0L);
	}
	/**
	 * 求和
	 * @param bytes
	 * @return
	 */
	public static long sum(byte[] bytes){
		return Optional.ofNullable(bytes).map(bs->sum(Types.ints(bs))).orElse(0L);
	}
	
	/**
	 * 求和
	 * @param doubles
	 * @return
	 */
	public static double sum(double[] doubles){
		return Optional.ofNullable(doubles).map(ds->Arrays.stream(ds).parallel().sum()).orElse(0.0);
	}
	/**
	 * 求和
	 * @param floats
	 * @return
	 */
	public static double sum(float[] floats){
		return Optional.ofNullable(floats).map(fs->sum(Types.doubles(fs))).orElse(0.0);
	}
	
	/**
	 * 求最大值
	 * @param longs
	 * @return
	 */
	public static long max(long[] longs){
		return Optional.ofNullable(longs).map(ls->Arrays.stream(ls).parallel().max().orElse(0L)).orElse(0L);
	}
	
	/**
	 * 求最大值
	 * @param ints
	 * @return
	 */
	public static int max(int[] ints){
		return Optional.ofNullable(ints).map(is->Arrays.stream(is).parallel().max().orElse(0)).orElse(0);
	}
	/**
	 * 求最大值
	 * @param shorts
	 * @return
	 */
	public static short max(short[] shorts){
		return Optional.ofNullable(shorts).map(ss->(short) max(Types.ints(ss))).orElse((short)0);
	}
	/**
	 * 求最大值
	 * @param bytes
	 * @return
	 */
	public static byte max(byte[] bytes){
		return Optional.ofNullable(bytes).map(bs->(byte) max(Types.ints(bs))).orElse((byte)0);
	}
	/**
	 * 求最大值
	 * @param doubles
	 * @return
	 */
	public static double max(double[] doubles){
		return Optional.ofNullable(doubles).map(ds->Arrays.stream(ds).parallel().max().orElse(0.0)).orElse(0.0);
	}
	/**
	 * 求最大值
	 * @param floats
	 * @return
	 */
	public static float max(float[] floats){
		return Optional.ofNullable(floats).map(fs->(float) max(Types.doubles(fs))).orElse(0.0f);
	}
	
	/**
	 * 求最小值
	 * @param longs
	 * @return
	 */
	public static long min(long[] longs){
		return Optional.ofNullable(longs).map(ls->Arrays.stream(ls).parallel().min().orElse(0L)).orElse(0L);
	}
	
	/**
	 * 求最小值
	 * @param ints
	 * @return
	 */
	public static int min(int[] ints){
		return Optional.ofNullable(ints).map(is->Arrays.stream(is).parallel().min().orElse(0)).orElse(0);
	}
	/**
	 * 求最小值
	 * @param shorts
	 * @return
	 */
	public static short min(short[] shorts){
		return Optional.ofNullable(shorts).map(ss->(short) min(Types.ints(ss))).orElse((short)0);
	}
	/**
	 * 求最小值
	 * @param bytes
	 * @return
	 */
	public static byte min(byte[] bytes){
		return Optional.ofNullable(bytes).map(bs->(byte) min(Types.ints(bs))).orElse((byte)0);
	}
	/**
	 * 求最小值
	 * @param doubles
	 * @return
	 */
	public static double min(double[] doubles){
		return Optional.ofNullable(doubles).map(ds->Arrays.stream(ds).parallel().min().orElse(0.0)).orElse(0.0);
	}
	/**
	 * 求最小值
	 * @param floats
	 * @return
	 */
	public static float min(float[] floats){
		return Optional.ofNullable(floats).map(fs->(float) min(Types.doubles(fs))).orElse(0.0f);
	}
	
	/**
	 * 传入int类型的数组，返回将int数组每个元素用连接符连接起来的字符串
	 * @param separator
	 * @param ints
	 * @return
	 */
	public static String join(String separator,int[] ints){
		return join(separator, toStrings(ints));
	}
	
	/**
	 * 传入long类型的数组，返回将long数组每个元素用连接符连接起来的字符串
	 * @param separator
	 * @param longs
	 * @return
	 */
	public static String join(String separator,long[] longs){
		return join(separator, toStrings(longs));
	}
	
	/**
	 * 传入short类型数组，返回将short数组每个元素用连接符连接起来的字符串
	 * @param separator
	 * @param shorts
	 * @return
	 */
	public static String join(String separator,short[] shorts){
		return join(separator, toStrings(shorts));
	}
	
	/**
	 * 传入byte类型数组，返回将byte数组每个元素用连接符连接起来的字符串
	 * @param separator
	 * @param bytes
	 * @return
	 */
	public static String join(String separator,byte[] bytes){
		return join(separator, toStrings( bytes));
	}
	
	/**
	 * 传入char类型数组，返回将char数组每个元素用连接符连接起来的字符串
	 * @param separator
	 * @param chars
	 * @return
	 */
	public static String join(String separator,char[] chars){
		return join(separator, toStrings(chars));
	}
	
	/**
	 * 传入boolean数组，返回将boolean数组每个元素用连接符连接起来的字符串
	 * @param separator
	 * @param booleans
	 * @return
	 */
	public static String join(String separator,boolean[] booleans){
		return join(separator, toStrings(booleans));
	}
	/**
	 * 传入double数组，返回将double数组每个元素用连接符连接起来的字符串
	 * @param separator
	 * @param doubles
	 * @return
	 */
	public static String join(String separator,double[] doubles){
		return join(separator, toStrings(doubles));
	}
	/**
	 * 传入float数组，返回将float数组每个元素用连接符连接起来的字符串
	 * @param separator
	 * @param floats
	 * @return
	 */
	public static String join(String separator,float[] floats){
		return join(separator, toStrings(floats));
	}
	
	/**
	 * 传入连接符，将字符串数组的每个元素之间用连接符连接起来
	 * @param separator
	 * @param strs
	 * @return
	 */
	public static String join(String separator,String[] strs){
		if (separator==null || strs==null || strs.length<1) {
			return Strings.empty();
		}
		strs=nullToEmpty(strs);
		int len=strs.length;
		StringBuffer stringBuffer=new StringBuffer(len).append(strs[0]);
		for(int i=1;i<len;i++){
			stringBuffer.append(separator).append(strs[i]);
		}
		return stringBuffer.toString();
	}
	/**
	 * 传入T数组，返回将T数组每个元素用连接符连接起来的字符串
	 * @param separator
	 * @param array
	 * @return
	 */
	public static <T>String join(String separator,T[] array){
		return join(separator, toStrings(array));
	}
	/**
	 * 传入T型的List，返回将List每个元素用连接符连接起来的字符串
	 * @param separator
	 * @param list
	 * @return
	 */
	public static <T>String join(String separator,List<T> list){
		if (list==null) {
			return Strings.empty();
		}
		int len=list.size();
		String[] strings=new String[len];
		for(int i=0;i<len;i++){
			strings[i]=String.valueOf(list.get(i));
		}
		return join(separator, strings);
	}
	
	/**
	 * 将obj对象转换成String数组返回,若obj对象为null或者obj对象不是数组类型那么将返回一个空数组(长度为0)
	 * 若传入的为对象类型的数组，此类型未对'toString'方法做任何特定的实现，那么结果可能料想不到。
	 * @param obj 任何类型的数组对象
	 * @return
	 */
	public static String[] toStrings(Object obj){
		if (obj==null || !obj.getClass().isArray()) {
			return new String[]{};
		}
		int len=Array.getLength(obj);
		String[] strs=new String[len];
		for(int i=0;i<len;i++){
			strs[i]=String.valueOf(Array.get(obj, i));
		}
		Optional.ofNullable(obj).map(o->o.getClass().isArray()).orElse(false);
		return strs;
	}
	/**
	 * 获取list中每个元素的field属性的值，并返回，若list中的元素是Map的子类的实例，那么将获取该map的key为field的值。
	 * @param list
	 * @param field
	 * @return
	 */
	public static <T>List<Object> fields(List<T> list,String field){
		List<Object> fields=new ArrayList<Object>();
		if(list==null || field==null){
			return fields;
		}
		for(T t:list){
			if(t==null){
				continue;
			}
			Object value=null;
			if(t instanceof Map){
				Map<?, ?> map=(Map<?, ?>) t;
				value=map.get(field);
			}else{
				for(Class<?> clazz=t.getClass();clazz!=Object.class;clazz=clazz.getSuperclass()) {
					try {
						Field f=clazz.getDeclaredField(field);
						f.setAccessible(true);
						value=f.get(t);
						break;
					} catch (Exception e) {
					}
				}
			}
			if(value!=null){
				fields.add(value);
			}
		}
		return fields;
	}
	/**
	 * 判断elements中的元素是否都在onlys中，如果是返回true(即elements是onlys的可重复元素的子集)，否则elements存在非onlys集合里的元素返回false
	 * @param elements
	 * @param onlys
	 * @return 若elements为null或empty，或onlys为null或empty，则返回false。
	 */
	public static <T>boolean containOnly(List<T> elements,List<T> onlys){
		if(elements!=null && !elements.isEmpty() && onlys!=null && !onlys.isEmpty()) {
			for(T t:elements) {
				if (!onlys.contains(t)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * 判断elements是否包含anys中的任一元素，如果包含返回true(即elements与anys有交集)，否则不包含anys中任一元素返回false
	 * @param elements
	 * @param anys
	 * @return 若elements为null或empty，或anys为null或empty，则返回false。
	 */
	public static <T>boolean containAny(List<T> elements,List<T> anys){
		if(elements!=null && !elements.isEmpty() && anys!=null && !anys.isEmpty()) {
			for(T t:elements) {
				if (anys.contains(t)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 将String型数组中的null值转换成空字符串，返回新生成的String数组
	 * @param strings
	 * @return
	 */
	public static String[] nullToEmpty(String[] strings) {
		return Optional.ofNullable(strings).map(ss->Arrays.stream(ss).parallel().map(s->Strings.nullToEmpty(s)).toArray(String[]::new)).orElse(Strings.emptys());
	}
	
	/**
	 * 将String型字符串中的null去除，返回新生成的String数组
	 * @param strings
	 * @return
	 */
	public static String[] removeNull(String[] strings) {
		return Optional.ofNullable(strings).map(ss->Arrays.stream(ss).parallel().filter(s->Objects.nonNull(s)).toArray(String[]::new)).orElse(Strings.emptys());
	}
	
	/**
	 * 将T型数组转换成List<T>返回
	 * @param array
	 * @return
	 */
	public static <T>List<T> toList(T[] array){
		return Optional.ofNullable(array).map(arr->Arrays.stream(arr).parallel().collect(Collectors.toList())).orElse(Collections.emptyList());
	}
}
