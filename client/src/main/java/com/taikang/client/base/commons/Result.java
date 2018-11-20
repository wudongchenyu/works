package com.taikang.client.base.commons;

import jdk.jfr.Description;
import lombok.Data;

@Data
public class Result<T> {
	
	@Description(value = "请求状态")
	private Integer status;
	
	@Description(value = "请求状态说明")
	private String statusMessage;
	
	@Description(value = "业务状态")
	private Integer code;
	
	@Description(value = "业务状态说明")
	private String codeMessage;
	
	@Description(value = "返回数据")
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
