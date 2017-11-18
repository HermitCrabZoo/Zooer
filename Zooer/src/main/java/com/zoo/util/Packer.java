package com.zoo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class Packer {

	private Packer() {}
	
	private static final int BUFFER = 4096;
	
	/**
	 * 将file解压到文件夹dock里
	 * @param file 不能为null，必须为可读的zip压缩包
	 * @param dock 不能为null，必须为可写的目录
	 * @return 返回输出目录(dock对象)
	 * @throws IOException
	 */
	public static Path unzip(Path file,Path dock) throws IOException {
		if (Filer.isReadableFile(file) && !Filer.isFile(dock)) {
			Filer.createDirIfNotExists(dock);//创建输出目录
			String dest=dock.toString();
			//识别压缩包的编码，识别不了则当UTF-8来处理
			Charset charset=Charsetor.discern(file,Charset.defaultCharset());
			//读取zip文件
			ZipFile zFile=new ZipFile(file.toFile(), ZipFile.OPEN_READ, charset);
			Enumeration<? extends ZipEntry> zEntries=zFile.entries();
			//遍历zip包里面的文件并输出
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
							BufferedOutputStream bos = new BufferedOutputStream(os);//带缓的写出流 
					) {
						byte [] b = new byte[BUFFER];//字节数组，每次读取4096个字节  
						//循环读取压缩文件的值  
						while(cos.read(b)!=-1)  
						{  
							bos.write(b);//写入到新文件  
						}  
					} catch (Exception e) {
						e.printStackTrace();
					}
					 
				}
			}
			zFile.close();
		}
		return dock;
	}
	
	/**
	 * 将dock压缩到名为zipPath的zip格式文件中。
	 * @param dock 需要被压缩的文件或文件夹，需要有可读权限。
	 * @param zipPath 压缩后保存的zip文件，不能是已存在的目录，不能在dock以及dock的子目录下。
	 * @return
	 * @throws IOException
	 */
	public static Path zip(Path dock,Path zipPath) throws IOException {
		if(Filer.isReadable(dock) && !Filer.isDir(zipPath) && !zipPath.startsWith(dock)) {
			try(FileOutputStream fos = new FileOutputStream(zipPath.toFile());
				CheckedOutputStream cos=new CheckedOutputStream(fos, new CRC32());
				BufferedOutputStream bos = new BufferedOutputStream(fos);//用带缓冲的输出流包装
				ZipOutputStream zos = new ZipOutputStream(bos);
			) {
				byte [] b = new byte[BUFFER];
				String parent=dock.getParent().toAbsolutePath().toString()+Platform.slash();
				for(Path p:Filer.paths(dock, null)) {
					//如果是文件就需要写入数据,非文件则只需创建文件夹
					String pt=Strs.removeStart(p.toAbsolutePath().toString(), parent);
					if (Files.isRegularFile(p)) {
						zos.putNextEntry(new ZipEntry(pt));
						FileInputStream fis = new FileInputStream(p.toFile());
						//用带缓冲的输入流包装，加快速度
			            BufferedInputStream bis = new BufferedInputStream(fis);
			            int count;
						while ((count = bis.read(b, 0, BUFFER)) != -1) {
							zos.write(b, 0, count);
						}
			            bis.close();
			            fis.close();
					}else {
						zos.putNextEntry(new ZipEntry(pt+Platform.SLASH));//文件夹以'/'结尾
					}
				};
			} catch (IOException e) {
				throw e;
			}
		}
		return dock;
	}
}
