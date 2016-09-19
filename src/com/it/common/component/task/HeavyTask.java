package com.it.common.component.task;

import java.io.Serializable;


public interface HeavyTask extends Runnable, Serializable {	
	/**
	 * the progress bar growing speed
	 */
	public final int SPEED = 2;
	/**
	 * the task states defination or the progress bar states
	 */
	public static enum ProgressState{EMPTY,UPLOADING,CHECKING,MAKING,READY,UNZIP,BACKUP,END}	
	/**
	 * get the states of the task
	 * @return the state of the task
	 */
	public ProgressState getProgressState();
	/**
	 * set the state to the task
	 */
	public void setProgressState(ProgressState state);
	/**
	 * @return the display word
	 */
	public String getWord();
	/**
	 * @return the task complete percent
	 */
	public String getPercent();
	/**
	 * set the percent of the task
	 * @param p progress percent
	 */
	public void setPercent(String p);
	/**
	 * calculate now percent
	 */
	public void calculatePercent();
	
}