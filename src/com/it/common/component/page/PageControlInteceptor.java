package com.it.common.component.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class PageControlInteceptor implements HandlerInterceptor {
	
	
	//Action之前执行:进行分页处理
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object arg2) throws Exception {
		if (request.getParameter("currPage") != null&&request.getParameter("currPage").trim().length()!=0) {
			int currPage = Integer.parseInt(request.getParameter("currPage"));
			PageUtil.setCurrentPageNumber(currPage);
		}else{
			PageUtil.remove();
		}
		if (request.getParameter("pageRows") != null&&request.getParameter("pageRows").trim().length()!=0) {
			int pageRows = Integer.parseInt(request.getParameter("pageRows"));
			PageUtil.setPageSize(pageRows);
		}
		return true;
	}
	
	//生成视图之前执行:有机会修改ModelAndView； 
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,Object arg2, ModelAndView arg3) throws Exception {
		//System.out.println("before return view ");
	}
	
	//最后执行，可用于释放资源:可以根据ex是否为null判断是否发生了异常，进行日志记录。
	@Override
	public void afterCompletion(HttpServletRequest arg0,HttpServletResponse arg1, Object arg2, Exception arg3)throws Exception {
		//System.out.println("last excute to release resource ");
	}
	
	//分别实现预处理、后处理（调用了Service并返回ModelAndView，但未进行页面渲染）、返回处理（已经渲染了页面）

}
