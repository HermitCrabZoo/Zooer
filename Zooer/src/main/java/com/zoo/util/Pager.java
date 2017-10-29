package com.zoo.util;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页排序工具
 *
 */
public final class Pager<T> {
	
	public static final String DESC="DESC";
	public static final String ASC="ASC";
	private static final Comparator<Object> CHINESE = Collator.getInstance(java.util.Locale.CHINA);
	/**
	 * 当前对象关联的list
	 */
	private List<T> list=null;
	private Pager(){}
	private Pager(List<T> list) {
		this.list=list;
	}
	/**
	 * 构造一个 Pager<T>对象
	 * @param list
	 * @return
	 */
	public static <T>Pager<T> of(List<T> list){
		if (list==null) {
			throw new NullPointerException("list 不能为空！");
		}
		return new Pager<T>(list);
	}
	/**
	 * 获取当前关联的对象
	 * @return
	 */
	public List<T> get(){
		return list;
	}
	/**
	 * 设置当前对象关联的list
	 * @param list
	 * @return
	 */
	public Pager<T> setNew(List<T> list){
		if (list==null) {
			throw new NullPointerException("list 不能为空！");
		}
		this.list=list;
		return this;
	}
	/**
	 * 通过sortField字段降序排序
	 * @param sortField
	 * @return
	 */
	public Pager<T> desc(String sortField){
		return sort(DESC, sortField);
	}
	/**
	 * 通过sortField字段升序排序
	 * @param sortField
	 * @return
	 */
	public Pager<T> asc(String sortField){
		return sort(ASC, sortField);
	}
	/**
	 * 按照sortField和sortType来对sorts进行排序
	 * @param sortType asc为升序，desc为降序，默认asc。
	 * @param sortField 当前list中的元素是String类型时，此字段可为空，非String类型的时候排序字段不能为空，若T代表的是Map对象，那么sortField代表Map的key，否则sortField作为T的属性
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Pager<T> sort(final String sortType,final String sortField){
		final boolean isRise=sortDefAsc(sortType).equals(ASC);
		final boolean isStr=(Generics.getGenericType(list) == String.class);
		if (isStr || sortField!=null) {//只有元素类型是字符串，的时候sortFiled才可以为null
			Collections.sort(list, new Comparator() {
				public int compare(Object a, Object b) {
					if (a==null || b==null) {
						return 1;
					}
					if (isStr) {
						a +="";
						b +="";
					}else if (a instanceof Map) {
						a=((Map)a).get(sortField);
						b=((Map)b).get(sortField);
					}else{
						try {
							a=Beaner.value(a,sortField);
							b=Beaner.value(b,sortField);
						} catch (Exception e) {
							return 1;
						}
					}
					if (a instanceof Number && b instanceof Number) {
						Number one=(Number) a,two=(Number) b;
						return isRise?compareNumber(one, two):compareNumber(two, one);
					}else{
						String one = a+"";
						String two = b+"";
						return isRise?CHINESE.compare(one, two):CHINESE.compare(two,one);
					}
				}
			});
		}
		return this;
	}
	/**
	 * 限制当前关联list的长度，start小于0或size小于1或start大于等于当前关联list的元素个数，那么该方法将不对当前关联的list做截取操作
	 * @param start
	 * @param size
	 */
	public Pager<T> limit(int start,int size){
		if(start>0 && size>1 && start<list.size()){
			int s=list.size();
			int end=start+size;
			list=list.subList(start, end>s?s:end);
		}
		return this;
	}
	/**
	 * 通过by字段(若元素是Map则by为key)分组
	 * @param by
	 */
	public Map<Object,List<T>> group(String by){
		Map<Object,List<T>> oneToMore=new HashMap<Object,List<T>>();
		for(T t:list){
			Object key=Beaner.value(t, by);
			List<T> us=oneToMore.containsKey(key)?oneToMore.get(key):new ArrayList<T>();
			us.add(t);
			oneToMore.put(key, us);
		}
		return oneToMore;
	}
	
	/**
	 * one大于two返回1，one小于two返回-1，one等于two返回0
	 * @param one
	 * @param two
	 * @return
	 */
	private static int compareNumber(Number one,Number two){
		double d1=one.doubleValue(),d2=two.doubleValue();
		if(d1>d2){
			return 1;
		}else if(d1==d2){
			return 0;
		}else{
			return -1;
		}
	}
	
	/**
	 * 判断排序类型是否是升序"ASC"或降序"DESC"，如果是返回原字符串，如果不是或为null，那么将默认返回"DESC"
	 * @param sortType
	 * @return
	 */
	public static String sortDefDesc(String sortType){
		if(sortType==null){
			return DESC;
		}
		String upper=sortType.toUpperCase();
		return DESC.equals(upper)||ASC.equals(upper)?sortType:DESC;
	}
	/**
	 * 判断排序类型是否是升序"ASC"或降序"DESC"，如果是返回原字符串，如果不是或为null，那么将默认返回"ASC"
	 * @param sortType
	 * @return
	 */
	public static String sortDefAsc(String sortType){
		if(sortType==null){
			return ASC;
		}
		String upper=sortType.toUpperCase();
		return DESC.equals(upper)||ASC.equals(upper)?sortType:ASC;
	}
}
