package com.it.common.component.db;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import com.it.common.component.lang.sys.SysUtil;

/**
 * @author liu
 * */
public class AppSessionFactory extends LocalSessionFactoryBean{

	private static ResourceBundle resources;

	private static String[] mappings;

	static {
		try {
			resources = ResourceBundle.getBundle("com/it/context/hibernateMapping");
			List<String> mappingList = new ArrayList<String>();
			Enumeration<String> mappingEnum = resources.getKeys();
			while (mappingEnum.hasMoreElements()) {
				String key = (String) mappingEnum.nextElement();
				String value = resources.getString(key);
				File xmlFile = new File(SysUtil.getClassRootPath()+value);
				if(xmlFile.exists()){
					mappingList.add(value);
				}
			}
			mappings = mappingList.toArray(new String[]{});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void setMappingResources(String mappingResources[]){
		super.setMappingResources(mappings);
    }

}
