package com.lt.base.contants;

public enum ConstantsEnum {
	
	SYSTEM_USER(1,"SU","USER"),
	SYSTEM_ROLE(1,"SR","ROLE"),
	SYSTEM_MENU(1,"SM","MENU");
	
	private Integer type;
	private String prefix;
	private String describe;
	
	ConstantsEnum(Integer type, String prefix, String describe){
		this.type = type;
		this.prefix = prefix;
		this.describe = describe;
	}

	public Integer getType() {
		return type;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getDescribe() {
		return describe;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

}
