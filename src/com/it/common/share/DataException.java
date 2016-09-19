package com.it.common.share;

/**
 * used for app
 * @author liu
 * */
public class DataException extends Exception{

	private static final long serialVersionUID = 3936909818892782281L;
	
	public DataException(String info){
		super(info);
	}

	public DataException(){
		super();
	}

}
