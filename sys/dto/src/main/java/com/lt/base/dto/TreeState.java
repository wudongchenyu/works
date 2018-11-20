package com.lt.base.dto;

import jdk.jfr.Description;
import lombok.Data;

@Data
public class TreeState {
	
	@Description(value = "指示一个节点是否处于checked状态")
	private Boolean checked;
	
	@Description(value = "指示一个节点是否处于disabled状态")
	private Boolean disabled;
	
	@Description(value = "指示一个节点是否处于展开状态")
	private Boolean expanded;
	
	@Description(value = "指示一个节点是否可以被选择")
	private Boolean selected;
	
	public TreeState() {
		this.checked = false;
		this.disabled = false;
		this.expanded = false;
		this.selected = false;
	}

	public TreeState(boolean checked, boolean disabled, boolean expanded, boolean selected) {
		this.checked = checked;
		this.disabled = disabled;
		this.expanded = expanded;
		this.selected = selected;
	}

}
