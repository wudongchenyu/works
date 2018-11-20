package com.lt.base.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jdk.jfr.Description;
import lombok.Data;

@Data
public class LoginUser implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3927031927746697097L;
	
	@Description(value = "编号")
	private String userCode;
	
	@Description(value = "姓名")
	private String name;
	
	@Description(value = "用户类型")
	private String type;
	
	@Description(value = "用户密码")
	private String pass;
	
	private boolean accountNonExpired;
	
	private boolean accountNonLocked;
	
	private boolean credentialsNonExpired;
	
	private boolean enabled;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	public LoginUser(
			String userCode, 
			String name, 
			String type, 
			String pass,
			boolean accountNonExpired, 
			boolean accountNonLocked,
			boolean credentialsNonExpired,
			boolean enabled,
			List<GrantedAuthority> authorities) {
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = authorities;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.name = name;
		this.type = type;
		this.userCode = userCode;
		this.pass = pass;
	}
	
	public LoginUser() {
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.pass;
	}

	@Override
	public String getUsername() {
		return this.userCode;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

}
