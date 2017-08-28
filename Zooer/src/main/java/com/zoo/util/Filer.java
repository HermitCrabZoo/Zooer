package com.zoo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class Filer {

	private Filer() {}
	/**
	 * 文件或目录、存在
	 * @param file
	 * @return
	 */
	public static boolean isExists(File file) {
		return Optional.ofNullable(file).map(f->f.exists()).orElse(false);
	}
	/**
	 * 文件、存在
	 * @param file
	 * @return
	 */
	public static boolean isFile(File file){
		return isExists(file)&&file.isFile();
	}
	/**
	 * 目录、存在
	 * @param file
	 * @return
	 */
	public static boolean isDir(File file){
		return isExists(file)&&file.isDirectory();
	}
	/**
	 * 文件或目录、可读
	 * @param file
	 * @return
	 */
	public static boolean isReadable(File file) {
		return isExists(file)&&file.canRead();
	}
	/**
	 * 文件或目录、可写
	 * @param file
	 * @return
	 */
	public static boolean isWriteable(File file) {
		return isExists(file)&&file.canWrite();
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
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isReadableWriteable(File file) {
		return isExists(file)&&file.canRead()&&file.canWrite();
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
	/**
	 * 判断是否是一个根目录
	 * @param file
	 * @return
	 */
	public static boolean isRoot(File file){
		if(isDir(file)) {
			File[] roots=File.listRoots();
			for(int i=0,size=roots.length;i<size;i++) {
				if (roots[i].equals(file)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 文件拷贝
	 * @param source
	 * @param target
	 * @return 拷贝成功返回true，否则返回false。
	 */
	public static boolean copy(File source,File target){
		if(isReadable(source)&&target!=null) {
			if (!target.exists()) {
				target.mkdirs();
			}
			Path origin=source.toPath();
			Path to=target.toPath();
			String parent=origin.getParent().toString();
			String toParent=to.getParent().toString();
			try(Stream<Path> pathStream = Files.walk(origin, FileVisitOption.FOLLOW_LINKS)) {
				pathStream.filter(Funcs.pathTrue).forEach(p->{
					System.out.println(p.toAbsolutePath());
					Path targetPath=Paths.get(toParent, p.getFileName().toString());
					if (!Files.exists(targetPath)) {
						try {
							Files.createDirectories(targetPath);
							Files.createFile(targetPath);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			boolean isDir=source.isDirectory();
			
			if (isDir) {
			}else {
				try (FileInputStream fis=new FileInputStream(source);
						FileOutputStream fos=new FileOutputStream(target);
						FileChannel in = fis.getChannel();
						FileChannel out = fos.getChannel();){
					in.transferTo(0, in.size(), out);
					return true;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return false;
	}
	/**
	 * 文件拷贝到指定字符集的目标文件
	 * @param source
	 * @param target
	 * @param charset 字符集，若为null或不支持该字符集，则返回false
	 * @return 拷贝成功返回true，否则返回false。
	 */
	public static boolean copy(File source,File target,String charset){
		if(isReadable(source)&&target!=null) {
			try (FileInputStream fis=new FileInputStream(source);
					FileOutputStream fos=new FileOutputStream(target);
					FileChannel in = fis.getChannel();
					FileChannel out = fos.getChannel();){
				if (charset==null || !Charset.isSupported(charset)) {
					return false;
				}
				Charset destCharset=Charset.forName(charset);
				Charset sourceCharset=null;
				String charsetName =new FileCharset().getCharset(source);
				if(charsetName!=null&&Charset.isSupported(charsetName)) {
					sourceCharset=Charset.forName(charsetName);
				}else {
					sourceCharset=Charset.defaultCharset();
				}
				ByteBuffer buffer = ByteBuffer.allocate(8192);
				while (in.read(buffer) != -1) {
					buffer.flip();
					out.write(destCharset.encode(sourceCharset.decode(buffer)));
					buffer.clear();
				}
				return true;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return false;
	}
}
