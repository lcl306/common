package com.it.common.component.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * @author liu
 * */
public class DaoUtil {

	static Session getSession(HibernateTemplate hibernateTemplate){
		return SessionFactoryUtils.getSession(hibernateTemplate.getSessionFactory(), false);
	}
	
	static List<Map<String, Object>> getMapBySql(JdbcTemplate jdbcTemplate, String sql, Map<String, ?> params){
		NamedParameterJdbcTemplate nt = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		return nt.queryForList(sql, params);
	}

	static <T> List<T> getObjectsBySql(JdbcTemplate jdbcTemplate, String sql,  Class<T> cls) {
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		Iterator<Map<String, Object>> it = results.iterator();
		List<T> resultList = new ArrayList<T>();
		T obj = null;
		while (it.hasNext()) {
			Map<String, Object> recordMap =  it.next();
			try {
				obj = cls.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mapToBeanTrans(recordMap, obj);
			if (obj != null)
				resultList.add(obj);
		}
		return resultList;
	}

	static <T,I> List<T> getObjectsBySql(JdbcTemplate jdbcTemplate, String sql, Class<T> beanCls, Class<I> idCls){
		List<Map<String, Object>> results = null;
		try{
			results = jdbcTemplate.queryForList(sql);
		}catch(Exception e){
			SQLException se = (SQLException)e.getCause();
			int sc = se.getErrorCode();
			if(sc==54) return null;
			else e.printStackTrace();
			return null;
		}
		Iterator<Map<String, Object>> it = results.iterator();
		List<T> resultList = new ArrayList<T>();
		T obj = null;
		I id = null;
		while (it.hasNext()) {
			Map<String, Object> recordMap = it.next();
			try {
				id = idCls.newInstance();
			} catch (Exception e) {}
			DaoUtil.mapToBeanTrans(recordMap, id);
			try{
				Constructor<T> setId = beanCls.getConstructor(new Class[]{id.getClass()});
				obj = setId.newInstance(new Object[]{id});
			}catch(Exception e){
				e.printStackTrace();
			}
			DaoUtil.mapToBeanTrans(recordMap, obj);
			if (obj != null)
				resultList.add(obj);
		}
		return resultList;
	}

	static <T> T getObjectByResultSet(ResultSet rs, T bean)throws SQLException{
		ResultSetMetaData rm = rs.getMetaData();
		for(int i=1; i<=rm.getColumnCount(); i++){
			Object value = rs.getObject(i);
			String colName = rm.getColumnName(i);
			Method m = DaoUtil.setterMethodIgnoreCase(colName, bean.getClass().getMethods());
			try{
				Class<?>[] types = m.getParameterTypes();
				if(types[0]==String.class)
					value = value.toString();
				if(types[0]==Long.class)
					value = Long.parseLong(value.toString());
				if(types[0]==Short.class)
					value = Short.parseShort(value.toString());
				if(types[0]==Integer.class)
				    value = Integer.parseInt(value.toString());
				if(types[0]==Float.class)
					value = Float.parseFloat(value.toString());
				if(types[0]==Double.class)
					value = Double.parseDouble(value.toString());
				if(types[0]==Byte.class)
					value = Byte.parseByte(value.toString());
				DaoUtil.setMethod(m.getName(),bean,value);
			}catch(Exception e){
				//e.printStackTrace();
			}
		}
		return bean;
	}

	static Method setterMethodIgnoreCase(String field,Method[] methods){
		String[] fieldParts = field.split("_");
		field = fieldParts[0];
		if(fieldParts.length>1){
			for(int i=1; i<fieldParts.length; i++){
				fieldParts[i] = fieldParts[i].toLowerCase();
				fieldParts[i] = fieldParts[i].substring(0, 1).toUpperCase() +fieldParts[i].substring(1);
				field += fieldParts[i];
			}
		}
		for(int i=0;i<methods.length;i++){
			if(methods[i].getName().equalsIgnoreCase("set"+field))
				return methods[i];
		}
		return null;
	}

	static void setMethod(String methodName, Object obj, Object setValue)throws Exception {
		Class<?>[] types = new Class[] { setValue.getClass() };
		Method method = obj.getClass().getMethod(methodName, types);
		method.invoke(obj, new Object[] { setValue });
	}

	public static void mapToBeanTrans(Map<String, Object> map, Object bean) {
		for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry =  it.next();
			String key = entry.getKey().toString();
			Object value = entry.getValue();
			if (value == null)
				value = "";
			if (value.getClass().isArray()) {
				Object[] sa = (Object[]) value;
				value = sa[0];
			}
			Method m = setterMethodIgnoreCase(key, bean.getClass().getMethods());
			try {
				Class<?>[] types = m.getParameterTypes();
				if (types[0] == String.class)
					value = value.toString();
				if (types[0] == Long.class)
					value = Long.parseLong(value.toString());
				if (types[0] == Short.class)
					value = Short.parseShort(value.toString());
				if (types[0] == Integer.class)
					value = Integer.parseInt(value.toString());
				if (types[0] == Float.class)
					value = Float.parseFloat(value.toString());
				if (types[0] == Double.class)
					value = Double.parseDouble(value.toString());
				setMethod(m.getName(), bean, value);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}
	
	public static void mapToBeanTrans(Map<String, Object> map, Object bean,String prefix) {
		for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry =  it.next();
			String key = entry.getKey().toString();
			if(key.startsWith(prefix)){
				key=key.substring(prefix.length());
			}
			Object value = entry.getValue();
			if (value == null)
				value = "";
			if (value.getClass().isArray()) {
				Object[] sa = (Object[]) value;
				value = sa[0];
			}
			Method m = setterMethodIgnoreCase(key, bean.getClass().getMethods());
			try {
				Class<?>[] types = m.getParameterTypes();
				if (types[0] == String.class)
					value = value.toString();
				if (types[0] == Long.class)
					value = Long.parseLong(value.toString());
				if (types[0] == Short.class)
					value = Short.parseShort(value.toString());
				if (types[0] == Integer.class)
					value = Integer.parseInt(value.toString());
				if (types[0] == Float.class)
					value = Float.parseFloat(value.toString());
				if (types[0] == Double.class)
					value = Double.parseDouble(value.toString());
				setMethod(m.getName(), bean, value);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}
	

}
