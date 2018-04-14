package com.zoo.bean;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 属性包装类,该类是多线程安全的
 * @author ZOO
 *
 * @param <T>
 */
public class Bun<T> {
	
	/**
	 * 绑定的属性
	 */
	private T stuffing;
	
	/**
	 * 读写锁(读写、写写互斥)
	 */
	private ReadWriteLock lock=new ReentrantReadWriteLock();
	
	
	/**
	 * 私有构造器,应使用{@link #of(Object)}、{@link #of(Object, Object)}、{@link #ofNull()}函数来构造实例
	 * @param stuffing
	 */
	private Bun(T stuffing) {
		this.stuffing=stuffing;
	}
	
	/**
	 * 使用参数来构造实例
	 * @param stuffing 不能null
	 * @throws IllegalArgumentException 当stuffing为null时,抛出此异常
	 * @return
	 */
	public static <T>Bun<T> of(T stuffing){
		notNull(stuffing);
		return new Bun<>(stuffing);
	}
	
	/**
	 * 使用参数来构造实例,并提供一个默认参数def,以防在stuffing为null时用来做替代.
	 * @param stuffing
	 * @param def
	 * @throws IllegalArgumentException 当stuffing与def同时为null时,抛出此异常
	 * @return
	 */
	public static <T>Bun<T> of(T stuffing,T def){
		return of(stuffing==null?def:stuffing);
	}
	
	/**
	 * 构造一个空的Bean实例(该操作具有一定的风险性)
	 * @return
	 */
	public static <T>Bun<T> ofNull(){
		return new Bun<>(null);
	}
	
	
	/**
	 * 获取绑定的属性值
	 * @return
	 */
	public T get() {
		lock.readLock().lock();
		try {
			return stuffing;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * 设置绑定的属性值
	 * @param stuffing 不能为null
	 * @throws IllegalArgumentException 当stuffing为null时,抛出此异常
	 */
	public void set(T stuffing) {
		lock.writeLock().lock();
		try {
			notNull(stuffing);
			this.stuffing = stuffing;
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
			this.stuffing = null;
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
			throw new IllegalArgumentException("argument stuffing must be not null !");
		}
	}
	
}
