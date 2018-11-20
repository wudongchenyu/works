package com.lt.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jdk.jfr.Description;
import lombok.Data;

@Data
@ApiModel
public class PermissionAddDto {
	
	@ApiModelProperty(value = "资源权限")
	@Description(value = "资源权限")
    private String resourceString;
	
	@ApiModelProperty(value = "父级编号")
	@Description(value = "父级编号")
    private String parentCode;
	
	@ApiModelProperty(value="资源名称",required = true)
	@Description(value = "资源名称")
	private String name;
	
	@ApiModelProperty(value="图标",required = true)
	@Description(value = "图标")
	private String icon;
	
	@ApiModelProperty(value="排序",required = true)
	@Description(value = "排序")
	private int order;
      
	@ApiModelProperty(value = "资源URL")
	@Description(value = "资源URL")
    private String resourceName;
      
	@ApiModelProperty(value = "资源所对应的方法名")
	@Description(value = "资源所对应的方法名")
    private String methodName;
      
	@ApiModelProperty(value = "资源所对应的包路径")
	@Description(value = "资源所对应的包路径")
    private String methodPath;
	
	@ApiModelProperty(value = "备注",required = true)
	@Description(value = "备注")
	private String remake;

}
