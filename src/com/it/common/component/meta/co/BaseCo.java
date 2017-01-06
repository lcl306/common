package com.it.common.component.meta.co;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.ServletRequestDataBinder;

import com.it.common.component.check.group.ErrListDto;
import com.it.common.component.log.LogPrint;
import com.it.common.share.GlobalName;


/**
 * @author liu
 * */
public class BaseCo {
	
	/**
	 *  request parameters value to list of bean
	 * */
	protected static <T> void bind(HttpServletRequest request, List<T> list, Class<T> cls){
		if(list!=null){
			Field[] fs = cls.getDeclaredFields();
			if(fs!=null){
				boolean first = true;
				for(int i=0; i<fs.length; i++){
					Field f = fs[i];
					String fName = f.getName();
					String[] values = request.getParameterValues(fName);
					try{
						if(values!=null && values.length>1){
							if(first){
								for(int j=0; j<values.length; j++){
									T t = cls.newInstance();
									list.add(t);
								}
								first = false;
							}
							for(int j=0; j<values.length; j++){
								Object o = list.get(j);
								String prop = fName.replaceFirst(fName.substring(0, 1),fName.substring(0, 1).toUpperCase());
								Method m = cls.getMethod("set"+prop, new Class[]{String.class});
									m.invoke(o, new Object[]{values[j]});
							}
						}		
					}catch(Exception e){
						e.printStackTrace();
						LogPrint.error(e.getMessage());
					}
				}
			}
		}
	}
	
	/**
	 * find another servlet context named contextPath from the same tomcat, then get session from this context
	 * in tag of <Host> of tomcat's server.xml
	 * 		<Context path="contextPath"  reloadable="true" crossContext="true"></Context>
	 *      contextPath such as "/goods"
	 * dto is  BeanUtils.copyProperties( target, source ); 
	 * */
	protected static HttpSession getSessionFromContext(HttpServletRequest request, String contextPath, String sessionName){
		ServletContext context = request.getSession().getServletContext().getContext(contextPath);
		if(context!=null){
			return (HttpSession)context.getAttribute(sessionName);
		}
		return null;
	}
	
	/**
	 *  request parameters value to bean
	 * */
	protected static void bind(HttpServletRequest request, Object bean){
		ServletRequestDataBinder binder = new ServletRequestDataBinder(bean);
		binder.bind(request);
	}
	
	/**
	 *  return a image
	 * */
	protected static void toImage(HttpServletResponse response, String imageType, InputStream in)throws IOException{
		response.setContentType( "image/" +imageType);
		ServletOutputStream out = response.getOutputStream();
		byte[] buffer = new byte[GlobalName.BUFFER_SIZE];
		int len = 0;
		while((len=in.read(buffer))!=-1){
			out.write(buffer, 0, len);
		}
	}
	
	/**
	 *  return the real path
	 * */
	protected static String getRealPath(HttpServletRequest request){
		return  request.getSession().getServletContext().getRealPath(File.separator);
	}
	
	/**
	 *  return errListDto for CheckBo using
	 * */
	protected static ErrListDto getErrList(HttpServletRequest request){
		ErrListDto errList = (ErrListDto)request.getAttribute(GlobalName.ERRLISTDTO_NAME);
		if(errList==null){
			errList = new ErrListDto();
			request.setAttribute(GlobalName.ERRLISTDTO_NAME, errList);
		}
		return errList;
	}

}
