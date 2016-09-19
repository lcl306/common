package com.it.common.share;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.it.common.component.log.LogPrint;
import com.it.common.component.meta.dto.SystemDto;
import com.it.common.component.security.RoleManagerImpl;

/**
 * used for javaEE filter
 * @author liu
 * */
public class AppFilter implements Filter{
	
	private List<String> passPath = new ArrayList<String>();
	
	private List<String> outPath = new ArrayList<String>();
	
	private String encoding;
	
	private boolean ignore;

	@Override
	public void destroy() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		//if(isXmlHttp(request)) encoding(request);
		if(isXmlHttp(request)) encoding(request, GlobalName.JSON_CODE);
		else encoding(request, GlobalName.CODE);
		
		if(!isPath(request, response, outPath) && isValid(request, response)){
			GlobalName.PROJECT_PATH = getRealPath(request);
			chain.doFilter(req, res);
		}else{
			if(request.getAttribute(RoleManagerImpl.EXPIRE_VALID)!=null){
				response.sendRedirect(getRegisterPage(request));
				//request.getRequestDispatcher(getRegisterPage(request)).forward(request, response);
			}else{
				goOut(request, response);
			}
		}
	}
	
	protected SystemDto getSystemDto(HttpServletRequest request){
		return (SystemDto)request.getSession().getAttribute(GlobalName.SYSTEMDTO_NAME);
	}
	
	protected boolean passRole(HttpServletRequest request){
		return GlobalName.roleManager.pass(request);
	}
	
	protected boolean nullSession(HttpServletRequest request){
		return request.getSession(false)==null;
	}
	
	private boolean isValid(HttpServletRequest request, HttpServletResponse response){
		boolean valid = true;
		if(isPath(request, response, passPath)){
			return true;
		}
		if(nullSession(request)){
			valid = false;
			LogPrint.error("============session is null.===============, url: " +request.getRequestURL());
		}else if(getSystemDto(request)==null){			
			LogPrint.error("============systemdto is null.=============, url: " +request.getRequestURL());
			valid = false;
		}else if(!passRole(request)){
			LogPrint.error("============the user could not use this menu.=============, url: " +request.getRequestURL());
			valid = false;
		}
		return valid;
	}
	
	private boolean isPath(HttpServletRequest request, HttpServletResponse response, List<String> goPath){
		String path = request.getContextPath();
		String uri = request.getRequestURI().toUpperCase();
		String project = (path!=null && path.trim().length()>1)?project = path.substring(1):null;
		for(String p: goPath){
			if(p.indexOf("*")==-1){
				if (uri.equalsIgnoreCase("/" + project.toUpperCase()+ "/"+p)){
					return true;
				}
			}else{
				p = p.replaceAll("\\*", "").toUpperCase();
				if(uri.indexOf(p)!=-1){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isXmlHttp(HttpServletRequest request){
		String requestType = request.getHeader("X-Requested-With");
		return requestType!=null && "XMLHttpRequest".equalsIgnoreCase(requestType);
	}
	
	private void goOut(HttpServletRequest request, HttpServletResponse response)throws IOException{
		String sendPage = getOutpage(request, response);
		LogPrint.info(":::SYSTEM LOGOUT:::");
		if(isXmlHttp(request)){
			PrintWriter out = response.getWriter();
			out.write(sendPage);
			out.flush();
		}else{
			response.sendRedirect(sendPage);
		}
	}
	
	public String getOutpage(HttpServletRequest request, HttpServletResponse response){
		return request.getContextPath() +"/index.jsp";
	}
	
	public String getRegisterPage(HttpServletRequest request){
		return request.getContextPath() +"/register.jsp";
	}
	
	private void encoding(HttpServletRequest request, String code) throws UnsupportedEncodingException{
		if (ignore || (request.getCharacterEncoding() == null)) {
			String encoding = this.encoding;
			if (encoding != null)
				request.setCharacterEncoding(code);
		}
	}
	
	private String getRealPath(HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath("/");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void init(FilterConfig config) throws ServletException {
		setPass(config);
		setEncoding(config);
	}
	
	private void setPass(FilterConfig config){
		LogPrint.info("PROJECT ::::::::::::::::"+ config.getServletContext().getContextPath().toUpperCase().substring(1)+ "::::::::::::::: IS STARTING SET UP...........");
		String[] arr = config.getInitParameter("pass").toUpperCase().split(",");
		for(int i=0; i<arr.length; i++){
			passPath.add(arr[i]);
		}
		String[] arr2 = config.getInitParameter("out").toUpperCase().split(",");
		for(int i=0; i<arr2.length; i++){
			outPath.add(arr2[i]);
		}
	}
	
	private void setEncoding(FilterConfig config){
		this.encoding = config.getInitParameter("encoding");
		String value = config.getInitParameter("ignore");
		if (value == null)
			this.ignore = true;
		else if (value.equalsIgnoreCase("true"))
			this.ignore = true;
		else if (value.equalsIgnoreCase("yes"))
			this.ignore = true;
		else
			this.ignore = false;
	}
	

}
