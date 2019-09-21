package com.zoo.mix;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.zoo.base.Strs;
import com.zoo.base.Typer;
import com.zoo.io.Filer;
import com.zoo.io.Pather;
import com.zoo.system.Platform;

public final class Packer {

	private Packer() {}
	
	private static final int BUFFER = 8192;

	
	/**
	 * 将file解压到文件夹dock里
	 * @param zipPath
	 * @param toPath
	 * @return 
	 * @throws IOException
	 * @see {@link #unzip(Path, Path, Charset)}
	 */
	public static Path unzip(Path zipPath,Path toPath) throws IOException {
		return unzip(zipPath, toPath, null);
	}
	
	/**
	 * 将file解压到文件夹dock里，自动识别zip文件的字符集
	 * @param zipPath
	 * @param toPath
	 * @return
	 * @throws IOException
	 * @see {@link #unzip(Path, Path, Charset)}
	 */
	public static Path unzipAutoCharset(Path zipPath,Path toPath) throws IOException {
		return unzip(zipPath, toPath, Charsetor.discern(zipPath, Charset.defaultCharset()));
	}
	
	/**
	 * 将file解压到文件夹dock里，指定字符集
	 * @param zipPath 不能为null，必须为可读的zip压缩包
	 * @param toPath 不能为null，必须为可写的目录
	 * @param charset 字符集，可为null
	 * @return 返回输出目录(dock对象)
	 * @throws IOException
	 */
	public static Path unzip(Path zipPath,Path toPath,Charset charset) throws IOException {
		if (Filer.isReadableFile(zipPath) && !Filer.isFile(toPath) && !toPath.startsWith(zipPath)) {
			Filer.createDirIfNotExists(toPath);//创建输出目录
			String dest=toPath.toString();
			//读取zip文件
			ZipFile zFile=null;
			if(charset!=null) {
				zFile=new ZipFile(zipPath.toFile(), ZipFile.OPEN_READ, charset);//设定字符集
			}else {
				zFile=new ZipFile(zipPath.toFile(), ZipFile.OPEN_READ);
			}
			Enumeration<? extends ZipEntry> zEntries=zFile.entries();
			//遍历zip包里面的文件并输出
			byte [] b = new byte[BUFFER];//字节数组，每次读取4096个字节  
			while (zEntries.hasMoreElements()) {
				ZipEntry zipEntry = zEntries.nextElement();
				Path one=Paths.get(Pather.join(dest, zipEntry.getName()));
				if (zipEntry.isDirectory()) {
					//创建文件夹
					Filer.createDirIfNotExists(one);
				}else {
					Filer.createFileIfNotExists(one);
					try(
			                InputStream is = zFile.getInputStream(zipEntry);//读取元素  
			                BufferedInputStream bis = new BufferedInputStream(is);//读取流的缓存流 
			                CheckedInputStream cos = new CheckedInputStream(bis, new CRC32());//检查读取流，采用CRC32算法，保证文件的一致性
							OutputStream os = new FileOutputStream(one.toFile());//创建解压后的文件 
							BufferedOutputStream bos = new BufferedOutputStream(os,BUFFER);//带缓的输出流 
					) {
						Filer.copy(cos, bos, b);
					} catch (Exception e) {
						e.printStackTrace();
					}
					 
				}
			}
			zFile.close();
		}
		return toPath;
	}
	
	/**
	 * 将dock压缩到名为zipPath的zip格式文件中。
	 * @param fromPath
	 * @param zipPath
	 * @return
	 * @throws IOException
	 * @see {@link #zip(Path, Path, Charset)}
	 */
	public static Checksum zip(Path fromPath,Path zipPath) throws IOException {
		return zip(fromPath, zipPath, null);
	}
	
