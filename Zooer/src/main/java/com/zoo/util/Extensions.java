package com.zoo.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Extensions {
	private Extensions(){}
	/**
	 * 传入文件或文件目录和文件名过滤器，获取此文件或此目录下的所有不重复的经过过滤的文件扩展名字符串数组
	 * @param file
	 * @param filter
	 * @return
	 */
	public static String[] getExtensionNames(File file,FilenameFilter filter) {
		return extensionNames(file, filter).parallelStream().distinct().sorted().toArray(String[]::new);
	}
	
	private static List<String> extensionNames(File file,FilenameFilter filter) {
		List<String> extensions=new ArrayList<String>();
		if (file.isDirectory() ) {
			File[] files=Optional.ofNullable(filter).map(fi->file.listFiles(fi)).orElse(file.listFiles());
			for (File f : files) {//继续遍历文件夹的下文件
				extensions.addAll(extensionNames(f, filter));
			}
		}else if(filter==null || filter.accept(file.getParentFile(), file.getName())){//通过过滤器
			String extensionName=Path.splitText(file.getName())[1];//获取扩展名
			if (!extensions.contains(extensionName)){
				extensions.add(extensionName);
			}
		}
		return extensions;
	}
	
	
}
