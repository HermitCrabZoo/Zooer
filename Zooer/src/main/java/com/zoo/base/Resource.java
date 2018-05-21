package com.zoo.base;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ImageIcon;

import com.zoo.mix.Filer;
import com.zoo.mix.Pather;

/**
 * 获取项目资源类
 * @author Administrator
 *
 */
public final class Resource
{
	private Resource(){}
	
	private static final String CP=getCp();
	
	private static String getCp(){
        try{
            URL url = Resource.class.getProtectionDomain().getCodeSource().getLocation();
            Path path=Paths.get(url.toURI());
            if (Filer.isFile(path)) {
                path=path.getParent();
            }
            return path.toString();
        } catch (Exception e){}
        return "";
    }
	
	/**
	 * 获取类路径,若获取失败则返回空字符串
	 * @return
	 */
	public static String classPath() {
		return CP;
	}
	
	/**
	 * 获取classpath下的文件或目录
	 * @param path 相对于classpath下的路径
	 * @return
	 */
	public static Path onClassPath(String path) {
		return Paths.get(Pather.join(CP,path));
	}
	
	
    /**
	 * 传入图片名
	 * @param imageName
	 * @return
	 */
    public static ImageIcon getIcon(String imageName)
    {
    	return getIcon(null, imageName);
    }
    
    /**
	 * 传入目录和图片名
	 * @param folder
	 * @param imageName
	 * @return
	 */
    public static ImageIcon getIcon(String folder,String imageName) {
    	ImageIcon icon=null;
    	try{
        	URL url=Resource.class.getClassLoader().getResource(Pather.join(folder, imageName));
        	icon = new ImageIcon(url);// 创建图标
    	}catch(Exception e){	
    		e.printStackTrace();
    	}
    	return icon;
	}
    
}
