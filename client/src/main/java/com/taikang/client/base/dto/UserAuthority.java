package com.taikang.client.base.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class UserAuthority implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3058375372817669802L;

	private String userId;
	
	private List<String> authorities;
	
	private Long issueTime = System.nanoTime();
	
}
