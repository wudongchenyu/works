package com.taikang.app.base.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginUser implements Serializable{

	private static final long serialVersionUID = 1914386498859816665L;

	private String ip;
	
	private String userName;
	
	private String userId;
	
	private String name;
	
	private Long issueTime = System.nanoTime();
	
	private String remortAddress;
}
