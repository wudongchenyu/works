package com.taikang.client.base.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taikang.client.base.commons.SystemGeneration;

import jdk.jfr.Description;
import lombok.Data;

@Data
@Entity
@Table(name = "sso_user")
public class User implements Serializable{
	
	private static final long serialVersionUID = -5143951884356117635L;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Description(value = "主键")
	@Column(nullable = false, unique = true)
	private String id;
	
	@Description(value = "编号")
	@Column(length = 25)
	private String userCode = SystemGeneration.getRandomTimeNumber("U", 5);
	
	@Description(value = "用户账号")
	@Column(length = 20)
	private String userName;
	
	@Description(value = "姓名")
	@Column(length = 20)
	private String name;
	
	@Description(value = "密码")
	@Column(length = 80)
	@JsonIgnore
	private String pass;
	
	@Description(value = "联系方式")
	@Column(length = 15)
	private String tele;
	
	@Description(value = "身份证")
	@Column(length = 18)
	private String coi;

	@Description(value = "创建时间")
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date();
	
	@Column
	@Description(value = "用户是否过期")
	private boolean accountNonExpired;
	
	@Column
	@Description(value = "是否被锁定")
	private boolean accountNonLocked;
	
	@Column
	@Description(value = "授权是否过期")
	private boolean credentialsNonExpired;
	
	@Column
	@Description(value = "是否禁用")
	private boolean enabled;

}
