package com.zoo.util;

import java.util.ArrayList;
import java.util.List;

public final class Statistics {

	private Statistics() {}
	/**
	 * 算同比或环比，若now小
	 * @param now
	 * @param old
	 * @return
	 */
	public static double yearOnYear(double now,double old){
		return (old<=0?(now<=0?0.0:100.0):(now-old)/old*100.0);
	}
	/**
	 * 从list中取值，产出一个与rules长度相同的包含了值的list。
	 * 规则:从list中找到第一个字段名为equalKey的字段值，与rules中每一个元素对比用equals判断是否相等，若相等，那这个字段值将放入返回结果集中。
	 * @param rules 匹配的值的集合
	 * @param list 需要被匹配的对象集合
	 * @param equalKey 需要被匹配的对象集合中元素的字段名
	 * @param valueKey 被匹配到的元素返回值的字段名
	 * @param defValue 未匹配到元素，用该值来填充。
	 * @return
	 */
	public static <T>List<Object> patch(List<T> rules,List<T> list,Object equalKey,Object valueKey,Object defValue){
		List<Object> data=new ArrayList<>();
		if (rules!=null && list!=null) {
			for(T rule:rules){
				Object val=Arrs.firstValueIf(list, equalKey, rule, valueKey);
				if (val!=null) {
					data.add(val);
				}else {
					data.add(defValue);
				}
			}
		}
		return data;
	}
}
