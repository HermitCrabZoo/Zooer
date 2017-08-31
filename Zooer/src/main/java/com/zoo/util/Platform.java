package com.zoo.util;

public final class Platform {
	private Platform(){}
	
	public static String SLASH="/";
	
	public static String BACKSLASH="\\";
	
	private static String CERTAIN_SLASH="";
	
	/**
	 * 获取不同级别的文件目录之间的连接符,若是windows平台则返回'\',其他平台返回'/'
	 * @return
	 */
	static String slash(){
		if (CERTAIN_SLASH.isEmpty()) {
			CERTAIN_SLASH=Syss.isWindows()?BACKSLASH:SLASH;
		}
		return CERTAIN_SLASH;
	}

}
