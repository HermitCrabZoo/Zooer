package com.zoo.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 泛型工具类
 * @author Administrator
 *
 */
public final class Generics {
	private Generics(){}
	/**
	 * 获取list里面元素的类型
	 * @param list
	 * @return 若list为null或没有元素则返回null
	 */
	public static <T>Class<?> getGenericType(List<T> list){
		if (list!=null&&!list.isEmpty()) {
			for(T t:list){
				if (t!=null) {
					return t.getClass();
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取父类泛型的Class
	 * @param clazz
	 * @return Class
	 */
	@SuppressWarnings("rawtypes")
	public static Class getGenericType(Class clazz){
		Type parentType = clazz.getGenericSuperclass();//得到泛型父类  
		Type[] types = ((ParameterizedType) parentType).getActualTypeArguments();
		if (!(types[0] instanceof Class)) {
            return Object.class;
        } 
		return (Class) types[0];
	}
	/**
	 * 获取对象对应的运行时类的简单名称(不含包路径的名称)
	 * @param clazz
	 * @return 类名称
	 */
	@SuppressWarnings("rawtypes")
	public static String getGenericName(Class clazz){
		return clazz.getSimpleName();
	}
}
