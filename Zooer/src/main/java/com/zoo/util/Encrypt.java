package com.zoo.util;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Encrypt {

	private static char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};    
	
	private static Encoder base64Encoder=Base64.getEncoder();
	private static Decoder base64Decoder=Base64.getDecoder();
	
	/**
	 * 对byte数组进行base64编码
	 * @param bytes
	 * @return 编码后的字符串
	 */
	public static String base64(byte[] bytes) {
		return bytes==null?Strs.empty():base64Encoder.encodeToString(bytes);
	}
	
	/**
	 * 对文件进行base64编码
	 * @param file
	 * @return 编码后的byte数组
	 */
	public static byte[] base64(Path file) {
		try {
			byte[] bytes = Files.readAllBytes(file);
			return base64Encoder.encode(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Typer.bytes();
	}
	
	/**
	 * 对base64字符串进行解码
	 * @param base64Str
	 * @return 解码后的byte数组
	 */
	public static byte[] unbase64(String base64Str) {
		try {
			return base64Decoder.decode(base64Str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Typer.bytes();
	}
	
	/**
	 * 将base64编码的byte数组解码到文件输出
	 * @param bytes
	 * @param file
	 * @return 解码后的byte数组
	 */
	public static byte[] unbase64(byte[] bytes,Path file) {
		byte[] decodes=Typer.bytes();
		try {
			decodes=base64Decoder.decode(bytes);
			Files.write(file, decodes, StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return decodes;
	}
	
	/**
	 * 对字符串进行md5加密16位
	 * @param str
	 * @return
	 */
	public static String md5_16(String str) {
		String md5Str=md5(str);
		return md5Str.isEmpty()?md5Str:md5Str.substring(8, 24);
	}
	
	/**
	 * 对字符串进行md5加密32位
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
        return messageDigest(str, "MD5");
    }
	
	/**
	 * 对文件进行md5加密32位
	 * @param file
	 * @return
	 */
	public static String md5(Path file) {
		try(FileChannel in=FileChannel.open(file, StandardOpenOption.READ)) {
			MappedByteBuffer byteBuffer = in.map(FileChannel.MapMode.READ_ONLY, 0, Files.size(file));
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
            return digest(md5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Strs.empty();
	}
	
	/**
	 * SHA-1加密:适用于长度不超过2^64二进制位的字符串
	 * @param str
	 * @return
	 */
	public static String sha1(String str) {
		return messageDigest(str, "SHA1");
	}
	
	/**
	 * SHA2-224加密:适用于长度不超过2^64二进制位的字符串
	 * @param str
	 * @return
	 */
	public static String sha2_224(String str) {
		return messageDigest(str, "SHA-224");
	}
	
	/**
	 * SHA2-256加密:适用于长度不超过2^64二进制位的字符串
	 * @param str
	 * @return
	 */
	public static String sha2_256(String str) {
		return messageDigest(str, "SHA-256");
	}
	
	/**
	 * SHA2-384加密:适用于长度不超过2^128二进制位的字符串
	 * @param str
	 * @return
	 */
	public static String sha2_384(String str) {
		return messageDigest(str, "SHA-384");
	}
	
	/**
	 * SHA2-512加密:适用于长度不超过2^128二进制位的字符串
	 * @param str
	 * @return
	 */
	public static String sha2_512(String str) {
		return messageDigest(str, "SHA-512");
	}
	
	/**
	 * SHA3-224加密
	 * @param str
	 * @return
	 */
	public static String sha3_224(String str) {
		return messageDigest(str, "SHA3-224");
	}
	
	/**
	 * SHA3-256加密
	 * @param str
	 * @return
	 */
	public static String sha3_256(String str) {
		return messageDigest(str, "SHA3-256");
	}
	
	/**
	 * SHA3-384加密
	 * @param str
	 * @return
	 */
	public static String sha3_384(String str) {
		return messageDigest(str, "SHA3-384");
	}
	
	/**
	 * SHA3-512加密
	 * @param str
	 * @return
	 */
	public static String sha3_512(String str) {
		return messageDigest(str, "SHA3-512");
	}
	
	
	private static String messageDigest(String str,String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(str.getBytes());
            return digest(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Strs.empty();
    }
	
	private static String digest(MessageDigest digest) {
		// 获得密文  
		byte[] md = digest.digest();  
        // 把密文转换成十六进制的字符串形式  
        int j = md.length;  
        char chars[] = new char[j * 2];  
        int k = 0;  
        for (int i = 0; i < j; i++) {  
            byte byte0 = md[i];  
            chars[k++] = hexDigits[byte0 >>> 4 & 0xf];  
            chars[k++] = hexDigits[byte0 & 0xf];  
        }  
        return new String(chars);
	}
}