	/**
	 * 将dock压缩到名为zipPath的zip格式文件中，指定压缩的字符集。
	 * @param fromPath 需要被压缩的文件或文件夹，需要有可读权限。
	 * @param zipPath 压缩后保存的zip文件，不能是已存在的目录，不能在dock以及dock的子目录下。
	 * @param charset 压缩为指定的字符集
	 * @return Checksum
	 * @throws IOException
	 */
	public static Checksum zip(Path fromPath,Path zipPath,Charset charset) throws IOException {
		Checksum checksum=new CRC32();
		if(Filer.isReadable(fromPath) && !Filer.isDir(zipPath) && !zipPath.startsWith(fromPath)) {
			Filer.createDirIfNotExists(zipPath.getParent());
			try(FileOutputStream fos = new FileOutputStream(zipPath.toFile());
				BufferedOutputStream bos = new BufferedOutputStream(fos,BUFFER);//用带缓冲的输出流包装
				CheckedOutputStream cos=new CheckedOutputStream(bos, new CRC32());
				ZipOutputStream zos=charset==null?new ZipOutputStream(cos):new ZipOutputStream(cos,charset);//设定字符集
			) {
				byte [] b = new byte[BUFFER];
				String parent=fromPath.getParent().toAbsolutePath().toString()+Platform.slash();
				for(Path p:Filer.paths(fromPath, null)) {
					//如果是文件就需要写入数据,非文件则只需创建文件夹
					String pt=Strs.removeStart(p.toAbsolutePath().toString(), parent);
					if (Files.isRegularFile(p)) {
						zos.putNextEntry(new ZipEntry(pt));
						FileInputStream fis = new FileInputStream(p.toFile());
						//用带缓冲的输入流包装，加快速度
			            BufferedInputStream bis = new BufferedInputStream(fis);
						Filer.copy(bis, zos, b);
			            bis.close();
			            fis.close();
					}else {
						zos.putNextEntry(new ZipEntry(pt+Platform.SLASH));//文件夹以'/'结尾
					}
				}
				checksum=cos.getChecksum();
			} catch (IOException e) {
				throw e;
			}
		}
		return checksum;
	}
	
	/**
	 * 解压gzip包到目录或文件
	 * @param gzipPath
	 * @param to 若该参数指向一个目录，则解压后的文件在此目录下文件名不变。
	 * @return
	 * @throws IOException
	 */
	public static Path ungzip(Path gzipPath,Path to) throws IOException {
		if(Filer.isReadableFile(gzipPath) &&!Filer.isSame(gzipPath.getParent(), to) && !to.startsWith(gzipPath)) {
			Path toPath=Filer.isDir(to)?Paths.get(to.normalize().toString(), gzipPath.getFileName().toString()):to;
			Filer.createDirIfNotExists(toPath.getParent());
			try(
				FileInputStream fis=new FileInputStream(gzipPath.toFile());
				BufferedInputStream bis=new BufferedInputStream(fis, BUFFER);
				GZIPInputStream gis=new GZIPInputStream(bis, BUFFER);
				FileOutputStream fos = new FileOutputStream(toPath.toFile());
				BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
			) {
				Filer.copy(gis, bos);
			} catch (IOException e) {
				throw e;
			}
		}
		return to;
	}
	
	/**
	 * 将文件以gzip形式压缩
	 * @param fromPath
	 * @param gzipPath 可以是目录或文件，若是目录则压缩后的文件在此目录下，文件名不变。
	 * @return
	 * @throws IOException
	 */
	public static Path gzip(Path fromPath,Path gzipPath) throws IOException {
		if(Filer.isReadableFile(fromPath) && !Filer.isSame(fromPath.getParent(), gzipPath) && !gzipPath.startsWith(fromPath)) {
			Path toPath=Filer.isDir(gzipPath)?Paths.get(gzipPath.normalize().toString(), fromPath.getFileName().toString()):gzipPath;
			Filer.createDirIfNotExists(toPath.getParent());
			try(
				FileInputStream fis=new FileInputStream(fromPath.toFile());
				BufferedInputStream bis=new BufferedInputStream(fis, BUFFER);
				FileOutputStream fos = new FileOutputStream(toPath.toFile());
				BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
				GZIPOutputStream gos=new GZIPOutputStream(bos, BUFFER);
			) {
				Filer.copy(bis, gos);
			} catch (IOException e) {
				throw e;
			}
		}
		return gzipPath;
	}
	
