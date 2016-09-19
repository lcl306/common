package com.it.common.component.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @author liu
 * */
public class JsonUtil {
	
	/**
	 * json to bean
	 * @param jStr is json string
	 * @param cls is bean class
	 * @param clsMap is class map of properties in bean, String is property name, Class is property class
	 * */
	@SuppressWarnings("unchecked")
	public static <T> T toBean(String jStr, Class<?> cls, Map<String,Class<?>> clsMap){
		T o = null;
		if(clsMap!=null)
			o = (T)JSONObject.toBean(JSONObject.fromObject(jStr), cls, clsMap);
		else
			o = (T)JSONObject.toBean(JSONObject.fromObject(jStr), cls);
		return o;
	}
	
	/**
	 * json to list
	 * @param jStr is json string
	 * @param cls is bean class
	 * @param clsMap is class map of properties in bean, String is property name, Class is property class
	 * */
	@SuppressWarnings("unchecked")
	public static <T> List<T> toBeans(String jStr, Class<?> cls, Map<String, Class<?>> clsMap){
		if(cls.equals(String.class)){
			List list = new ArrayList();
			JSONArray jsonArray = JSONArray.fromObject(jStr);
			Iterator<String> it = jsonArray.iterator();
			while (it.hasNext()){
				list.add(it.next());
			}
			return list;
		}else{
			List<T> list = new ArrayList<T>();
			JSONArray jsonArray = JSONArray.fromObject(jStr);
			Iterator<JSONObject> it = jsonArray.iterator();
			while (it.hasNext()){
				JSONObject jsonObject = it.next();
			    T o = null;
			    if(clsMap!=null){
			    	o = (T)JSONObject.toBean(jsonObject, cls, clsMap);
			    }else{
			    	o = (T)JSONObject.toBean(jsonObject, cls);
			    }
			    list.add(o);
			}
			return list;
		}
	}
	
	
	/**
	 * json to bean
	 * @param jStr is json string
	 * @param cls is bean class
	 * */
	public static Object toBean(String jStr, Class<?> cls){
		return toBean(jStr, cls, null);
	}
	
	/**
	 * json to list
	 * @param jStr is json string
	 * @param cls is bean class
	 * */
	public static Object toBeans(String jStr, Class<?> cls){
            return toBeans(jStr, cls, null);
	}
	
	/**
	 * bean to json
	 * */
	public static String toJson(Object bean){
		return JSONObject.fromObject(bean).toString();
	}
	
	public static String toJson(Object bean, JsonConfig jsonConfig){ 
		return JSONObject.fromObject(bean, jsonConfig).toString();  

	}
	
	/**
	 * list of beans to json
	 * */
	public static <T> String toJsons(List<T> beans){
		return JSONArray.fromObject(beans).toString();
	}
	
	/**
	 * set of beans to json
	 * */
	public static <T> String toJsons(Set<T> beans){
		return JSONArray.fromObject(beans).toString();
	}

}
