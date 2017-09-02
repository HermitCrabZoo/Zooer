package com.zoo.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Optional;

public final class Dates {
	private Dates(){}
	public static final String allFormat="yyyy-MM-dd HH:mm:ss";
	/**
	 * 获取当前日期的"年月日时分秒"字符串(yyyy-MM-dd HH:mm:ss)
	 * @param date
	 * @return
	 */
	public static String formatDateTime() {
		return dateTime(allFormat);
	}
	/**
	 * 获取当前日期的"年月日时分秒毫秒"字符串(yyyy-MM-dd HH:mm:ss.SSS)
	 * @param date
	 * @return
	 */
	public static String formatDateTimeMilliSecond() {
		return dateTime("yyyy-MM-dd HH:mm:ss.SSS");
	}
	
	/**
	 * 获取当前日期的"年月日"字符串(yyyy-MM-dd)
	 * @param date
	 * @return
	 */
	public static String formatDate() {
		return LocalDate.now().toString();
	}
	
	/**
	 * 获取当前日期的"时分秒"字符串(HH:mm:ss)
	 * @param date
	 * @return
	 */
	public static String formatTime() {
		return dateTime("HH:mm:ss");
	}
	
	/**
	 * 获取当前日期的"年月日时分秒毫秒"字符串(无格式yyyyMMddHHmmssSSS)
	 * @return 当前"年月日时分秒毫秒"字符串
	 */
	public static String dateTimeMilliSecond(){
		return dateTime("yyyyMMddHHmmssSSS");
	}
	
	
	/**
	 * 获取当前日期的"年月日时分秒"字符串(无格式yyyyMMddHHmmss)
	 * @return 当前"年月日时分秒"字符串
	 */
	public static String dateTime(){
		return dateTime("yyyyMMddHHmmss");
	}
	
	
	/**
	 * 获取当前日期的"年月日时分"字符串(无格式yyyyMMddHHmm)
	 * @return 当前"年月日时分"字符串
	 */
	public static String dateHourMinute(){
		return dateTime("yyyyMMddHHmm");
	}
	
	
	/**
	 * 获取当前日期的"年月日时"字符串(无格式yyyyMMddHH)
	 * @return 当前"年月日时"字符串
	 */
	public static String dateHour(){
		return dateTime("yyyyMMddHH");
	}
	
	/**
	 * 获取当前日期的"年月日"字符串(无格式yyyyMMdd)
	 * @return 当前"年月日"字符串
	 */
	public static String date(){
		return dateTime("yyyyMMdd");
	}
	
	/**
	 * 获取当前日期的"年月"字符串(无格式yyyyMM)
	 * @return 当前"年月"字符串
	 */
	public static String yearMonth(){
		return dateTime("yyyyMM");
	}
	
	/**
	 * 获取当前日期的"月日"字符串(无格式MMdd)
	 * @return 当前"月日"字符串
	 */
	public static String monthDay(){
		return dateTime("MMdd");
	}

	/**
	 * 获取当前日期的"时分秒"字符串(无格式HHmmss)
	 * @return 当前"时分秒"字符串
	 */
	public static String time(){
		return dateTime("HHmmss");
	}
	
	/**
	 * 获取当前日期的"时分"字符串(无格式HHmm)
	 * @return 当前"时分"字符串
	 */
	public static String hourMinute(){
		return dateTime("HHmm");
	}
	
	/**
	 * 获取当前日期的"分秒"字符串(无格式mmsss)
	 * @return 当前"分秒"字符串
	 */
	public static String minuteSecond(){
		return dateTime("mmss");
	}
	
	/**
	 * 获取当前日期的"年份"字符串
	 * @return 当前"年份"字符串
	 */
	public static String year(){
		return String.valueOf(LocalDate.now().getYear());
	}
	/**
	 * 获取当前日期的"月份"字符串
	 * @return 当前"月份"字符串
	 */
	public static String month(){
		return String.valueOf(LocalDate.now().getMonthValue());
	}
	
	/**
	 * 获取当前日期的"日"字符串
	 * @return 当前"日"字符串
	 */
	public static String day(){
		return String.valueOf(LocalDate.now().getDayOfMonth());
	}
	
	/**
	 * 获取当前日期的"小时"字符串
	 * @return 当前"小时"字符串
	 */
	public static String hour(){
		return String.valueOf(LocalTime.now().getHour());
	}
	
	/**
	 * 获取当前日期的"分钟"字符串
	 * @return 当前"分钟"字符串
	 */
	public static String minute(){
		return String.valueOf(LocalTime.now().getMinute());
	}
	
	/**
	 * 获取当前日期的"秒"字符串
	 * @return 当前"秒"字符串
	 */
	public static String second(){
		return String.valueOf(LocalTime.now().getSecond());
	}
	
	/**
	 * 获取当前日期的"毫秒"字符串
	 * @return 当前"毫秒"字符串
	 */
	public static String milliSecond(){
		return dateTime("SSS");
	}
	
	/**
	 * 根据字符串格式返回当前日期时间字符串
	 * @param pattern 字符串格式
	 * @return 格式化后的日期时间字符串
	 */
	public static String dateTime(String pattern)
	{
		return format(LocalDateTime.now(),pattern);
	}
	/**
	 * 根据字符串格式返回当前日期字符串
	 * @param pattern 字符串格式
	 * @return 格式化后的日期字符串
	 */
	public static String date(String pattern)
	{
		return format(LocalDate.now(),pattern);
	}
	/**
	 * 根据字符串格式返回当前时间字符串
	 * @param pattern 字符串格式
	 * @return 格式化后的时间字符串
	 */
	public static String time(String pattern)
	{
		return format(LocalTime.now(), pattern);
	}
	/**
	 * 将时间按pattern格式化输出
	 * @param temporal
	 * @param pattern
	 * @return
	 */
	public static String format(TemporalAccessor temporal, String pattern) {
		return Optional.ofNullable(pattern)
				.flatMap(p -> Optional.ofNullable(temporal).map(t -> DateTimeFormatter.ofPattern(p).format(t)))
				.orElse(Strs.empty());
	}
	/**
	 * 将时间按pattern格式化输出
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		return Optional.ofNullable(pattern)
				.flatMap(p -> Optional.ofNullable(date).map(t -> new SimpleDateFormat(p).format(t)))
				.orElse(Strs.empty());
	}
}
