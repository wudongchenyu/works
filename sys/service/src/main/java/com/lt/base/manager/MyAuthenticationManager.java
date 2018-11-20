package com.lt.base.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lt.base.dto.LoginUser;

@Service(value = "myAuthenticationManager")
public class MyAuthenticationManager implements AuthenticationManager{
	
	private @Autowired UserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		LoginUser userDetails = (LoginUser) userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
		
		if (authentication.getCredentials() == null) {
			return authentication;
		}

		String presentedPassword = authentication.getCredentials().toString();

		if (!new Pbkdf2PasswordEncoder().matches(presentedPassword, userDetails.getPassword())) {
			return authentication;
		}
		
		System.out.println("MyAuthenticationManager.authenticate(Authentication authentication):" +authentication.getPrincipal());
		userDetails.setPass(null);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		return authenticationToken;
	}

}
