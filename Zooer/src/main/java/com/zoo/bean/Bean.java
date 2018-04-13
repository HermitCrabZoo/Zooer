package com.zoo.bean;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * bean属性包装类,该类是线程安全的
 * @author ZOO
 *
 * @param <T>
 */
public class Bean<T> {
	
	/**
	 * 绑定的属性
	 */
	private T property;
	
	/**
	 * 读写锁(读写、写写互斥)
	 */
	private ReadWriteLock lock=new ReentrantReadWriteLock();
	
	
	/**
	 * 私有构造器,应使用{@link #of(Object)}、{@link #of(Object, Object)}、{@link #ofNull()}函数来构造实例
	 * @param property
	 */
	private Bean(T property) {
		this.property=property;
	}
	
	/**
	 * 使用参数来构造实例
	 * @param property 不能null
	 * @throws IllegalArgumentException 当property为null时,抛出此异常
	 * @return
	 */
	public static <T>Bean<T> of(T property){
		notNull(property);
		return new Bean<>(property);
	}
	
	/**
	 * 使用参数来构造实例,并提供一个默认参数def,以防在property为null时用来做替代.
	 * @param property
	 * @param def
	 * @throws IllegalArgumentException 当property与def同时为null时,抛出此异常
	 * @return
	 */
	public static <T>Bean<T> of(T property,T def){
		return of(property==null?def:property);
	}
	
	/**
	 * 构造一个空的Bean实例(该操作具有一定的风险性)
	 * @return
	 */
	public static <T>Bean<T> ofNull(){
		return new Bean<>(null);
	}
	
	
	/**
	 * 获取绑定的属性值
	 * @return
	 */
	public T get() {
		lock.readLock().lock();
		try {
			return property;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * 设置绑定的属性值
	 * @param property 不能为null
	 * @throws IllegalArgumentException 当property为null时,抛出此异常
	 */
	public void set(T property) {
		lock.writeLock().lock();
		try {
			notNull(property);
			System.out.println("通过");
			this.property = property;
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	/**
	 * 设置当前绑定的属性为null(该操作是危险的)
	 */
	public void setNull() {
		lock.writeLock().lock();
		try {
			this.property = null;
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	
	/**
	 * 断言t不为空
	 * @param t
	 */
	private static <T>void notNull(T t) {
		if (t==null) {
			throw new IllegalArgumentException("argument property must be not null !");
		}
	}
	
}
