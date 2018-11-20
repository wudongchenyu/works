package com.author.po;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import jdk.jfr.Description;
import lombok.Data;

@Data
public class User implements Serializable{
	
	private static final long serialVersionUID = -5143951884356117635L;

	private String id;
	
	@Description(value = "编号")
	private String userCode;
	
	@Description(value = "用户账号")
	private String userName;
	
	@Description(value = "姓名")
	private String name;
	
	@Description(value = "密码")
	private String pass;
	
	@Description(value = "联系方式")
	private String tele;
	
	@Description(value = "身份证")
	private String coi;

	@Description(value = "创建时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@Description(value = "用户是否过期")
	private boolean accountNonExpired;
	
	@Description(value = "是否被锁定")
	private boolean accountNonLocked;
	
	@Description(value = "授权是否过期")
	private boolean credentialsNonExpired;
	
	@Description(value = "是否禁用")
	private boolean enabled;

}
