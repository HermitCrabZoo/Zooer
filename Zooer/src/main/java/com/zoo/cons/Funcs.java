package com.zoo.cons;

import java.nio.file.Path;
import java.util.function.Predicate;

import com.zoo.io.Filer;

public final class Funcs {
	private Funcs() {}
	/**
	 * 永远返回true
	 */
	public final static Predicate<? super Path> pathTrue=p->true;
	/**
	 * 永远返回false
	 */
	public final static Predicate<? super Path> pathFalse=p->false;
	/**
	 * 是文件返回true
	 */
	public final static Predicate<? super Path> onlyFile=Filer::isFile;
	/**
	 * 是目录返回true
	 */
	public final static Predicate<? super Path> onlyDir=Filer::isDir;

}
