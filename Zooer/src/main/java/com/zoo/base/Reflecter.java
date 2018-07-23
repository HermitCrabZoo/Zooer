package com.zoo.base;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

import sun.misc.Unsafe;

/**
 * 泛型工具
 * @author ZOO
 *
 */
public final class Reflecter {
	
	private Reflecter() {}
	
    /**
     * 设置类的静态变量/常量的值,不支持基本类型的常量字段!
     * @param clazz
     * @param newValue
     */
    public static void setStatic(Class<?> clazz, String fieldName, Object newValue){
        if (Typer.notNull(clazz,fieldName)) {
        	try {
        		Field field=clazz.getDeclaredField(fieldName);
        		field.setAccessible(true);
        		Field modifiersField = Field.class.getDeclaredField("modifiers");
        		modifiersField.setAccessible(true);
        		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        		field.set(null, newValue);
        	}catch (Exception e){}
        }
    }

    /**
     * 获取类的静态变量/常量的值
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Optional<Object> getStatic(Class<?> clazz, String fieldName){
        if (Typer.notNull(clazz,fieldName)){
            try {
                Field field=clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return Optional.ofNullable(field.get(null));
            }catch (Exception e){}
        }
        return Optional.empty();
    }
    
    
    /**
     * 获取{@link Unsafe}实例
     * @return
     */
    public static Unsafe getUnsafe() {
    	return Unsafer.unsafe;
    }
    
    private static class Unsafer{
    	private static final Unsafe unsafe=(Unsafe) getStatic(Unsafe.class, "theUnsafe").orElse(null);
    }
    

}
