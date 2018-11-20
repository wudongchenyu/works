package com.taikang.sso.base.dto;

import java.io.Serializable;

import jdk.jfr.Description;
import lombok.Data;

@Data
public class LoginUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1778086676694896566L;

	@Description(value = "用户账号")
	private String userName;
	
	@Description(value = "姓名")
	private String name;
	
	@Description(value = "联系方式")
	private String tele;
	
	private boolean accountNonExpired;
	
	private boolean accountNonLocked;
	
	private boolean credentialsNonExpired;
	
	private boolean enabled;
	
	public LoginUser(
			String userName, 
			String name, 
			String tele,
			boolean accountNonExpired, 
			boolean accountNonLocked,
			boolean credentialsNonExpired,
			boolean enabled) {
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.name = name;
		this.tele = tele;
		this.userName = userName;
	}
	
	public LoginUser() {
		
	}

	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

}
