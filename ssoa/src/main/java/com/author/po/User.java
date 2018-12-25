package com.author.po;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.author.util.SystemGeneration;

import lombok.Data;

@Data
public class User implements Serializable{
	
	private static final long serialVersionUID = -5143951884356117635L;

	private String id;
	
	private String userCode = SystemGeneration.getTimeNumber("U");
	
	private String userName;
	
	private String name;
	
	private String pass;
	
	private String tele;
	
	private String coi;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date();
	
	private boolean accountNonExpired = true;
	
	private boolean accountNonLocked = true;
	
	private boolean credentialsNonExpired  = true;
	
	private boolean enabled  = true;

}
