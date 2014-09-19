package com.github.bcip.schrodingersphone;

public class SPException extends Exception{
	
	private static final long serialVersionUID = 1677358969149927483L;

	public SPException(String msg){
		this.msg = msg;
	}
	
	public String getMessage(){
		return msg;
	}
	
	private String msg;
	
}