	/**
	 * 将字符解压并返回新字符串
	 * @param str
	 * @return
	 * @throws Exception
	 * @see {@link #decompress(byte[])}
	 */
	public static String decompress(String str) throws Exception {
		return decompress(str,null);
	}
	
	/**
	 * 将字符串按特定编码解压返回新字符串
	 * @param str
	 * @param charset
	 * @return
	 * @throws Exception
	 * @see {@link #decompress(byte[])}
	 */
	public static String decompress(String str,Charset charset) throws Exception {
		if (str!=null) {
			if(charset!=null) {
				return new String(decompress(str.getBytes(charset)),charset);
			}else {
				return new String(decompress(str.getBytes()));
			}
		}
		return Strs.empty();
	}
	
	/**
	 * 将字节数组解压缩，返回解压后的数组
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static byte[] decompress(byte[] bytes) throws Exception {
		byte[] output = new byte[0];
		if (bytes!=null && bytes.length>0) {
			Inflater decompresser = new Inflater();
			decompresser.setInput(bytes);
			try(ByteArrayOutputStream o = new ByteArrayOutputStream(bytes.length);) {
				byte[] buf = new byte[BUFFER];
				while (!decompresser.finished()) {
					int i = decompresser.inflate(buf);
					o.write(buf, 0, i);
				}
				output = o.toByteArray();
			} catch (IOException | DataFormatException e) {
				throw e;
			}
			decompresser.end();
		}
        return output;
	}
	
	/**
	 * 对字符串进行压缩，返回压缩后的字节数组
	 * @param str
	 * @return
	 * @see #compress(String, Charset, int)
	 */
	public static byte[] compress(String str) {
		return compress(str.getBytes());
	}
	
	/**
	 * 对字符串按指定级别进行压缩，返回压缩后的字节数组
	 * @param str
	 * @param level
	 * @return
	 * @see #compress(String, Charset, int)
	 */
	public static byte[] compress(String str,int level) {
		return compress(str,null,level);
	}
	
	/**
	 * 对字符串按指定的编码进行压缩，返回压缩后的字节数组
	 * @param str
	 * @param charset
	 * @return
	 * @see #compress(String, Charset, int)
	 */
	public static byte[] compress(String str,Charset charset) {
		return compress(str,charset,Deflater.DEFAULT_COMPRESSION);
	}
	
	/**
	 * 对字符串按指定的编码和压缩级别进行压缩，返回压缩后的字节数组
	 * @param str
	 * @param charset
	 * @param level
	 * @return
	 * @see #compress(byte[], int)
	 */
	public static byte[] compress(String str,Charset charset,int level) {
		if (str!=null) {
			if(charset!=null) {
				return compress(str.getBytes(charset), level);
			}else {
				return compress(str.getBytes(), level);
			}
		}
		return Typer.bytes();
	}
	
	/**
	 * 对字节数组进行压缩，返回压缩后的数组
	 * @param bytes
	 * @return
	 * @see #compress(byte[], int)
	 */
	public static byte[] compress(byte[] bytes) {
		return compress(bytes, Deflater.DEFAULT_COMPRESSION);
	}
	
	/**
	 * 对字节数组进行压缩，返回压缩后的字节数组
	 * @param bytes
	 * @param level 压缩级别
	 * @return
	 */
	public static byte[] compress(byte[] bytes,int level) {
		if (bytes!=null && bytes.length>0) {
			byte[] output = new byte[bytes.length+10+Double.valueOf(Math.ceil(bytes.length*0.25f)).intValue()];
			Deflater compresser = new Deflater();
			compresser.setLevel(level);
			compresser.setInput(bytes);
			compresser.finish();
			int compressedDataLength = compresser.deflate(output);
			compresser.end();
			return Arrays.copyOf(output, compressedDataLength);
		}
		return Typer.bytes();
	}
	
	
}
