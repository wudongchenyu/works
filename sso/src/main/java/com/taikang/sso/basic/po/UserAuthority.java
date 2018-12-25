package com.taikang.sso.basic.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import jdk.jfr.Description;
import lombok.Data;

@Data
@Entity
@Table(name = "sso_user_authority")
@Description(value = "用户权限表")
public class UserAuthority implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3058375372817669802L;

	@Id
	@GenericGenerator(name = "user_authority_uuid", strategy = "uuid")
	@GeneratedValue(generator = "user_authority_uuid")
	@Description(value = "主键")
	@Column(nullable = false, unique = true)
	private String id;
	
	@Column(name = "user_id")
	@Description(value = "用户ID")
	private String userId;
	
	@Column(name = "authority_id")
	@Description(value = "权限ID")
	private String authorityId;
	
}
