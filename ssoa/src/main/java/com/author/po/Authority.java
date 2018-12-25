package com.author.po;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.author.util.SystemGeneration;

import lombok.Data;

@Data
public class Authority implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3612578856171668026L;
	
	private String id;
	
	private String authorityCode = SystemGeneration.getTimeNumber("A");
	
	private String authorityName;
	
	private String authorityUrl;
	
	private String authorityType;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date();
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime = new Date();
	
	private boolean enabled = true;
	
	private String subordinateSystem;
	
	private String subordinateApp;
	
	private String subordinateModule;
	
	private String channel;
	
}
