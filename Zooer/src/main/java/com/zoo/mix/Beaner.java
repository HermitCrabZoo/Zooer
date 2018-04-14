package com.zoo.mix;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zoo.base.Strs;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;

/**
 * bean操作类,线程安全
 * @author ZOO
 *
 */
public final class Beaner {
	
	private Beaner(){}
	
	/**
	 * 缓存BeanCopier对象
	 */
	private static final Map<String, BeanCopier> COPIER_MAP=new HashMap<String, BeanCopier>();
	
	/**
	 * 缓存key
	 */
	private static final List<String> KEYS=new ArrayList<String>();
	
	/**
	 * 将多个JavaBean转换到多个Map输出
	 * @param beans
	 * @return
	 */
	public static <T>List<Map<String, Object>> toMaps(List<T> beans){
		return toMaps(beans,null);
	}
	/**
	 * 将多个JavaBean转换到多个Map输出
	 * @param beans
	 * @param excludeKeys 不包含的属性
	 * @return
	 */
	public static <T>List<Map<String, Object>> toMaps(List<T> beans,List<String> excludeKeys){
		List<Map<String, Object>> maps=new ArrayList<Map<String,Object>>();
		if (beans!=null) {
			for(T bean:beans) {
				maps.add(toMap(bean, excludeKeys));
			}
		}
		return maps;
	}
	/**
	 * 将JavaBean转换到Map输出
	 * @param bean
	 * @return
	 */
	public static <T>Map<String, Object> toMap(T bean){
		return toMap(bean, null);
	}
	
	/**
	 * 将JavaBean转换到Map输出
	 * @param bean
	 * @param excludeKeys 不包含的属性
	 * @return
	 */
	public static <T>Map<String, Object> toMap(T bean,List<String> excludeKeys){
		Map<String,Object> map=new HashMap<String, Object>();
		if (bean!=null) {
			BeanMap beanMap=BeanMap.create(bean);
			if (excludeKeys!=null) {
				put(map, beanMap, excludeKeys);
			}else {
				put(map, beanMap);
			}
		}
		return map;
	}
	
	private static void put(Map<String,Object> map,BeanMap beanMap,List<String> excludeKeys) {
		for(Object key:beanMap.keySet()){
			if (!excludeKeys.contains(key)) {
				map.put(String.valueOf(key), beanMap.get(key));
			}
		}
	}
	
	private static void put(Map<String,Object> map,BeanMap beanMap) {
		for(Object key:beanMap.keySet()){
			map.put(String.valueOf(key), beanMap.get(key));
		}
	}

	/**
	 * 将多个Map转换到多个JavaBean输出
	 * @param maps
	 * @param beans
	 * @return
	 */
	public static <T>List<T> toBeans(List<Map<String, Object>> maps,List<T> beans){
		return toBeans(maps, beans, null);
	}
	
	/**
	 * 将多个Map转换到多个JavaBean输出
	 * @param maps
	 * @param beans
	 * @param excludeKeys 不包含的属性
	 * @return
	 */
	public static <T>List<T> toBeans(List<Map<String, Object>> maps,List<T> beans,List<String> excludeKeys){
		if(maps!=null&&beans!=null) {
			for(int i=0,msize=maps.size(),bsize=beans.size(); i<msize&&i<bsize; i++) {
				toBean(maps.get(i), beans.get(i), excludeKeys);
			}
		}
		return beans;
	}
	
	/**
	 * 将Map转换到JavaBean输出
	 * @param map
	 * @param bean
	 * @return
	 */
	public static <T>T toBean(Map<String, Object> map,T bean){
		return toBean(map, bean, null);
	}
	
	/**
	 * 将Map转换到JavaBean输出
	 * @param map
	 * @param bean
	 * @param excludeKeys 不包含的属性
	 * @return
	 */
	public static <T>T toBean(Map<String, Object> map,T bean,List<String> excludeKeys){
		if (map!=null && bean!=null) {
			BeanMap beanMap=BeanMap.create(bean);
			if (excludeKeys==null || excludeKeys.isEmpty()) {
				beanMap.putAll(map);
			}else{
				for(String key:map.keySet()){
					if (!excludeKeys.contains(key)) {
						beanMap.put(key, map.get(key));
					}
				}
			}
		}
		return bean;
	}
	
