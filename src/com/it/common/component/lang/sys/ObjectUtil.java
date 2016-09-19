package com.it.common.component.lang.sys;

import java.lang.reflect.Method;

import com.it.common.component.lang.str.StrUtil;
import com.it.common.component.log.LogPrint;

public class ObjectUtil {
	
	public static Object getValue(Object o, String fieldName){
		Object value = null;
		if(o!=null){
			Class<?> cls = o.getClass();
			try {
				String fName = cls.getDeclaredField(fieldName).getName();
				String methodName = "get"+StrUtil.firstUpperCase(fName);
				Method m = cls.getMethod(methodName, new Class<?>[]{});
				value = m.invoke(o, new Object[]{});
			} catch (Exception e) {
				LogPrint.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return value;
	}
	
	public static boolean isEmpty(Object o, String[] fields){
		boolean empty = true;
		for(String f : fields){
			if(StrUtil.isNotEmpty(ObjectUtil.getValue(o, f))){
				empty = false;
				break;
			}
		}
		return empty;
	}

}
