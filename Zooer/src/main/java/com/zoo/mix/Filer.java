package com.zoo.mix;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zoo.base.Strs;
import com.zoo.base.Typer;
import com.zoo.cons.Funcs;

import jdk.internal.ref.Cleaner;
import sun.nio.ch.DirectBuffer;

public final class Filer {

	private Filer() {}
	
	private static final String MIME_VIDEO="video/";
	
	private static final String MIME_IMAGE="image/";
	
	private static final int BUFFER = 8192;
	
	/**
	 * 缓存root->FileStore
	 */
	private static ConcurrentHashMap<Path, FileStore> fsMap=new ConcurrentHashMap<Path, FileStore>();
	
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
	 * 创建文件，当不存在时则创建该文件，创建成功返回true，失败返回false；当存在时则不创建，若存在的是文件返回true，若存在的是目录返回false
	 * @param file
	 * @param attrs
	 * @return
	 */
	public static boolean createFileIfNotExists(Path file, FileAttribute<?>... attrs) {
		if (!isExists(file)) {
			createDirIfNotExists(file.getParent(), attrs);
			try {
				Files.createFile(file, attrs);
				return true;
			} catch (Exception e) {}
		}else {
			return isFile(file);
		}
		return false;
	}
	
	/**
	 * 创建目录，当不存在时则创建该文件夹，创建成功返回true，失败返回false；当存在时则不创建，若存在的是目录则返回true，若存在的是文件则返回false
	 * @param dir
	 * @param attrs
	 * @return
	 */
	public static boolean createDirIfNotExists(Path dir, FileAttribute<?>... attrs) {
		if (!isExists(dir)) {
			try {
				Files.createDirectories(dir, attrs);
				return true;
			} catch (Exception e) {}
		}else {
			return isDir(dir);
		}
		return false;
	}
	
	/**
	 * 文件拷贝
	 * @param source
	 * @param target
	 * @return
	 * @see #copy(Path, Path, Predicate, Charset)
	 */
	public static CopyResult copy(Path source,Path target) {
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
	public static CopyResult copy(Path source,Path target,Charset fromCharset,Charset toCharset) {
		return copy(source, target, null,fromCharset, toCharset);
	}
	
	/**
	 * 文件拷贝
	 * @param source
	 * @param target
	 * @param filter
	 * @see #copy(Path, Path, Predicate, Charset, Charset)
	 */
	public static CopyResult copy(Path source,Path target,Predicate<? super Path> filter) {
		return copy(source, target, filter,null, null);
	}
	
	/**
	 * 文件拷贝，被拷贝的目录或文件要有可读属性，目录无法拷贝到文件，两个不相同的目录或文件之间才能拷贝。
	 * @param source
	 * @param target
	 * @param filter
	 * @param destCharset
	 * @return
	 */
	public static CopyResult copy(Path source,Path target,Predicate<? super Path> filter,Charset fromCharset,Charset toCharset){
		CopyResult cr=CopyResult.instance();
		//被拷贝的目录或文件要有可读属性，目录无法拷贝到文件，两个不相同的目录或文件才能对考
		if (!isReadable(source)) {
			cr.setStatus(CopyResult.UN_READABLE);
			return cr;
		}
		if (target==null || (isDir(source)&&isFile(target))) {
			cr.setStatus(CopyResult.UN_WRITEABLE);
			return cr;
		}
		if (isSame(source.getParent(), target) || target.startsWith(source)) {
			cr.setStatus(CopyResult.SAME);
			return cr;
		}
		String inParent=isDir(target)||isDir(source)?source.normalize().getParent().toString():source.normalize().toString();
		String toParent=target.normalize().toString();
		paths(source, filter).forEach(p->{//遍历
			Path t=Paths.get(toParent, Strs.removeStart(p.toAbsolutePath().toString(),inParent));
			cr.add(p,t);
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
					transfer(p, t,fromCharset,toCharset);
				}
			} catch (IOException e) {
				cr.addException(e);
			}
		});
		cr.setStatus(CopyResult.COMPLETED);
		return cr;
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
				ByteBuffer buffer = ByteBuffer.allocate(BUFFER);
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
	
