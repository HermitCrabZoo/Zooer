package com.zoo.bean;

/**
 * 属性包装类,该类是多线程安全的
 * 
 * @author ZOO
 *
 * @param <T>
 */
public class Bun<T> {

	/**
	 * 绑定的属性
	 */
	private volatile T stuffing;

	/**
	 * 私有构造器,应使用{@link #of(Object)}、{@link #of(Object, Object)}、{@link #ofNull()}函数来构造实例
	 * 
	 * @param stuffing
	 */
	private Bun(T stuffing) {
		this.stuffing = stuffing;
	}

	/**
	 * 使用参数来构造实例
	 * 
	 * @param stuffing 不能null
	 * @throws IllegalArgumentException 当stuffing为null时,抛出此异常
	 * @return
	 */
	public static <T> Bun<T> of(T stuffing) {
		notNull(stuffing);
		return new Bun<>(stuffing);
	}

	/**
	 * 使用参数来构造实例,并提供一个默认参数def,以防在stuffing为null时用来做替代.
	 * 
	 * @param stuffing
	 * @param def
	 * @throws IllegalArgumentException 当stuffing与def同时为null时,抛出此异常
	 * @return
	 */
	public static <T> Bun<T> of(T stuffing, T def) {
		return of(stuffing == null ? def : stuffing);
	}

	/**
	 * 构造一个空的Bean实例(该操作具有一定的风险性)
	 * 
	 * @return
	 */
	public static <T> Bun<T> ofNull() {
		return new Bun<>(null);
	}

	/**
	 * 获取绑定的属性值
	 * 
	 * @return
	 */
	public T get() {
		return stuffing;
	}

	/**
	 * 设置绑定的属性值
	 * 
	 * @param stuffing 不能为null
	 * @throws IllegalArgumentException 当stuffing为null时,抛出此异常
	 */
	public void set(T stuffing) {
		notNull(stuffing);
		this.stuffing = stuffing;
	}

	/**
	 * 设置当前绑定的属性为null(该操作是危险的)
	 */
	public void setNull() {
		this.stuffing = null;
	}

	/**
	 * 断言t不为空
	 * 
	 * @param t
	 */
	private static <T> void notNull(T t) {
		if (t == null) {
			throw new IllegalArgumentException("argument stuffing must be not null !");
		}
	}

}
