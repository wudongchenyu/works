package com.lt.base.entry;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import com.alibaba.fastjson.annotation.JSONField;
import com.lt.base.common.SystemGeneration;

import jdk.jfr.Description;
import lombok.Data;

@Data
@Entity
@Table(name = "ibug_resource")
public class Permission implements GrantedAuthority{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5095092483106249013L;

	@Id
	@GenericGenerator(name = "sruid", strategy = "uuid")
	@GeneratedValue(generator = "sruid")
	@Column(name = "id",nullable = false,unique=true)
	@Description(value = "主键")
	private String id;
	
	@Column(name="name",length=20)
	@Description(value = "资源名称")
	private String name;
	
	@Description(value = "资源权限")
	@Column(name="resource_string",length=100)
    private String resourceString;
	
	@Column(name="icon",length=100)
	@Description(value = "图标")
	private String icon;
	
	@Column(name="resource_order",length=4)
	@Description(value = "排序")
	private int resourceOrder;
      
	@Description(value = "编号")
    @Column(name="resource_code",length=20,nullable = false, unique = true)  
    private String resourceCode = SystemGeneration.getTimeNumber("RS");
	
	@Description(value = "父级编号")
    @Column(name="parent_code",length=20)  
    private String parentCode;
      
	@Description(value = "资源URL")
    @Column(name="resource_name",length=100)  
    private String resourceName;
      
	@Description(value = "资源所对应的方法名")
    @Column(name="method_name",length=40)  
    private String methodName;
      
	@Description(value = "资源所对应的包路径")
    @Column(name="method_path",length=50)  
    private String methodPath;
    
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
	
	@Transient
	private String authority;
	
	@Transient
	private List<Permission> childs;
	
	@Override
	public String getAuthority() {
		return resourceString;
	}

}
