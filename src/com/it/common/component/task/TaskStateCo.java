package com.it.common.component.task;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class TaskStateCo{
	
	@RequestMapping(value="/taskState/taskState.progressBarValue.do")
	public void progressBarValue(HttpServletRequest req, HttpServletResponse resp){
		HeavyTask task =TaskUtil.getTask(req);
		resp.setContentType("text/Xml;charset=utf-8");   
		PrintWriter out;
		try {
			out = resp.getWriter();
			if (task != null) {
				HeavyTask.ProgressState state = task.getProgressState();			
				if(state==HeavyTask.ProgressState.END)
					TaskUtil.removeTask(req);
				task.calculatePercent();
				String percent = task.getPercent();
			    out.println(percent);
			}else{
				out.println("100");		
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

	
}
