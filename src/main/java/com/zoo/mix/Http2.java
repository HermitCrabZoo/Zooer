package com.zoo.mix;

import java.net.ServerSocket;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

import com.zoo.base.Strs;

/**
 * support for JAVA11
 * @author ZOO
 *
 */
public final class Http2 {
	
	private Http2(){}
	
	private static final String[] headers= {"accept", "*/*", "user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36"};
	
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
	public static String get(String url,Map<String, Object> param,Charset charset) {
		
		charset = Optional.ofNullable(charset).orElseGet(Charset::defaultCharset);
		String uri=url+(Strs.endsWith(url, "?")?"":"?")+param(param, charset);
		try {
			HttpClient client = getClient();
			HttpRequest request = HttpRequest.newBuilder(URI.create(uri))
					.headers(headers)
					.GET().build();
			HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString(charset));
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
			charset = Optional.ofNullable(charset).orElseGet(Charset::defaultCharset);
			HttpClient client = getClient();
			BodyPublisher body=BodyPublishers.ofString(param(param, charset), charset);
			HttpRequest request = HttpRequest.newBuilder(URI.create(url))
					.headers(headers)
					.POST(body)
					.build();
			HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString(charset));
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
    public static String param(Map<String, Object> map,Charset charset){
    	StringBuffer sb=new StringBuffer();
    	if(map!=null&&!map.isEmpty()){
    		for(String key:map.keySet()){
    			sb.append(key).append("=").append(map.get(key)).append("&");
    		}
    		sb.deleteCharAt(sb.length()-1);
    		if(charset!=null){
    			return URLEncoder.encode(sb.toString(), charset);
    		}
    	}
    	return sb.toString();
    }
    
    
    /**
     * 判断端口是否是可用的(未被占用)
     * @param port
     * @return
     */
	public static boolean isPortUnused(int port) {
		boolean ret = false;
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

}
