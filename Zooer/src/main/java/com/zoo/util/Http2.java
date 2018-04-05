package com.zoo.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

import com.zoo.base.Strs;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpRequest.BodyProcessor;
import jdk.incubator.http.HttpResponse;

/**
 * use for Java9
 * @author ZOO
 *
 */
public final class Http2 {
	private Http2(){}
	
	/**
	 * 发送get请求
	 * @param url 请求地址
	 * @return 响应结果
	 * @see #get(String, Map, String)
	 */
	public static String get(String url){
		return get(url, null,null);
	}
	
	/**
	 * 发送get请求
	 * @param url 请求地址
	 * @param param 请求参数(该参数将会被拼接到url的后面)
	 * @return 响应结果
	 * @see #get(String, Map, String)
	 */
	public static String get(String url,Map<String, Object> param){
		return get(url, param,null);
	}
	
	/**
	 * 发送post请求
	 * @param url 请求地址
	 * @return 响应结果
	 * @see #post(String, Map, String)
	 */
	public static String post(String url){
		return post(url, null,null);
	}
	
	/**
	 * 发送post请求
	 * @param url 请求地址
	 * @param param 请求参数
	 * @return 响应结果
	 * @see #post(String, Map, String)
	 */
	public static String post(String url, Map<String, Object> param){
		return post(url, param,null);
	}
	
	/**
     * 向指定url发送get方法的请求
     * @param url 请求地址
     * @param param 请求参数(该参数将会被拼接到url的后面)
     * @param charset 请求参数的字符集编码
     * @return 响应结果
     */
	public static String get(String url,Map<String, Object> param,String charset) {
		String uri=url+(Strs.endsWith(url, "?")?"":"?")+param(param, charset);
		try {
			HttpClient client = getClient();
			HttpRequest request = HttpRequest.newBuilder(URI.create(uri))
					.header("accept", "*/*")
		            .header("connection", "Keep-Alive")
		            .header("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
					.GET().build();
			HttpResponse<String> response = client.send(request,HttpResponse.BodyHandler.asString());
			String result=response.body();//响应结果
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Strs.empty();
	}
	
	/**
     * 向指定 url 发送post方法的请求
     * @param url 请求地址
     * @param param 请求参数
     * @param charset 请求参数的字符集编码
     * @return 响应结果
     */
    public static String post(String url, Map<String, Object> param,Charset charset) {
		try {
			HttpClient client = getClient();
			BodyProcessor body=BodyProcessor.fromString(param(param), Optional.ofNullable(charset).orElse(Charset.defaultCharset()));//请求体
			HttpRequest request = HttpRequest.newBuilder(URI.create(url))
					.header("accept", "*/*")
		            .header("connection", "Keep-Alive")
		            .header("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
					.POST(body)
					.build();
			HttpResponse<String> response = client.send(request,HttpResponse.BodyHandler.asString());
			String result=response.body();//响应结果
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Strs.empty();
    }
	
    /**
     * 内部私有静态类用于构造安全的单例
     * @author ZOO
     */
    private static class HttpClienter{
    	private static final HttpClient CLIENT = HttpClient.newHttpClient();
    }
    
    /**
     * 获取HttpClient单例
     * @return
     */
    private static HttpClient getClient() {
    	return HttpClienter.CLIENT;
    }
	
	/**
     * 将map编成http类型的参数返回(key1=value1&key2=value2)
     * @param map
     * @return
     */
    public static String param(Map<String, Object> map){
    	return param(map, null);
    }
    
    /**
     * 将map编成http类型的参数返回,有charset则编成对应的url编码字符串
     * @param map
     * @param charset
     * @return 返回格式：key1=value1&key2=value2
     */
    public static String param(Map<String, Object> map,String charset){
    	StringBuffer sb=new StringBuffer();
    	if(map!=null&&!map.isEmpty()){
    		for(String key:map.keySet()){
    			sb.append(key).append("=").append(map.get(key)).append("&");
    		}
    		sb.deleteCharAt(sb.length()-1);
    		if(charset!=null){
    			try {
    				return URLEncoder.encode(sb.toString(), charset);
    			} catch (UnsupportedEncodingException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	return sb.toString();
    }
}
