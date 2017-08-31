package com.zoo.util;

public final class Pather {
	private Pather(){}
	/**
	 * 组合路径和文件名组合成文件路径
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static String join(String path, String fileName) {
		path = Strs.nullToEmpty(path);
		fileName = Strs.nullToEmpty(fileName);
		if (path.isEmpty()&&fileName.isEmpty()) {
			return Strs.empty();
		}
		String tempPath = toPath(path);
		String tempFileName=toPath(fileName);
		return tempPath + (tempPath.endsWith(Platform.slash()) || tempFileName.startsWith(Platform.slash()) ? Strs.empty() 
				: Platform.slash())+tempFileName;
	}
	
	/**
	 * 将文件路径分割为目录和文件名
	 * 返回长度为2的字符串数组,第一个值为文件所在的目录,第二个值为文件名
	 * @param fileName
	 * @return
	 */
	public static String[] split(String fileName){
		String[] results=new String[2];
		fileName=toPath(Strs.nullToEmpty(fileName));
		if(fileName.contains(Platform.slash())){
			results[0]=fileName.substring(0, fileName.lastIndexOf(Platform.slash()));
			results[1]=fileName.substring(fileName.lastIndexOf(Platform.slash())+1,fileName.length());
		}else {
			results[0]=Strs.empty();
			results[1]=fileName;
		}
		return results;
	}
	
	/**
	 * 将文件名分割成主文件名和文件扩展名
	 * 返回长度为2的字符串数组,第一个值为文件名,第二个值为文件扩展名
	 * @param fileName
	 * @return
	 */
	public static String[] splitText(String fileName){
		String[] results=new String[2];
		fileName=split(fileName)[1];
		if (fileName.contains(".")) {
			int i=fileName.lastIndexOf(".");
			results[0]=fileName.substring(0, i);
			results[1]=fileName.substring(i+1,fileName.length());
		}else {
			results[0]=fileName;
			results[1]=Strs.empty();
		}
		return results;
	}
	
	/**
	 * 根据平台使用的不同级别目录之间的连接符，删除字符串末端的所有目录连接符
	 * @param str
	 * @return
	 */
	public static String removeEndSlash(String str){
		StringBuffer stringBuffer=new StringBuffer(Strs.nullToEmpty(str));
		while(stringBuffer.length()>1 && Platform.slash().equals(String.valueOf(stringBuffer.charAt(stringBuffer.length()-1)))){
			stringBuffer.deleteCharAt(stringBuffer.length()-1);
		}
		return stringBuffer.toString();
	}
	
	/**
	 * 将非本地的不同级别目录之间的连接符改为本地的目录连接符后返回
	 * @param path
	 * @return
	 */
	public static String toPath(String path){
		path=Strs.nullToEmpty(path);
		return Syss.isWindows() ? path.replace(Platform.SLASH, Platform.BACKSLASH).trim()
				:path.replace(Platform.BACKSLASH, Platform.SLASH).trim();
	}
}
