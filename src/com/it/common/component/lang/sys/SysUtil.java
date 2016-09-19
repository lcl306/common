package com.it.common.component.lang.sys;

import java.io.File;

public class SysUtil {
	
	public static String getClassRootPath(){
		String rootPath = SysUtil.class.getClassLoader().getResource(File.separator).getPath(); 
		if(rootPath!=null && rootPath.trim().length()>0){
			rootPath = rootPath.substring(0, rootPath.lastIndexOf("/")+1);
			rootPath = rootPath.replaceAll("%20", " ");
		}
		return rootPath;
	}

}
