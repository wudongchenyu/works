package com.taikang.app.base.commons;

import lombok.Data;

@Data
public class Result<T> {
	
	private Integer status;
	
	private String statusMessage;
	
	private Integer code;
	
	private String codeMessage;
	
	private T data;
	
	public Result(){
		
	}
	
	public Result(Integer status){
		this(status, null);
	}
	
	public Result(Integer status, String statusMessage){
		this(status, statusMessage, null);
	}
	
	public Result(Integer status, String statusMessage, Integer code){
		this(status, statusMessage, code, null);
	}
	
	public Result(Integer status, String statusMessage, Integer code, String codeMessage){
		this(status, statusMessage, code, codeMessage, null);
	}
	
	public Result(Integer status, String statusMessage, Integer code, String codeMessage, T data){
		this.status = status;
		this.statusMessage = statusMessage;
		this.code = code;
		this.codeMessage = codeMessage;
		this.data = data;
	}

}