	/**
	 * 将f的属性拷贝到t中,同名和同类型的属性才会,t中的setter方法需大于f中的getter方法,否则将无法创建拷贝.
	 * @param fs
	 * @param ts
	 * @return
	 */
	public static <T>List<T> copy(List<T> fs,List<T> ts){
		if (fs!=null&&ts!=null) {
			for(int i=0,fsize=fs.size(),tsize=ts.size(); i<fsize&&i<tsize; i++) {
				copy(fs.get(i), ts.get(i));
			}
		}
		return ts;
	}
	
	/**
	 * 将f的属性拷贝到t中,同名和同类型的属性才会,t中的setter方法需大于f中的getter方法,否则将无法创建拷贝.
	 * @param f
	 * @param t
	 * @return
	 */
	public static <T>T copy(T f,T t){
		if (f!=null && t!=null) {
			copier(f.getClass(), t.getClass()).copy(f, t, null);
		}
		return t;
	}
	
	/**
	 * 获取属性拷贝对象,以提升批量拷贝时的性能
	 * @param f
	 * @param t
	 * @return
	 */
	private static synchronized BeanCopier copier(Class<?> f,Class<?> t) {
		String key=f.getName()+"->"+t.getName();
		if (KEYS.contains(key)) {
			return COPIER_MAP.get(key);
		}
		BeanCopier copier=BeanCopier.create(f, t, false);
		COPIER_MAP.put(key, copier);
		KEYS.add(key);
		return copier;
	}
	
	/**
	 * 获取t的field字段值，若t是Map子类的实例:那么field将作为t的key来获取对应的value。
	 * @param t
	 * @param field
	 * @return value(未获取到则为null)
	 */
	public static <T>Object value(T t,Object field) {
		Object value=null;
		String fstr=field+"";
		if (t!=null) {
			if(t instanceof Map){
				Map<?, ?> map=(Map<?, ?>) t;
				value=map.get(field);
			}else{
				for(Class<?> clazz=t.getClass();clazz!=Object.class;clazz=clazz.getSuperclass()) {
					try {
						Field f=clazz.getDeclaredField(fstr);
						f.setAccessible(true);
						value=f.get(t);
						break;
					} catch (Exception e) {
					}
				}
			}
		}
		return value;
	}
	
	/**
	 * 获取list中每个元素的field字段的值，并返回值的list
	 * @param list
	 * @param field
	 * @return
	 */
	public static <T>List<Object> values(List<T> list,Object field){
		List<Object> vals=new ArrayList<>();
		if (list!=null) {
			for (T t : list) {
				Object val=value(t, field);
				if (val!=null) {
					vals.add(val);
				}
			}
		}
		return vals;
	}
	
	/**
	 * 判断t对象以及t对象的fields属性是否null，若其中有一个为null则返回false，若t对象以及其fields属性对应的值都不为null，则返回true
	 * @param t
	 * @param fields t对象的属性数组，若t是Map或其子类的实例，那fields作为t对象的key的数组来对待
	 * @return
	 */
	public static <T>boolean notNull(T t,Object...fields){
		if (t==null) {
			return false;
		}
		if(fields!=null) {
			for(Object field:fields) {
				if (value(t, field)==null) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 判断t对象以及t对象的fields属性是否不是null且不是空字符串，若其中有一个为null或空字符串则返回false，若t对象以及其fields属性对应的值都不为null且不为空字符串，则返回true，
	 * @param t
	 * @param fields t对象的属性数组，若t是Map或其子类的实例，那fields作为t对象的key的数组来对待
	 * @return
	 */
	public static <T>boolean notEmpty(T t,Object...fields){
		if (t==null||Strs.empty().equals(t)) {
			return false;
		}
		if(fields!=null) {
			for(Object field:fields) {
				Object value=value(t, field);
				if (value==null||Strs.empty().equals(value)) {
					return false;
				}
			}
		}
		return true;
	}
}