	/**
	 * 获取目录下的所有目录和文件(包含当前传入目录)。
	 * @param directory
	 * @return
	 */
	public static List<Path> paths(Path directory){
		return paths(directory, null);
	}
	
	
	/**
	 * 获取文件的MIME信息
	 * @param file
	 * @return
	 */
	public static Optional<String> mime(Path file) {
		try {
			return Optional.ofNullable(Files.probeContentType(file));
		} catch (Exception e) {}
		return Optional.empty();
	}
	
	/**
	 * 判断文件是否是视频类型文件
	 * @param file
	 * @return
	 */
	public static boolean isVideo(Path file) {
		return mime(file).map(m->m.contains(MIME_VIDEO)).orElse(false);
	}
	
	/**
	 * 判断文件是否是图片类型的文件
	 * @param file
	 * @return
	 */
	public static boolean isImage(Path file) {
		return mime(file).map(m->m.contains(MIME_IMAGE)).orElse(false);
	}
	
	/**
	 * 从in拷贝到out默认缓存数组长度为 {@link #BUFFER}
	 * @param in
	 * @param out
	 * @return
	 * @throws IOException
	 */
	public static long copy(InputStream in, OutputStream out) throws IOException {
		return copy(in, out, new byte[BUFFER]);
	}
	
	/**
	 * 从in拷贝到out
	 * @param in
	 * @param out
	 * @param buff
	 * @return
	 * @throws IOException
	 */
	public static long copy(InputStream in, OutputStream out, byte[] buff) throws IOException {
		long count = 0;
		int len = -1,blen=buff.length;
		while((len=in.read(buff, 0, blen))!=-1){
			out.write(buff, 0, len);
			count += len;
		}
		return count;
	}
	
	/**
	 * 输入流到输出流的拷贝,默认缓存数组的长度 {@link #BUFFER}
	 * @param reader
	 * @param writer
	 * @return
	 * @throws IOException
	 * @see {@link #copy(Reader, Writer, char[])}
	 */
	public static long copy(Reader reader, Writer writer) throws IOException {
		return copy(reader, writer,new char[BUFFER]);
	}
	
	/**
	 * 输入流到输出流的拷贝
	 * @param reader
	 * @param writer
	 * @param buff
	 * @return
	 * @throws IOException
	 */
	public static long copy(Reader reader, Writer writer, char[] buff) throws IOException {
		long count = 0;
		int len = -1,blen=buff.length;
		while((len=reader.read(buff, 0, blen))!=-1){
			writer.write(buff, 0, len);
			count += len;
		}
		return count;
	}

	
	
	/**
	 * 读取文件内容到字符串
	 * @param path
	 * @return
	 */
	public static String read(Path path) {
		return new String(readBytes(path));
	}
	
	
	/**
	 * 使用指定编码,读取文件内容到字符串
	 * @param path
	 * @param cs 字符串编码
	 * @return
	 */
	public static String read(Path path,Charset cs) {
		return new String(readBytes(path), cs);
	}
	
	
	/**
	 * 读取文件中每一行到字符串
	 * @param path
	 * @return
	 */
	public static List<String> readLines(Path path){
		return readLines(path,Charset.defaultCharset());
	}
	
	
	/**
	 * 使用指定编码,读取文件每一行内容到字符串
	 * @param path
	 * @param cs
	 * @return
	 */
	public static List<String> readLines(Path path,Charset cs) {
		List<String> lines;
		try {
			lines=Files.readAllLines(path, cs);
		} catch (IOException e) {
			lines=new ArrayList<String>();
		}
		return lines;
	}
	
	
	/**
	 * 读取文件内容到byte数组
	 * @param path
	 * @return
	 */
	public static byte[] readBytes(Path path) {
		byte[] contents;
		try {
			contents=Files.readAllBytes(path);
		} catch (IOException e) {
			contents=Typer.bytes();
		}
		return contents;
	}
	
	
	
