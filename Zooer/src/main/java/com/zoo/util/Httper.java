package com.zoo.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



public final class Httper {
	
	private Httper(){}
	
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
    	if (url!=null && param!=null) {
			url+="?"+param(param, charset);
		}
        String result = "";
        BufferedReader in = null;
        try {
            URL postUrl = new URL(url);  
            // 打开连接  
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();       
            connection.setRequestMethod("GET");        
            // Read from the connection. Default is true.  
            connection.setDoInput(true);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();  
            
            // 设置通用的请求属性
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            //把连接断了  
            connection.disconnect();  
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 url 发送post方法的请求
     * @param url 请求地址
     * @param param 请求参数
     * @param charset 请求参数的字符集编码
     * @return 响应结果
     */
    public static String post(String url, Map<String, Object> param,String charset) {
    	DataOutputStream out = null;
        BufferedReader in = null;
        String result = "";
        try {
        	//设置跨过HTTPS的安全验证
        	HttpsURLConnection.setDefaultHostnameVerifier(new Httper().new NullHostNameVerifier());
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        	
            URL postUrl = new URL(url);  
            // 打开连接  
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();       
            connection.setRequestMethod("POST");        
            // 设置是否向connection输出，因为这个是post请求，参数要放在  
            // http正文内，因此需要设为true  
            connection.setDoOutput(true);  
            // Read from the connection. Default is true.  
            connection.setDoInput(true);  
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的  
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
            // Post 请求不能使用缓存  
            connection.setUseCaches(false);
               //设置本次连接是否自动重定向   
            connection.setInstanceFollowRedirects(true);        
            // 意思是正文是urlencoded编码过的form参数  
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，  
            // 要注意的是connection.getOutputStream会隐含的进行connect。  
            connection.connect();  
            out = new DataOutputStream(connection.getOutputStream());  
            out.writeBytes(param(param, charset));
            //流用完记得关  
            out.flush();  
            out.close();  
            
            // 设置通用的请求属性
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            //把连接断了  
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
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
    
    
    private static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    } };

    public class NullHostNameVerifier implements HostnameVerifier {
        /*
         * (non-Javadoc)
         * 
         * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
         * javax.net.ssl.SSLSession)
         */
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            // TODO Auto-generated method stub
            return true;
        }
    }
    
}
