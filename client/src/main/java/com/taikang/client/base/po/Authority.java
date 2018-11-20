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
import com.taikang.client.base.commons.SystemGeneration;

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
	@GenericGenerator(name = "authority-uuid", strategy = "uuid")
	@GeneratedValue(generator = "authority-uuid")
	@Description(value = "主键")
	@Column(nullable = false, unique = true)
	private String id;
	
	@Column(name = "authority_code")
	@Description(value = "编号")
	private String authorityCode = SystemGeneration.getRandomTimeNumber("A", 5);
	
	@Column(name = "authority_name")
	@Description(value = "名称")
	private String authorityName;
	
	@Column
	@Description(value = "所属")
	private String subordinate;
	
	@Column(name = "authority_url")
	@Description(value = "URL")
	private String authorityUrl;

	@Description(value = "创建时间")
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date();
	
	@Column
	@Description(value = "是否禁用")
	private boolean enabled = true;
	
}
