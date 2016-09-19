package com.it.common.component.task;


public class Uploader implements HeavyTask {
	
	private  final String WORD_1 = "UPLOADING IN PROGRESS...";

	private  final String WORD_2 = "CHECKING IN PROGRESS...";

	private  final String WORD_3 = "MAKING IN PROGRESS...";
	
	protected int reqTimes1 = 0;

	protected int reqTimes2 = 0;

	protected int reqTimes3 = 0;

	protected int reqTimes4 = 0;
	
	protected String percent;
	
	/**
	 * initialize the progress state to empty
	 */
	protected ProgressState progressState = ProgressState.EMPTY;
	
	private long upload_file_size=0l;
	
	private long readed_file_size=0l;
	
	private long all_data_size=0l;
	
	private long insert_data_size=0l;
	
	public void calculatePercent() {
		int p = 0;
		if (getProgressState() == HeavyTask.ProgressState.EMPTY) {
			p = reqTimes1 * 1;
			if(p>5)
				p=5;
			reqTimes1++;
		}
		else if (getProgressState() == HeavyTask.ProgressState.UPLOADING) {
			p = 5 + reqTimes2 * 1;
			if(p>15)
				p=15;
			reqTimes2++;
		}
		else if (getProgressState() == HeavyTask.ProgressState.CHECKING) {
			int addValue=0;
			if(upload_file_size!=0l){
				double checkPer=Math.round(readed_file_size*1000/upload_file_size)/1000.0;
				Long l=(Math.round(checkPer*65));
				addValue=l.intValue();
			}else
				addValue=reqTimes3;
			p = 15 + addValue;
			if(p>80)
				p=80;
			reqTimes3++;
		}
		else if (getProgressState() == HeavyTask.ProgressState.MAKING) {
			int addValue=0;
			if(all_data_size!=0l){
				double checkPer=Math.round(insert_data_size*1000/all_data_size)/1000.0;
				Long l=(Math.round(checkPer*20));
				addValue=l.intValue();
			}else
				addValue=reqTimes4;
			
			p = 80 + addValue;
			if(p>100)
				p=100;
			reqTimes4++;
		}
		else if (getProgressState() == HeavyTask.ProgressState.END){
			p = 100;
		}
		if(p>100)
			p=100;
		setPercent(p + "");

	}
	
	/**
	 * return the display word in the progress
	 */
	@Override
	public String getWord() {
		if (getProgressState() == HeavyTask.ProgressState.UPLOADING)
			return WORD_1;
		else if (getProgressState() == HeavyTask.ProgressState.CHECKING)
			return WORD_2;
		else if (getProgressState() == HeavyTask.ProgressState.MAKING)
			return WORD_3;
		else if (getProgressState() == HeavyTask.ProgressState.END)
			return "COMPLETE";
		else
			return "PROGRESS";
	}
	
	/**
	 * set the task progress state
	 */
	@Override
	public void setProgressState(ProgressState state) {
		this.progressState = state;
		if (state == HeavyTask.ProgressState.UPLOADING)
			System.out.println("begin to upload data");
		else if (state == HeavyTask.ProgressState.CHECKING)
			System.out.println("begin to check data");
		else if (state == HeavyTask.ProgressState.MAKING)
			System.out.println("begin to insert data into database");
		else if (state == HeavyTask.ProgressState.END)
			System.out.println("upload over");
		else
			System.out.println("do progress");
	}
	
	/**
	 * do the task upload
	 */
	@Override
	public void run() {
		try {
			this.progressState = HeavyTask.ProgressState.READY;
			System.out.println(" now begin to upload.....");
			this.progressState = HeavyTask.ProgressState.END;
			System.out.println(" upload end .....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setUpload_file_size(long upload_file_size) {
		this.upload_file_size = upload_file_size;
	}

	public void setReaded_file_size(long readed_file_size) {
		this.readed_file_size = readed_file_size;
	}

	public void setAll_data_size(long all_data_size) {
		this.all_data_size = all_data_size;
	}

	public void setInsert_data_size(long insert_data_size) {
		this.insert_data_size = insert_data_size;
	}

	@Override
	public ProgressState getProgressState() {
		return this.progressState;
	}

	@Override
	public String getPercent() {
		return percent;
	}

	@Override
	public void setPercent(String p) {
        this.percent=p;
	}
}
