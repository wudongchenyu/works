package com.lt.base.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.lt.base.dto.LoginUser;

public class UserUtils {
	
	public static LoginUser getUserDetails() {
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();
		if (principal instanceof String) {
			return null;
		}
		
		LoginUser userDetails = (LoginUser) principal;
		return userDetails;
	}
	
	public static String getUserCode() {
		LoginUser userDetails = getUserDetails();
		if (userDetails == null) {
			return null;
		}
		return getUserDetails().getUsername();
	}
	
	public static Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<? extends GrantedAuthority> authorities = getUserDetails().getAuthorities();
		return authorities;
	}

}
