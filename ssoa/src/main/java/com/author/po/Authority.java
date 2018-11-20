package com.author.po;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import jdk.jfr.Description;
import lombok.Data;

@Data
public class Authority implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3612578856171668026L;
	
	private String id;
	
	@Description(value = "编号")
	private String authorityCode;
	
	@Description(value = "名称")
	private String authorityName;
	
	@Description(value = "所属")
	private String subordinate;
	
	@Description(value = "名称")
	private String authorityUrl;

	@Description(value = "创建时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@Description(value = "是否禁用")
	private boolean enabled;
	
	@Description(value = "是否是菜单")
	private boolean isMenu;

}
