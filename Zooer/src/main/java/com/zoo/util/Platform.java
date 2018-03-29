package com.zoo.util;

public final class Platform {
	private Platform(){}
	
	public static final String SLASH="/";
	
	public static final String BACKSLASH="\\";
	
	private static String CERTAIN_SLASH;
	
	public static final String DLL=".dll";
	
	public static final String SO=".so";
	
	
	
	/**
	 * 获取不同级别的文件目录之间的连接符,若是windows平台则返回'\',其他平台返回'/'
	 * @return
	 */
	public static String slash(){
		if (CERTAIN_SLASH==null) {
			CERTAIN_SLASH=Syss.isWindows()?BACKSLASH:SLASH;
		}
		return CERTAIN_SLASH;
	}
	
	/**
	 * 获取当前系统平台下使用的jni文件的后缀<br/>
	 * windows:.dll<br/>
	 * other:.so
	 * @return
	 */
	public static String jniSuffix() {
		return Syss.isWindows()?DLL:SO;
	}
	
}
