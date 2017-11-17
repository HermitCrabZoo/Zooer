package com.zoo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class Packer {

	private Packer() {}
	
	/**
	 * 将file解压到文件夹dock里
	 * @param file 不能为null，必须为可读的zip压缩包
	 * @param dock 不能为null，必须为可写的目录
	 * @return
	 * @throws IOException
	 */
	public static Path unzip(Path file,Path dock) throws IOException {
		if (Filer.isReadableFile(file) && !Filer.isFile(dock)) {
			Filer.createDirIfNotExists(dock);//创建输出目录
			String dest=dock.toString();
			//识别压缩包的编码，识别不了则当UTF-8来处理
			Charset charset=Charsetor.discern(file,Charsetor.UTF8);
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
					try(OutputStream os = new FileOutputStream(one.toFile());//创建解压后的文件 
			                BufferedOutputStream bos = new BufferedOutputStream(os);//带缓的写出流 
			                InputStream is = zFile.getInputStream(zipEntry);//读取元素  
			                BufferedInputStream bis = new BufferedInputStream(is);//读取流的缓存流 
			                CheckedInputStream cos = new CheckedInputStream(bis, new CRC32());//检查读取流，采用CRC32算法，保证文件的一致性
					) {
						byte [] b = new byte[4096];//字节数组，每次读取4096个字节  
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
}
