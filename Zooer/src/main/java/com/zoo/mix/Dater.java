package com.zoo.mix;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.zoo.base.Strs;

/**
 * 日期工具类,该类是多线程安全的
 * @author ZOO
 *
 */
public final class Dater {
	private Dater(){}
	
	public static final String ALL_FORMAT="yyyy-MM-dd HH:mm:ss";
	
	public static final String YMD_FORMAT="yyyy-MM-dd";
	
	public static final String HMS_FORMAT="HH:mm:ss";
	
	private static final Clock CLOCK=Clock.systemUTC();
	
	/**
	 * 默认开始日期
	 */
	private static final LocalDate START=LocalDate.of(0, 1, 1);
	
	/**
	 * 缓存的日期格式编码器
	 */
	private static final Map<String, DateTimeFormatter> DFS=new HashMap<String, DateTimeFormatter>();
	
	
	/**
	 * 获取当前日期的"年月日时分秒"字符串(yyyy-MM-dd HH:mm:ss)
	 * @param date
	 * @return
	 */
	public static String formatDateTime() {
		return dateTime(ALL_FORMAT);
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
	 * 获取当前日期是一年中的"第几天"字符串
	 * @return 当前是一年中的"第几天"字符串
	 */
	public static String dayOfYear(){
		return String.valueOf(LocalDate.now().getDayOfYear());
	}
	/**
	 * 获取当前日期的"周几"字符串
	 * @return 当前"周几"字符串
	 */
	public static String week(){
		return String.valueOf(LocalDate.now().getDayOfWeek().getValue());
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
				.flatMap(p -> Optional.ofNullable(temporal).map(t -> formatter(p).format(t)))
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
	
	/**
	 * 缓存并获取DateTimeFormatter类的对象
	 * @param pattern
	 * @return
	 */
	private static synchronized DateTimeFormatter formatter(String pattern) {
		DateTimeFormatter dateTimeFormatter=DFS.get(pattern);
		if (dateTimeFormatter == null) {
			dateTimeFormatter=DateTimeFormatter.ofPattern(pattern);
			DFS.put(pattern, dateTimeFormatter);
		}
		return dateTimeFormatter;
	}
	
	/**
	 * 自1970-01-01T00:00Z (UTC)以来的毫秒数。
	 * @return
	 */
	public static long millis() {
		return CLOCK.millis();
	}
	
	/**
	 * 获取两个时间之间相差的年数
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long years(Temporal startInclusive, Temporal endExclusive) {
		return months(startInclusive, endExclusive)/12;
	}
	
	/**
	 * 获取两个时间之间相差的月数
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long months(Temporal startInclusive, Temporal endExclusive) {
		LocalDateTime incDt=getDateTime(startInclusive);
		LocalDateTime excDt=getDateTime(endExclusive);
		int y=excDt.getYear()-incDt.getYear();
		if (y>0&&excDt.minusYears(y).isBefore(incDt)) {
			y--;
		}else if(y<0&&excDt.minusYears(y).isAfter(incDt)) {
			y++;
		}
		LocalDateTime temp=incDt.plusYears(y);
		int m=(excDt.getYear()-temp.getYear())*12+excDt.getMonthValue()-temp.getMonthValue();
		temp=temp.plusMonths(m);
		//由于闰年2月29号转到非闰年的2月28号所以需要补足天数
		if (incDt.toLocalDate().isLeapYear()&&incDt.getMonthValue()==2&&incDt.getDayOfMonth()==29&&!excDt.toLocalDate().isLeapYear()) {
			temp=temp.plusDays(1);
		}
		if (m>0&&excDt.isBefore(temp)) {
			m--;
		}else if(m<0&&excDt.isAfter(temp)){
			m++;
		}
		return y*12+m;
	}
	
	/**
	 * 获取两个时间之间相差的月数
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long weeks(Temporal startInclusive, Temporal endExclusive) {
		return days(startInclusive, endExclusive)/7;
	}
	
	/**
	 * 获取两个时间之间相差的天数
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long days(Temporal startInclusive, Temporal endExclusive) {
		return duration(startInclusive,endExclusive).toDays();
	}
	
	/**
	 * 获取两个时间之间相差的小时数
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long hours(Temporal startInclusive, Temporal endExclusive) {
		return duration(startInclusive,endExclusive).toHours();
	}
	
	/**
	 * 获取两个时间之间相差的分钟数
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long minutes(Temporal startInclusive, Temporal endExclusive) {
		return duration(startInclusive,endExclusive).toMinutes();
	}
	
	/**
	 * 获取两个时间之间相差的秒数
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long seconds(Temporal startInclusive, Temporal endExclusive) {
		return duration(startInclusive,endExclusive).getSeconds();
	}
	
	/**
	 * 获取两个时间之间相差的毫秒数
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long millis(Temporal startInclusive, Temporal endExclusive) {
		return duration(startInclusive,endExclusive).toMillis();
	}
	
	/**
	 * 获取两个时间之间相差的微秒数
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long micros(Temporal startInclusive, Temporal endExclusive) {
		return millis(startInclusive, endExclusive)*1000;
	}
	
	/**
	 * 获取两个时间之间相差的纳秒数
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long nanos(Temporal startInclusive, Temporal endExclusive) {
		return duration(startInclusive,endExclusive).toNanos();
	}
	
	/**
	 * 获取Duration对象
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	private static Duration duration(Temporal startInclusive, Temporal endExclusive) {
		startInclusive=getDateTime(startInclusive);
		endExclusive=getDateTime(endExclusive);
		return Duration.between(startInclusive, endExclusive);
	}
	
	/**
	 * 通过temporal获取LocalDateTime对象，如果传入的是LocalTime对象，则日期部分用{@link #START}填充，如果传入的是LocalDate对象，那么时间部分将设置为0:0:0.0
	 * @param temporal
	 * @return
	 */
	private static LocalDateTime getDateTime(Temporal temporal) {
		if (temporal instanceof LocalDate) {
			return LocalDateTime.of(LocalDate.from(temporal),LocalTime.MIN);
		}else if (temporal instanceof LocalTime) {
			return LocalDateTime.of(START,LocalTime.from(temporal));
		}else {
			return LocalDateTime.from(temporal);
		}
	}
	
	/**
	 * 获取当前一周从周一到周日每天的日期对象的数组
	 * @return
	 * @see {@link #daysOfWeek(Temporal)}
	 */
	public static LocalDate[] daysOfNowWeek() {
		return daysOfWeek(LocalDate.now());
	}
	
	/**
	 * 获取传入日期所在这一周从周一到周日的每一天的日期,若传入日期为null或不支持年月日字段则返回一个长度为0的数组
	 * @param temporal
	 * @return
	 */
	public static LocalDate[] daysOfWeek(Temporal temporal) {
		LocalDate[] days=null;
		if (temporal!=null && temporal.isSupported(ChronoUnit.YEARS) && temporal.isSupported(ChronoUnit.MONTHS) && temporal.isSupported(ChronoUnit.DAYS)) {
			LocalDate day=LocalDate.from(temporal);
			DayOfWeek[] dows=DayOfWeek.values();
			int len=dows.length;
			days=new LocalDate[len];
			for(int i=0;i<len;i++) {
				days[i]=day.with(dows[i]);
			}
		}else {
			days=new LocalDate[0];
		}
		return days;
	}
	
	
	/**
	 * 获取当前月份从第一天到最后一天之间每天的日期对象数组
	 * @return
	 * @see {@link #daysOfMonth(Temporal)}
	 */
	public static LocalDate[] daysOfMonth() {
		return daysOfMonth(LocalDate.now());
	}
	
	/**
	 * 获取传入日期所在月份的第一天到最后一天之间每天的日期,若传入日期为null或不支持年月日字段则返回一个长度为0的数组
	 * @param temporal
	 * @return
	 */
	public static LocalDate[] daysOfMonth(Temporal temporal) {
		LocalDate[] days=null;
		if (temporal!=null && temporal.isSupported(ChronoUnit.YEARS) && temporal.isSupported(ChronoUnit.MONTHS) && temporal.isSupported(ChronoUnit.DAYS)) {
			LocalDate day=LocalDate.from(temporal).withDayOfMonth(1);
			int len=day.lengthOfMonth();
			days=new LocalDate[len];
			days[0]=day;
			for(int i=1;i<len;i++) {
				days[i]=day=day.plusDays(1);
			}
		}else {
			days=new LocalDate[0];
		}
		return days;
	}
}