	/**
	 * 获取文件或目录占用的空间(单位/字节)
	 * @param path
	 * @return
	 */
	public static long size(Path path) {
		long s=0L;
		try {
			s=Files.size(path);
		} catch (IOException e) {}
		return s;
	}
	
	
	/**
	 * 获取文件/目录创建时间,若获取失败则返回null
	 * @param path
	 * @return
	 */
	public static LocalDateTime creationTime(Path path) {
		try {
			BasicFileAttributeView basicview = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
			BasicFileAttributes attr = basicview.readAttributes();
			LocalDateTime time = LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneOffset.ofHours(8));
			return time;
		} catch (Exception e) {}
		return null;
	}

	/**
	 * 获取文件/目录最后修改时间,若获取失败则返回null
	 * @param path
	 * @param options
	 * @return
	 */
	public static LocalDateTime modifiedTime(Path path) {
		try {
			FileTime fileTime=Files.getLastModifiedTime(path, LinkOption.NOFOLLOW_LINKS);
			LocalDateTime time=LocalDateTime.ofInstant(fileTime.toInstant(), ZoneOffset.ofHours(8));
			return time;
		} catch (IOException e) {}
		return null;
	}
	
	
	/**
	 * 释放内存映射文件的文件句柄
	 * @param var0
	 */
	public static void unmap(MappedByteBuffer var0) {
		if (var0 instanceof DirectBuffer) {
			Cleaner var1 = ((DirectBuffer)var0).cleaner();
			if (var1 != null) {
				var1.clean();
			}
		}
    }
	
	
	/**
	 * 将sizeOfByte转换为{B,KB,MB,GB,TB,PB,EB,ZB,YB}中最适合的那个单位来显示,返回转换后的字符串.
	 * @param sizeOfByte
	 * @return
	 */
	public static String grace(double sizeOfByte) {
		if (sizeOfByte<1024.0) {
			return Math.round(Math.floor(sizeOfByte))+"B";
		}
		String[] units=new String[] {"KB","MB","GB","TB","PB","EB","ZB"};
		for (int i=0;i<units.length;i++) {
			if (sizeOfByte<Math.pow(1024.0, i+2)) {
				double val=sizeOfByte/Math.pow(1024.0, i+1);
				double flat=Math.floor(val);
				return (val-flat==0?Math.round(flat)+"":String.format("%.2f", flat))+units[i];
			}
		}
		double val=sizeOfByte/Math.pow(1024.0, units.length+1);
		double flat=Math.floor(val);
		String res=String.format("%.2f", val);
		return (val-flat==0?Strs.removeEnd(res, ".00"):res)+"YB";
	}
	
	
	/**
	 * 获取path所在根目录的路径
	 * @param path
	 * @return
	 */
	public static Path driverRoot(Path path) {
		path = path.normalize();
		Iterable<Path> roots=FileSystems.getDefault().getRootDirectories();;
		for(Path root:roots) {
			if (path.startsWith(root)) {
				return root;
			}
		}
		return path;
	}
	
	
	/**
	 * 从缓存中获取FileStore
	 * @param path
	 * @return
	 */
	private static FileStore getFileStore(Path path) {
		Path root=driverRoot(path);
		FileStore fs=fsMap.get(root);
		if (fs==null) {
			try {
				fs=Files.getFileStore(root);
				fsMap.put(root, fs);
			} catch (IOException e) {}
		}
		return fs;
	}
	
	
	/**
	 * 获取驱动器总的字节大小
	 * @param driver
	 * @return
	 */
	public static long total(Path driver) {
		try {
			FileStore fs = getFileStore(driver);
			return fs.getTotalSpace();
		} catch (IOException e) {}
		return 0L;
	}
	
	
	/**
	 * 获取驱动器未分配的字节大小
	 * @param driver
	 * @return
	 */
	public static long unallocated(Path driver) {
		try {
			FileStore fs = getFileStore(driver);
			return fs.getUnallocatedSpace();
		} catch (IOException e) {}
		return 0L;
	}
	
	
	/**
	 * 获取驱动器未使用的字节大小
	 * @param driver
	 * @return
	 */
	public static long usable(Path driver) {
		try {
			FileStore fs = getFileStore(driver);
			return fs.getUsableSpace();
		} catch (IOException e) {}
		return 0L;
	}
	
	
	/**
	 * 获取驱动器已使用的字节大小
	 * @param driver
	 * @return
	 */
	public static long used(Path driver) {
		try {
			FileStore fs = getFileStore(driver);
			return fs.getTotalSpace()-fs.getUsableSpace();
		} catch (IOException e) {}
		return 0L;
	}
}
