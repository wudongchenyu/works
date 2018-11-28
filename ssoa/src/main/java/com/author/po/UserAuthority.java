package com.author.po;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserAuthority implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3058375372817669802L;

	private String id;
	
	private String userId;
	
	private String authorityId;
	
}
