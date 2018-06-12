package com.zoo.bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

import com.zoo.base.Strs;
import com.zoo.mix.Dater;

@FunctionalInterface
public interface Processor<T> {
	T process(T property);

	/**
	 * 日期 时间转换处理器处理器
	 */
	Processor<?> DATE_TIME_PROCESSOR = obj -> Optional.ofNullable(obj).map(o -> {
		if (LocalDateTime.class.isAssignableFrom(o.getClass())) {
			return Dater.format((LocalDateTime) o, Dater.ALL_FORMAT);
		} else if (Date.class.isAssignableFrom(o.getClass())) {
			return Dater.format((Date) o, Dater.ALL_FORMAT);
		}
		return o;
	}).orElse(Strs.empty());
	
	
	/**
	 * 日期转换处理器
	 */
	Processor<?> DATE_PROCESSOR = obj -> Optional.ofNullable(obj).map(o -> {
		if (LocalDate.class.isAssignableFrom(o.getClass())) {
			return Dater.format((LocalDate) o, Dater.YMD_FORMAT);
		} else if (Date.class.isAssignableFrom(o.getClass())) {
			return Dater.format((Date) o, Dater.YMD_FORMAT);
		}
		return o;
	}).orElse(Strs.empty());
	
	
	/**
	 * 时间转换处理器
	 */
	Processor<?> TIME_PROCESSOR = obj -> Optional.ofNullable(obj).map(o -> {
		if (LocalTime.class.isAssignableFrom(o.getClass())) {
			return Dater.format((LocalTime) o, Dater.HMS_FORMAT);
		} else if (Date.class.isAssignableFrom(o.getClass())) {
			return Dater.format((Date) o, Dater.HMS_FORMAT);
		}
		return o;
	}).orElse(Strs.empty());
}
