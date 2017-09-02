package com.zoo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;

public final class Beaner {
	private Beaner(){}
	private static Map<String, BeanCopier> copierMap=new HashMap<String, BeanCopier>();
	private static List<String> keys=new ArrayList<String>();
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
	private static BeanCopier copier(Class<?> f,Class<?> t) {
		String key=f.getName()+"->"+t.getName();
		if (keys.contains(key)) {
			return copierMap.get(key);
		}
		BeanCopier copier=BeanCopier.create(f, t, false);
		copierMap.put(key, copier);
		keys.add(key);
		return copier;
	}
}
