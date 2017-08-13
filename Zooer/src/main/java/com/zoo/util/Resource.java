package com.zoo.util;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

import javax.swing.ImageIcon;

import com.zoo.cons.Charsetc;

/**
 * 获取项目资源类
 * @author Administrator
 *
 */
public final class Resource
{
	private Resource(){}
    /**
	 * 传入图片名,默认去classpath下的'res'文件夹获取文件
	 * @param imageName
	 * @return
	 */
    public static ImageIcon getIcon(String imageName)
    {
    	return getIcon(null, imageName);
    }
    
    /**
	 * 传入目录和图片名,若folder为空则默认去classpath下的'res'文件夹获取文件
	 * @param folder
	 * @param imageName
	 * @return
	 */
    public static ImageIcon getIcon(String folder,String imageName) {
    	ImageIcon icon=null;
    	try{
        	URL url=Resource.class.getClassLoader().getResource((folder==null?"res"+Platform.SLASH:folder+Platform.SLASH)+imageName);
        	icon = new ImageIcon(url);// 创建图标
    	}catch(Exception e)
    	{	System.out.println("获取图标资源出错!");
    		e.printStackTrace();
    	}
    	return icon;
	}
    
    /**
     * 传入文件名，文件存在返回文件，否则返回null
     * @param fileName
     * @return
     */
    public static File getFile(String fileName)
    {
    	URL url = Resource.class.getProtectionDomain().getCodeSource().getLocation();  
        String filePath = null;  
        try
        {
            filePath = URLDecoder.decode(url.getPath(), Charsetc.UTF8);// 转化为utf-8编码  
        } catch (Exception e)
        {  
            e.printStackTrace();  
        }  
        if (filePath.endsWith(".jar"))// 可执行jar包运行的结果里包含".jar" 
        { 
            // 截取路径中的jar包名  
            filePath = filePath.substring(0, filePath.lastIndexOf(Platform.slash()) + 1);  
        }
		File file=new File(filePath+fileName);
		return file.exists()?file:null;
    }
}
