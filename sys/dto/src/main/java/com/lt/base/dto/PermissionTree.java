package com.lt.base.dto;

import java.util.List;

import jdk.jfr.Description;
import lombok.Data;

@Data
public class PermissionTree {
	
	@Description(value = "ID")
	private String id;
	
	@Description(value = "节点上的文本")
	private String text;
	
	@Description(value = "列表树节点上的图标")
	private String icon;
	
	@Description(value = "列表树节点上的图标")
	private String selectedIcon;
	
	@Description(value = "列表树节点上的图标")
	private String nodeIcon = "glyphicon glyphicon-plus";
	
	@Description(value = "结合全局enableLinks选项为列表树节点指定URL")
	private String href;
	
	@Description(value = "指定列表树的节点是否可选择")
	private Boolean selectable = true;
	
	@Description(value = "一个节点的初始状态")
	private TreeState state = new TreeState();
	
	@Description(value = "编号")
	private String code;
	
	@Description(value = "父级编号")
	private String parentCode;
	
	@Description(value = "排序")
	private int order;
	
	@Description(value = "节点的前景色，覆盖全局的前景色选项。")
	private String color;
	
	@Description(value = "节点的背景色，覆盖全局的背景色选项。")
	private String backColor;
	
	@Description(value = "通过结合全局showTags选项来在列表树节点的右边添加额外的信息")
	private String tags;
	
	private List<PermissionTree> nodes;

}
