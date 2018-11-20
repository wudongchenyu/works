package com.lt.base.entry;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;
import com.lt.base.common.SystemGeneration;

import jdk.jfr.Description;
import lombok.Data;

@Data
@Entity
@Table(name = "ibug_role")
public class Role {
	
	@Id
	@GenericGenerator(name = "suid", strategy = "uuid")
	@GeneratedValue(generator = "suid")
	@Description(value = "主键")
	@Column(nullable = false, unique = true)
	private String id;
	
	@Description(value = "编号")
	@Column(length = 20,nullable = false, unique = true)
	private String roleCode = SystemGeneration.getTimeNumber("R");
	
	@Description(value = "角色名")
	@Column(length = 20)
	private String name;
	
	@ManyToMany(fetch = FetchType.EAGER,cascade={CascadeType.REFRESH,CascadeType.MERGE})
	@JoinTable(name = "ibug_roleresource",joinColumns = {@JoinColumn(name = "roleid", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "resourceid", referencedColumnName = "id"
				)})
	private Set<Permission> resources;
	
	@Description(value = "创建时间")
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false)
	@JSONField(format = "yyyy-MM-dd")
	private Date createTime = new Date();
	
	@Description(value = "是否删除")
	@Column(length = 1)
	private String flag = "0";
	
	@Description(value = "备注")
	@Column(length = 1000)
	private String remake;

	@Description(value = "是否禁用")
	@Column(length = 1)
	private String disable = "0";

}
