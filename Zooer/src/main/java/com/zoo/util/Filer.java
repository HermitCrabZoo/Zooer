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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
	public static boolean copy(Path source,Path target){
		if(Files.isReadable(source)&&target!=null) {
			File tFile=target.toFile();
			if (!tFile.exists()){
				tFile.mkdirs();
			}
			String inParent=source.getParent().toString();
			String toParent=target.toString();
			try(Stream<Path> pathStream = Files.walk(source, FileVisitOption.FOLLOW_LINKS)) {
				pathStream.filter(Funcs.pathTrue).forEach(p->{//遍历
					Path t=Paths.get(toParent, Strs.removeStart(p.toAbsolutePath().toString(),inParent));
					try {
						if (!Files.exists(t)) {//目标不存在
							if (Files.isDirectory(p)) {//原path是目录则创建
								Files.createDirectories(t);
							}
						}
						if (!Files.isDirectory(p)&&!Files.isDirectory(t)) {//文件对文件拷贝
							transfer(p, t);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
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
	/**
	 * 通道传送文件
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private static void transfer(Path in,Path out) throws IOException {
		try(FileChannel ic = FileChannel.open(in);
			FileChannel oc = FileChannel.open(out);)
		{
			ic.transferTo(0, ic.size(), oc);
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
					pathStream.filter(filter).forEachOrdered(paths::add);
				}else {
					pathStream.forEachOrdered(paths::add);
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		return paths;
	}
}
