package com.zoo.util;

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

}
