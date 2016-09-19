package com.it.common.component.task;

import javax.servlet.http.HttpServletRequest;

public class TaskUtil {
	
	
	
	public static HeavyTask getUploader(HttpServletRequest request){
		HeavyTask task=getTask(request);
    	if(task==null){
    		task=new Uploader();
    		request.getSession().setAttribute("_heavyTask",task);
    	}
    	return task;
    }
	
	public static HeavyTask getTask(HttpServletRequest request){
		HeavyTask task=(HeavyTask) request.getSession().getAttribute("_heavyTask");
    	return task;
    }
	
	public static void removeTask(HttpServletRequest request){
		request.getSession().removeAttribute("_heavyTask");
    }
}
