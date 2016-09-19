package com.it.common.component.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.it.common.component.db.BaseDao;
import com.it.common.component.json.JsonUtil;
import com.it.common.component.log.LogPrint;
import com.it.common.share.GlobalName;

/**
 * @author liu
 * */
public class HttpServerUtil {
	
	/**
	 * get byte array from request
	 * */
	public static byte[] readStream(HttpServletRequest request){
		ByteArrayOutputStream out = null;
		try{
			ServletInputStream in = request.getInputStream();
			try{
				out = new ByteArrayOutputStream();
				byte[] buffer = new byte[GlobalName.BUFFER_SIZE];
				int len = 0;
				while((len=in.read(buffer))!=-1){
					out.write(buffer, 0, len);
				}
			}finally{
				out.close();
			}	
		}catch(IOException e){
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
    /////////////////////////////////////////////////request////////////////////////////////////////////////////////
	
	/**
	 * the json from request to bean
	 * @param clsMap: map contains class name of objects from bean
	 * */
	public static Object toBean(HttpServletRequest request, Class<?> cls, Map<String,Class<?>> clsMap){
		String jStr = null;
		try{
			jStr = new String(readStream(request), GlobalName.JSON_CODE);
		}catch(Exception e){
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		}
		return JsonUtil.toBean(jStr, cls, clsMap);
	}
	
	/**
	 * the json from request to bean
	 * */
	public static Object toBean(HttpServletRequest request, Class<?> cls){
		return toBean(request, cls, null);
	}
	
	/**
	 * the list of json from request to bean
	 * @param clsMap: map contains class name of objects from bean
	 * */
	public static <T> List<T> toBeans(HttpServletRequest request, Class<?> cls, Map<String,Class<?>> clsMap){
		String jStr = null;
		try{
			jStr = new String(readStream(request), GlobalName.JSON_CODE);
		}catch(Exception e){
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		}
		return JsonUtil.toBeans(jStr, cls, clsMap);
	}
	
	/**
	 * the list of json from request to bean
	 * */
	public static <T> List<T> toBeans(HttpServletRequest request, Class<?> cls){
		return toBeans(request, cls, null);
	}
	
	/////////////////////////////////////////////////response////////////////////////////////////////////////////////
	
	/**
	 * the plain text to response
	 * */
	public static void textToResponse(HttpServletResponse response, String text){
		setTextResponse(response);
		try{
			PrintWriter out = response.getWriter();
			out.write(text);
			out.flush();
		}catch(IOException e){
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * the json to response
	 * */
	public static void jsonToResponse(HttpServletResponse response, String json){
		try{
			setJsonResponse(response);
			PrintWriter out = response.getWriter();
			out.write(json);
			out.flush();
		}catch(IOException e){
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * the bean converted to json, to response
	 * */
	public static void jsonToResponse(HttpServletResponse response, Object bean){
		jsonToResponse(response, JsonUtil.toJson(bean));
	}
	
	/**
	 * the list of bean converted to json, to response
	 * */
	public static <T> void jsonToResponse(HttpServletResponse response, List<T> beans){
		jsonToResponse(response, JsonUtil.toJsons(beans));
	}	
	
	/**
	 * the json data from sql, to response
	 * */
	public static void jsonBySqlToResponse(HttpServletResponse response, String sql){
		jsonToResponse(response, BaseDao.getInstance().getJsonBysql(sql));
	}
	
	/**
	 * print {"right":"true"} to response
	 * */
	public static void trueResponse(HttpServletResponse response){
		HttpServerUtil.jsonToResponse(response, "{\"right\":\"true\"}");
	}
	
	private static void setJsonResponse(HttpServletResponse response){
		response.setContentType("application/json");
		response.setCharacterEncoding(GlobalName.CODE);
	}
	
	private static void setTextResponse(HttpServletResponse response){
		response.setContentType("text/plain");
		response.setCharacterEncoding(GlobalName.CODE);
	}

}
