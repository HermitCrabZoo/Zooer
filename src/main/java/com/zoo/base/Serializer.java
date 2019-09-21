package com.zoo.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

/**
 * 对象序列化工具
 * @author ZOO
 *
 */
public final class Serializer {
	
	private Serializer() {}
	
	/**
	 * 序列化对象
	 * @param obj
	 * @return 序列化失败则返回空的byte[]数组
	 */
	public static <T>byte[] serialize(T obj) {
		try (
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();  
				ObjectOutputStream out = new ObjectOutputStream(byteOut);){
			out.writeObject(obj);
			return byteOut.toByteArray();
		} catch (Exception e) {}
		return Typer.bytes();
	}
	
	
	/**
	 * 反序列化对象
	 * @param bytes
	 * @param clazz
	 * @return 反序列化失败则返回{@link Optional#empty()}
	 */
	@SuppressWarnings("unchecked")
	public static <T>Optional<T> deserialize(byte[] bytes,Class<T> clazz) {
		try (
				ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
				ObjectInputStream in = new ObjectInputStream(byteIn);){
			return Optional.ofNullable((T)in.readObject());
		} catch (Exception e) {}
		return Optional.empty();
	}
}
