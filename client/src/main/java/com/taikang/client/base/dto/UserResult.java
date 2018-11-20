package com.taikang.client.base.dto;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import jdk.jfr.Description;
import lombok.Data;

@Data
public class UserResult implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 8079058889330519288L;

	@Description(value = "主键")
	private String id;

	@Description(value = "编号")
	private String userCode;

	@Description(value = "用户账号")
	private String userName;

	@Description(value = "姓名")
	private String name;

	@Description(value = "联系方式")
	private String tele;

	@Description(value = "身份证")
	private String coi;

	@Description(value = "创建时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date();

	@Description(value = "用户是否过期")
	private boolean accountNonExpired;

	@Description(value = "是否被锁定")
	private boolean accountNonLocked;

	@Description(value = "授权是否过期")
	private boolean credentialsNonExpired;

	@Description(value = "是否禁用")
	private boolean enabled;

}
