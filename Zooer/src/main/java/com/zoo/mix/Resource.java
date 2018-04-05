package com.zoo.mix;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ImageIcon;

/**
 * 获取项目资源类
 * @author Administrator
 *
 */
public final class Resource
{
	private Resource(){}
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
    
    /**
     * 获取项目的bin目录或jar文件所在目录下的名为fileName的目录或文件，若获取失败则返回null。
     * @param fileName
     * @return
     */
    public static Path bins(String fileName){
    	URL url = Resource.class.getProtectionDomain().getCodeSource().getLocation();
    	Path path=null;
        try{
        	path=Paths.get(url.toURI());
        	if (Filer.isFile(path)) {
				path=path.getParent();
			}else {
				path=Paths.get(path.toString(), fileName);
			}
        } catch (Exception e){  
            e.printStackTrace();
        }  
		return path;
    }
}
