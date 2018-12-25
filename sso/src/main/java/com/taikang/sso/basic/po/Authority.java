package com.taikang.sso.basic.po;

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
import com.taikang.result.basic.commons.SystemGeneration;

import jdk.jfr.Description;
import lombok.Data;

@Data
@Entity
@Table(name = "sso_authority")
public class Authority implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3612578856171668026L;
	
	@Id
	@GenericGenerator(name = "authority_uuid", strategy = "uuid")
	@GeneratedValue(generator = "authority_uuid")
	@Description(value = "主键")
	@Column(nullable = false, unique = true)
	private String id;
	
	@Column(name = "authority_code")
	@Description(value = "权限编号")
	private String authorityCode = SystemGeneration.getTimeNumber("A");
	
	@Column(name = "authority_name")
	@Description(value = "权限名称")
	private String authorityName;
	
	@Column(name = "authority_url")
	@Description(value = "权限URL")
	private String authorityUrl;
	
	@Column(name = "authority_type")
	@Description(value = "权限类别")
	private String authorityType;

	@Description(value = "创建时间")
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date();
	
	@Description(value = "修改时间")
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "update_time", nullable = false)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime = new Date();
	
	@Column
	@Description(value = "是否禁用")
	private boolean enabled = true;
	
	@Column(name = "subordinate_system")
	@Description(value = "权限所属系统")
	private String subordinateSystem;
	
	@Column(name = "subordinate_app")
	@Description(value = "权限所属应用")
	private String subordinateApp;
	
	@Column(name = "subordinate_module")
	@Description(value = "权限所属模块")
	private String subordinateModule;
	
	@Column(name = "channel")
	@Description(value = "权限访问渠道")
	private String channel;
	
}
