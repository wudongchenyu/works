package com.lt.base.outrest.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lt.base.service.JwtTokenService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{

	private @Autowired JwtTokenService jwtTokenService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		PathMatcher matcher = new AntPathMatcher();
		String urlString = request.getRequestURI();
		if (!matcher.match("/login", urlString)) {
			log.info("JwtAuthenticationTokenFilter");
			String authHeader = request.getHeader("Authorization");
	        String tokenHead = "Bearer ";
	        log.info("token:" + authHeader);
	        if (authHeader != null && authHeader.startsWith(tokenHead)) {
	            String authToken = authHeader.substring(tokenHead.length());
            	String username = jwtTokenService.getUsernameFromToken(authToken);
	            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	                UserDetails userDetails = jwtTokenService.getUserFromToken(authToken);
	                if (jwtTokenService.validateToken(authToken, userDetails)) {
	                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
	                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                    SecurityContextHolder.getContext().setAuthentication(authentication);
	                }
	            }
	        }
		}
        filterChain.doFilter(request, response);
	}

}
