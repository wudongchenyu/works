package com.taikang.sso.basic.dto;

import java.io.Serializable;
import java.util.List;

import jdk.jfr.Description;
import lombok.Data;

@Data
public class UserAuthorityToken implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4184421939729534245L;
	
	@Description(value = "用户ID")
	private String userId;
	
	@Description(value = "权限集合")
	private List<String> authorities;
	
}
