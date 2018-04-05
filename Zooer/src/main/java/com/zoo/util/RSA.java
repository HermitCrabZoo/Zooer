package com.zoo.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA {

	
	private static final String ALGORITHM="RSA";
	
	private static final int KEY_SIZE=1024;
	
	private static final int MAX_DECODE_LEN=128;
	
	private static final int MAX_ENCODE_LEN=117;
	
	private KeyPair keyPair;
	
	private PublicKey publicKey;
	
	private PrivateKey privateKey;
	
	private byte[] publics;
	
	private byte[] privates;
	
	private RSA() {}
	
	/**
	 * 构造拥有密匙对的RSA对象(KeyPair)
	 * @return
	 */
	public static RSA of() {
		return new RSA().init();
	}
	
	
	/**
	 * 初始化当前对象
	 * @return
	 */
	public RSA init() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			this.keyPair= keyPair;
			publicKey=keyPair.getPublic();
			privateKey=keyPair.getPrivate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	/**
	 * 获取公钥
	 * @return
	 */
	public PublicKey publicKey() {
		return publicKey;
	}
	
	
	/**
	 * 获取私钥
	 * @return
	 */
	public PrivateKey privateKey() {
		return privateKey;
	}
	
	
	/**
	 * 获取公钥字节数组
	 * @return
	 */
	public byte[] publicKeys() {
		return publics==null?(publics=keyPair.getPublic().getEncoded()):publics;
	}
	
	
	/**
	 * 获取私钥字节数组
	 * @return
	 */
	public byte[] privateKeys() {
		return privates==null?(privates=keyPair.getPrivate().getEncoded()):privates;
	}
	
	
	/**
	 * 对原文字节数组src使用公钥加密,返回加密后的字节数组
	 * @param src 需要被加密的原文字节数组
	 * @return
	 */
	public byte[] encode(byte[] src) {
        return encode(src, publicKey);
	}
	
	
	/**
	 * 对密文字节数组secrets使用私钥解密,返回解密后的字节数组
	 * @param secrets 待解密的字节数组
	 * @return
	 */
	public byte[] decode(byte[] secrets) {
		return decode(secrets, privateKey);
	}
	
	
	/**
	 * 对原文字节数组src使用给定的公钥publicKey加密,返回加密后的字节数组
	 * @param src 需被加密原文字节数组
	 * @param publicKey 指定的公钥
	 * @return
	 */
	public static byte[] encode(byte[] src,PublicKey publicKey) {
		return code(src, Cipher.ENCRYPT_MODE, publicKey);
	}
	
	
	/**
	 * 对原文字节数组src使用给定的公钥publicKeys字节数组加密,返回加密后的字节数组
	 * @param src 需被加密的原文字节数组
	 * @param publicKeys 指定的公钥字节数组
	 * @return
	 */
	public static byte[] encode(byte[] src,byte[] publicKeys) {
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeys);  
	        KeyFactory keyFactory = KeyFactorys.FACTORY;
			PublicKey publicKey = keyFactory.generatePublic(keySpec); 
			return code(src, Cipher.ENCRYPT_MODE, publicKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Typer.bytes();
	}
	
	
	
	/**
	 * 对密文字节数组secrets使用给定的私钥privateKey解密,返回解密后的字节数组
	 * @param secrets 待解密的密文字节数组
	 * @param privateKey 指定的私钥
	 * @return
	 */
	public static byte[] decode(byte[] secrets,PrivateKey privateKey) {
		return code(secrets, Cipher.DECRYPT_MODE, privateKey);
	}
	
	
	/**
	 * 对密文字节数组secrets使用给定私钥privateKeys字节数组解密,返回解密后的字节数组
	 * @param secrets 待解密的密文字节数组
	 * @param privateKeys 指定的私钥字节数组
	 * @return
	 */
	public static byte[] decode(byte[] secrets,byte[] privateKeys) {
		try {
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeys);  
	        KeyFactory keyFactory = KeyFactorys.FACTORY;
	        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
			return code(secrets, Cipher.DECRYPT_MODE, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Typer.bytes();
	}
	
	
	/**
	 * 编码(加密/解密)使用公钥/私钥
	 * @param codes
	 * @param opmode
	 * @param key
	 * @return
	 */
	private static byte[] code(byte[] codes,int opmode, Key key) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(opmode, key);
			int max=opmode==Cipher.ENCRYPT_MODE?MAX_ENCODE_LEN:MAX_DECODE_LEN;
			//分段加密/解密
			int len=codes.length,p=len/max+(len%max==0?0:1);
			byte[][] partitions=new byte[p][];
			for (int i = 0,offset=0; i < p; i++,offset+=max) {
				partitions[i]=cipher.doFinal(codes, offset, len-offset>=max?max:len-offset);
			}
			return Arrs.concat(partitions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Typer.bytes();
	}
	
	
	/**
	 * 密钥对工厂单例获取类
	 * @author ZOO
	 *
	 */
	private static class KeyFactorys{
		private static final KeyFactory FACTORY=keyFactory();
		private static final KeyFactory keyFactory() {
			try {
				return KeyFactory.getInstance(ALGORITHM);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	
}
