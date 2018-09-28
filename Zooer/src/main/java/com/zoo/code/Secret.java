package com.zoo.code;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.zoo.base.Strs;
import com.zoo.base.Typer;

public class Secret {

	private static char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};    
	
	private static Encoder base64Encoder=Base64.getEncoder();
	private static Decoder base64Decoder=Base64.getDecoder();
	
	/**
	 * 对byte数组进行base64编码
	 * @param bytes
	 * @return 编码后的字符串
	 */
	public static String base64(String str) {
		return str==null?Strs.empty():base64Encoder.encodeToString(str.getBytes());
	}
	
	
	/**
	 * 对byte数组进行base64编码
	 * @param bytes
	 * @return 编码后的byte数组
	 */
	public static byte[] base64(byte[] bytes) {
		return bytes==null?Typer.bytes():base64Encoder.encode(bytes);
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
		} catch (Exception e) {}
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
		} catch (Exception e) {}
		return Typer.bytes();
	}
	
	/**
	 * 将base64编码的byte数组解码
	 * @param bytes
	 * @param file
	 * @return 解码后的byte数组
	 */
	public static byte[] unbase64(byte[] bytes) {
		try {
			return base64Decoder.decode(bytes);
		} catch (Exception e) {}
		return Typer.bytes();
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
		return messageDigest(file, "MD5");
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
	 * SHA-1加密文件
	 * @param str
	 * @return
	 */
	public static String sha1(Path file) {
		return messageDigest(file, "SHA1");
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
	 * SHA2-224加密文件
	 * @param str
	 * @return
	 */
	public static String sha2_224(Path file) {
		return messageDigest(file, "SHA-224");
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
	 * SHA2-256加密文件
	 * @param str
	 * @return
	 */
	public static String sha2_256(Path file) {
		return messageDigest(file, "SHA-256");
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
	 * SHA2-384加密文件
	 * @param str
	 * @return
	 */
	public static String sha2_384(Path file) {
		return messageDigest(file, "SHA-384");
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
	 * SHA2-512加密文件
	 * @param str
	 * @return
	 */
	public static String sha2_512(Path file) {
		return messageDigest(file, "SHA-512");
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
	 * SHA3-224加密文件
	 * @param str
	 * @return
	 */
	public static String sha3_224(Path file) {
		return messageDigest(file, "SHA3-224");
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
	 * SHA3-256加密文件
	 * @param str
	 * @return
	 */
	public static String sha3_256(Path file) {
		return messageDigest(file, "SHA3-256");
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
	 * SHA3-384加密文件
	 * @param str
	 * @return
	 */
	public static String sha3_384(Path file) {
		return messageDigest(file, "SHA3-384");
	}
	
	/**
	 * SHA3-512加密
	 * @param str
	 * @return
	 */
	public static String sha3_512(String str) {
		return messageDigest(str, "SHA3-512");
	}
	
	/**
	 * SHA3-512加密文件
	 * @param str
	 * @return
	 */
	public static String sha3_512(Path file) {
		return messageDigest(file, "SHA3-512");
	}
	
	
	
	private static String messageDigest(Path file,String algorithm) {
		try(FileChannel in=FileChannel.open(file, StandardOpenOption.READ)) {
			MappedByteBuffer byteBuffer = in.map(FileChannel.MapMode.READ_ONLY, 0, Files.size(file));
			MessageDigest md5 = MessageDigest.getInstance(algorithm);
			md5.update(byteBuffer);
            return hex(md5.digest());
		} catch (Exception e) {}
		return Strs.empty();
	}
	
	
	
	private static String messageDigest(String str,String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(str.getBytes());
            return hex(digest.digest());
        } catch (Exception e) {}
        return Strs.empty();
    }
	

	
	/**
	 * HmacMD5加密
	 * @param str
	 * @param key
	 * @return
	 */
	public static String hmacMd5(String str,String key) {
		return hmac(str, key, "HmacMD5");
	}

	
	/**
	 * HmacSHA1加密
	 * @param str
	 * @param key
	 * @return
	 */
	public static String hmacSha1(String str,String key) {
		return hmac(str, key, "HmacSHA1");
	}

	
	/**
	 * HmacSHA224加密
	 * @param str
	 * @param key
	 * @return
	 */
	public static String hmacSha224(String str,String key) {
		return hmac(str, key, "HmacSHA224");
	}

	
	/**
	 * HmacSHA256加密
	 * @param str
	 * @param key
	 * @return
	 */
	public static String hmacSha256(String str,String key) {
		return hmac(str, key, "HmacSHA256");
	}

	
	/**
	 * HmacSHA384加密
	 * @param str
	 * @param key
	 * @return
	 */
	public static String hmacSha384(String str,String key) {
		return hmac(str, key, "HmacSHA384");
	}

	
	/**
	 * HmacSHA512加密
	 * @param str
	 * @param key
	 * @return
	 */
	public static String hmacSha512(String str,String key) {
		return hmac(str, key, "HmacSHA512");
	}
	
	
	
	private static String hmac(String str,String key,String algorithm) {
		try {
			//生成密匙
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), algorithm);
			Mac mac = Mac.getInstance(algorithm);
			mac.init(signingKey);
			return hex(mac.doFinal(str.getBytes()));
		} catch (Exception e) {}
		return Strs.empty();
	}

	
	
	/**
	 * 将byte[]转为十六进制字符串
	 * @param md
	 * @return
	 */
	private static String hex(byte[] md) {
		// 获得密文  
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
	
	
	/**
	 * DES加密
	 * @param src
	 * @param key
	 * @return
	 */
	public static byte[] des(String src,String key) {
		if (Typer.notNull(src,key)) {
			return des(src.getBytes(), key.getBytes());
		}
		return Typer.bytes();
	}
	
	
	/**
	 * DES加密
	 * @param src 
	 * @param key 密匙字节数组,长度必须大于或等于8
	 * @return 返回加密后得到字节数组
	 */
	public static byte[] des(byte[] src,byte[] key) {
		return secretCode(src, key, "DES", Cipher.ENCRYPT_MODE);
	}
	
	
	
	/**
	 * DES解密
	 * @param secrets
	 * @param key 密匙字节数组,长度必须大于或等于8
	 * @return 返回解密后的字节数组
	 */
	public static byte[] unDes(byte[] secrets,byte[] key) {
		return secretCode(secrets, key, "DES", Cipher.DECRYPT_MODE);
	}
	
	
	
	
	/**
	 * TripleDES加密
	 * @param src
	 * @param key
	 * @return
	 */
	public static byte[] tripleDes(String src,String key) {
		if (Typer.notNull(src,key)) {
			return tripleDes(src.getBytes(), key.getBytes());
		}
		return Typer.bytes();
	}
	
	
	/**
	 * TripleDES加密
	 * @param src 
	 * @param key 密匙字节数组,长度必须大于或等于8
	 * @return 返回加密后得到字节数组
	 */
	public static byte[] tripleDes(byte[] src,byte[] key) {
		return secretCode(src, key, "DESede", Cipher.ENCRYPT_MODE);
	}
	
	
	
	/**
	 * TripleDES解密
	 * @param secrets
	 * @param key 密匙字节数组,长度必须大于或等于8
	 * @return 返回解密后的字节数组
	 */
	public static byte[] unTripleDes(byte[] secrets,byte[] key) {
		return secretCode(secrets, key, "DESede",Cipher.DECRYPT_MODE);
	}
	
	
	/**
	 * AES加密
	 * @param src
	 * @param key
	 * @return
	 */
	public static byte[] aes(String src,String key) {
		if (Typer.notNull(src,key)) {
			return aes(src.getBytes(), key.getBytes());
		}
		return Typer.bytes();
	}
	
	
	/**
	 * AES加密
	 * @param src 
	 * @param key 密匙字节数组,长度必须大于或等于8
	 * @return 返回加密后得到字节数组
	 */
	public static byte[] aes(byte[] src,byte[] key) {
		return secretCode(src, key, "AES", Cipher.ENCRYPT_MODE);
	}
	
	
	
	/**
	 * AES解密
	 * @param secrets
	 * @param key 密匙字节数组,长度必须大于或等于8
	 * @return 返回解密后的字节数组
	 */
	public static byte[] unAes(byte[] secrets,byte[] key) {
		return secretCode(secrets, key, "AES", Cipher.DECRYPT_MODE);
	}
	
	
	
	/**
	 * DES、AES等对称加密/解密
	 * @param secrets
	 * @param key
	 * @param algorithm
	 * @param mode
	 * @return
	 */
	private static byte[] secretCode(byte[] secrets,byte[] key,String algorithm,int mode) {
		try {
			//生成密匙
			SecretKeySpec signingKey = new SecretKeySpec(key, algorithm);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(mode, signingKey);
			return cipher.doFinal(secrets);
		} catch (Exception e) {}
		return Typer.bytes();
	}
	
	
}
