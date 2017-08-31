package com.zoo.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public final class Funcs {
	private Funcs() {}
	/**
	 * 永远返回true
	 */
	public static Predicate<? super Path> pathTrue=p->true;
	/**
	 * 永远返回false
	 */
	public static Predicate<? super Path> pathFalse=p->false;
	/**
	 * 是文件返回true
	 */
	public static Predicate<? super Path> onlyFile=p->Files.isRegularFile(p);
	/**
	 * 是目录返回true
	 */
	public static Predicate<? super Path> onlyDir=p->Files.isDirectory(p);

}
