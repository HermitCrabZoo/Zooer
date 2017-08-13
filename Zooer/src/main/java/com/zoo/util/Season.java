package com.zoo.util;

public enum Season {
	spring("春",0,2,31),summer("夏",3,5,30),autumn("秋",6,8,30),winter("冬",9,11,31);
	private final String name;
	private final int start;
	private final int end;
	private final int monthDay;
	private Season(String name, int start, int end, int monthDay) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.monthDay = monthDay;
	}
	public String getName() {
		return name;
	}
	public int getStart() {
		return start;
	}
	public int getEnd() {
		return end;
	}
	public int getMonthDay() {
		return monthDay;
	}
	
}
