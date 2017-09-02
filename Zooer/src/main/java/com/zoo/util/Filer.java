package com.zoo.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Filer {

	private Filer() {}
	/**
	 * 文件或目录、存在
	 * @param path
	 * @return
	 */
	public static boolean isExists(Path path) {
		try {
			return Files.exists(path);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 文件、存在
	 * @param path
	 * @return
	 */
	public static boolean isFile(Path path){
		try {
			return Files.isRegularFile(path);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 目录、存在
	 * @param path
	 * @return
	 */
	public static boolean isDir(Path path){
		try {
			return Files.isDirectory(path);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 文件或目录、可读
	 * @param path
	 * @return
	 */
	public static boolean isReadable(Path path) {
		try {
			return Files.isReadable(path);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 文件或目录、可写
	 * @param path
	 * @return
	 */
	public static boolean isWritable(Path path) {
		try {
			return Files.isWritable(path);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 文件、存在、可读
	 * @param path
	 * @return
	 */
	public static boolean isReadableFile(Path path){
		return isFile(path)&&isReadable(path);
	}
	/**
	 * 目录、存在、可读
	 * @param path
	 * @return
	 */
	public static boolean isReadableDir(Path path){
		return isDir(path)&&isReadable(path);
	}
	/**
	 * 文件、存在、可写
	 * @param path
	 * @return
	 */
	public static boolean isWritableFile(Path path){
		return isFile(path)&&isWritable(path);
	}
	/**
	 * 目录、存在、可写
	 * @param path
	 * @return
	 */
	public static boolean isWritableDir(Path path){
		return isDir(path)&&isWritable(path);
	}
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isReadableWritable(Path path) {
		return isReadable(path)&&isWritable(path);
	}
	/**
	 * 文件、存在、可读写
	 * @param path
	 * @return
	 */
	public static boolean isReadableWritableFile(Path path){
		return isReadableFile(path)&&isWritable(path);
	}
	/**
	 * 目录、存在、可读写
	 * @param path
	 * @return
	 */
	public static boolean isReadableWritableDir(Path path){
		return isReadableDir(path)&&isWritable(path);
	}
	/**
	 * 判断是否是一个根目录
	 * @param path
	 * @return
	 */
	public static boolean isRoot(Path path){
		if(isDir(path)) {
			Iterable<Path> roots=FileSystems.getDefault().getRootDirectories();;
			for(Path root:roots) {
				if (isSame(path, root)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 判断两个Path是否指向同一个目录或文件，是返回true
	 * @param path1
	 * @param path2
	 * @return 返回false不一定能确定这两个Path是指向同一个目录或文件
	 */
	public static boolean isSame(Path path1,Path path2) {
		try {
			return Files.isSameFile(path1, path2);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 文件拷贝
	 * @param source
	 * @param target
	 * @return
	 * @see #copy(Path, Path, Predicate, Charset)
	 */
	public static boolean copy(Path source,Path target) {
		return copy(source, target, null, null);
	}
	/**
	 * 文件拷贝
	 * @param source
	 * @param target
	 * @param destCharset
	 * @return
	 * @see #copy(Path, Path, Predicate, Charset)
	 */
	public static boolean copy(Path source,Path target,Charset destCharset) {
		return copy(source, target, null, destCharset);
	}
	/**
	 * 文件拷贝
	 * @param source
	 * @param target
	 * @param filter
	 * @see #copy(Path, Path, Predicate, Charset)
	 */
	public static boolean copy(Path source,Path target,Predicate<? super Path> filter) {
		return copy(source, target, filter, null);
	}
	/**
	 * 文件拷贝，被拷贝的目录或文件要有可读属性，目录无法拷贝到文件，两个不相同的目录或文件之间才能拷贝。
	 * @param source
	 * @param target
	 * @param filter
	 * @param destCharset
	 * @return
	 */
	public static boolean copy(Path source,Path target,Predicate<? super Path> filter,Charset destCharset){
		//被拷贝的目录或文件要有可读属性，目录无法拷贝到文件，两个不相同的目录或文件才能对考
		if(isReadable(source) && target!=null && !(isDir(source)&&isFile(target)) && !isSame(source, target) && !isSame(source.getParent(), target)) {
			String inParent=isFile(target)?source.normalize().toString():source.normalize().getParent().toString();
			String toParent=target.normalize().toString();
			paths(source, filter).forEach(p->{//遍历
				Path t=Paths.get(toParent, Strs.removeStart(p.toAbsolutePath().toString(),inParent));
				try {
					if (!Files.exists(t)) {//不存在则创建
						if (Files.isRegularFile(p)) {
							Files.createDirectories(t.getParent());
							Files.createFile(t);
						}else {
							Files.createDirectories(t);
						}
					}
					if (Files.isRegularFile(p)&&Files.isRegularFile(t)) {//文件对文件拷贝
						if(destCharset!=null) {
							transfer(p, t,Charsetor.discern(p),destCharset);
						}else {
							transfer(p, t,null,null);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			return true;
		}
		return false;
	}
	/**
	 * 通道传送文件，in与out必须是存在的文件
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private static void transfer(Path in,Path out,Charset inc,Charset onc) throws IOException {
		try(FileChannel ic = FileChannel.open(in, StandardOpenOption.READ);
			FileChannel oc = FileChannel.open(out,StandardOpenOption.WRITE);)
		{
			if (inc==null || onc==null) {
				ic.transferTo(0, ic.size(), oc);
			}else {
				ByteBuffer buffer = ByteBuffer.allocate(8192);
				while (ic.read(buffer) != -1) {
					buffer.flip();
					oc.write(onc.encode(inc.decode(buffer)));
					buffer.clear();
				}
			}
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * 获取目录下的文件后缀
	 * @param directory
	 * @param filter
	 * @return
	 */
	public static List<String> suffixs(Path directory,Predicate<? super Path> filter){
		return paths(directory, filter).stream().filter(Funcs.onlyFile).map(p->Pather.suffix(p.toString())).distinct().collect(Collectors.toList());
	}
	/**
	 * 获取目录下的所有目录和文件(包含当前传入目录)。
	 * @param directory
	 * @param filter
	 * @return
	 */
	public static List<Path> paths(Path directory,Predicate<? super Path> filter){
		List<Path> paths=new ArrayList<Path>();
		if (directory!=null) {
			try(Stream<Path> pathStream = Files.walk(directory, FileVisitOption.FOLLOW_LINKS)) {
				if (filter!=null) {
					pathStream.filter(filter).forEach(paths::add);
				}else {
					pathStream.forEach(paths::add);
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		return paths;
	}
}
