package com.zoo.mix;

import java.time.LocalDate;
import java.time.Year;

/**
 * 季节枚举常量，包含中文名、英文名、开始月份、结束月份、结束月份的天数。
 * @author ZOOM
 *
 */
public enum Season {
	
	/**
	 * 春
	 */
	SPRING("春","Spring",0,2,1,3,31),
	/**
	 * 夏
	 */
	SUMMER("夏","Summer",3,5,4,6,30),
	/**
	 * 秋
	 */
	AUTUMN("秋","Autumn",6,8,7,9,30),
	/**
	 * 冬
	 */
	WINTER("冬","Winter",9,11,10,12,31);
	
	private final String name;
	private final String ename;
	private final int start;
	private final int end;
	private final int startValue;
	private final int endValue;
	private final int monthDay;
	
	private Season(String name,String ename, int start, int end,int startValue,int endValue, int monthDay) {
		this.name = name;
		this.ename = ename;
		this.start = start;
		this.end = end;
		this.startValue=startValue;
		this.endValue=endValue;
		this.monthDay = monthDay;
	}
	
	/**
	 * 中文名
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 英文名
	 * @return
	 */
	public String getEname() {
		return ename;
	}
	
	/**
	 * 开始月份(从0开始)
	 * @return
	 */
	public int getStart() {
		return start;
	}
	
	/**
	 * 结束月份(最大11)
	 * @return
	 */
	public int getEnd() {
		return end;
	}
	
	
	/**
	 * 开始月份(从1开始)
	 * @return
	 */
	public int getStartValue() {
		return startValue;
	}

	/**
	 * 结束月份(最大12)
	 * @return
	 */
	public int getEndValue() {
		return endValue;
	}

	/**
	 * 结束月份的最大天数
	 * @return
	 */
	public int getMonthDay() {
		return monthDay;
	}
	
	/**
	 * 判断传入月份是否属于当前季节(月份范围1-12)
	 * @param month
	 * @return
	 */
	public boolean in(int month) {
		return this.startValue<=month && month<=this.endValue;
	}
	
	
	/**
	 * 获取当前对象所代表的季节在今年占有的天数
	 * @return
	 */
	public int days() {
		return days(LocalDate.now().getYear());
	}
	
	/**
	 * 获取当前对象所代表季节在传入年份里占有的天数
	 * @param year
	 * @return
	 */
	public int days(int year) {
		switch (this) {
		case SPRING:
			return 62+(Year.isLeap(year)?29:28);
		case SUMMER:
			return 91;
		case AUTUMN:
			return 92;
		case WINTER:
			return 92;
		default:
			return 0;
		}
	}
	
	/**
	 * 获取当前系统日期所在的季节的对象
	 * @return
	 */
	public static Season now() {
		int month=LocalDate.now().getMonthValue();
		Season[] seasons=Season.values();
		for (Season season : seasons) {
			if (season.in(month)) {
				return season;
			}
		}
		return null;
	}
	
}
