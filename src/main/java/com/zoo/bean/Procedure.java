package com.zoo.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * 属性转换过程工具,多线程安全
 */
@SuppressWarnings("rawtypes")
public class Procedure {
	
	private Map<String,List<Processor>> procs=new HashMap<String,List<Processor>>();
	
	private ReadWriteLock lock=new ReentrantReadWriteLock();
	
	/**
	 * 为属性名添加处理器来处理该属性
	 * 每个属性对应的处理器队列按处理器添加的顺序对该属性值进行处理
	 * @param propertyName
	 * @param proc
	 * @return
	 */
	public <T>Procedure add(String propertyName,Processor<T> proc){
		lock.writeLock().lock();
		try {
			List<Processor> ps=procs.get(propertyName);
			if(ps==null){
				ps=new ArrayList<Processor>();
				procs.put(propertyName, ps);
			}
			ps.add(proc);
			return this;
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	
	/**
	 * 为某个属性同时添加多个处理器
	 * @param propertyName
	 * @param procs
	 * @return
	 */
	public <T>Procedure add(String propertyName,List<Processor<T>> procs){
		if(procs!=null && !procs.isEmpty()){
			for(Processor<T> proc:procs){
				add(propertyName, proc);
			}
		}
		return this;
	}
	
	
	/**
	 * 添加处理器的键值对key-value.
	 * @param map
	 * @return
	 */
	public <T>Procedure add(Map<String,Processor<T>> map){
		if(map!=null && !map.isEmpty()){
			for(String propertyName:map.keySet()){
				add(propertyName, map.get(propertyName));
			}
		}
		return this;
	}
	
	
	/**
	 * 添加处理器的键值对key-values.
	 * @param maps
	 * @return
	 */
	public <T>Procedure addAll(Map<String,List<Processor<T>>> maps){
		if(maps!=null && !maps.isEmpty()){
			for(String propertyName:maps.keySet()){
				add(propertyName, maps.get(propertyName));
			}
		}
		return this;
	}
	
	
	/**
	 * 将另一个Procedure的处理器队列合并到当前处理队列中
	 * @param procedure
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Procedure concat(Procedure procedure){
		if(procedure != null && procedure != this && !procedure.procs.isEmpty()){
			lock.writeLock().lock();
			try {
				for(String key:procedure.procs.keySet()){
					List<Processor> ps=procedure.procs.get(key);
					if(ps!=null){
						for(Processor p:ps){
							add(key, p);
						}
					}
				}
			} finally {
				lock.writeLock().unlock();
			}
		}
		return this;
	}
	
	
	/**
	 * 对属性值进行处理，使用该属性名已映射的处理器来按处理器添加到该属性映射中的先后顺序对属性进行处理，返回最终处理结果对象
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T>T doProcess(String propertyName,T propertyValue){
		lock.readLock().lock();
		try {
			List<Processor> pros = procs.get(propertyName);
			if(pros!=null){
				T t=propertyValue;
				for(Processor<T> pro:pros){
					t=pro.process(t);
				}
				return t;
			}
			return propertyValue;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	@Override
	public String toString() {
		return procs.toString();
	}
}