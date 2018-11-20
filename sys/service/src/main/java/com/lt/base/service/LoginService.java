package com.lt.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lt.base.dto.LoginUser;
import com.lt.base.manager.MyAuthenticationManager;

@Service
public class LoginService {
	
	private @Autowired JwtTokenService jwtTokenService;
	
	private @Autowired MyAuthenticationManager myAuthenticationManager;
	
	public String generateToken(String userCode, String pass) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userCode, pass);
		Authentication authenticate = myAuthenticationManager.authenticate(authentication);
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		if (authenticate.getPrincipal() instanceof LoginUser) {
			String generateToken = jwtTokenService.generateToken((LoginUser) authenticate.getPrincipal());
			return generateToken;
		}
		return null;
		
	}

}
