package com.zoo.util;

import java.io.File;
import java.util.Optional;

public final class Files {

	private Files() {}
	/**
	 * 文件、存在
	 * @param file
	 * @return
	 */
	public static boolean isFile(File file){
		return Optional.ofNullable(file).map(f->f.exists()&&f.isFile()).orElse(false);
	}
	/**
	 * 目录、存在
	 * @param file
	 * @return
	 */
	public static boolean isDir(File file){
		return Optional.ofNullable(file).map(f->f.exists()&&f.isDirectory()).orElse(false);
	}
	/**
	 * 文件、存在、可读
	 * @param file
	 * @return
	 */
	public static boolean isReadableFile(File file){
		return isFile(file)&&file.canRead();
	}
	/**
	 * 目录、存在、可读
	 * @param file
	 * @return
	 */
	public static boolean isReadableDir(File file){
		return isDir(file)&&file.canRead();
	}
	/**
	 * 文件、存在、可写
	 * @param file
	 * @return
	 */
	public static boolean isWriteableFile(File file){
		return isFile(file)&&file.canWrite();
	}
	/**
	 * 目录、存在、可写
	 * @param file
	 * @return
	 */
	public static boolean isWriteableDir(File file){
		return isDir(file)&&file.canWrite();
	}
	/**
	 * 文件、存在、可读写
	 * @param file
	 * @return
	 */
	public static boolean isReadableWriteableFile(File file){
		return isReadableFile(file)&&file.canWrite();
	}
	/**
	 * 目录、存在、可读写
	 * @param file
	 * @return
	 */
	public static boolean isReadableWriteableDir(File file){
		return isReadableDir(file)&&file.canWrite();
	}
}
