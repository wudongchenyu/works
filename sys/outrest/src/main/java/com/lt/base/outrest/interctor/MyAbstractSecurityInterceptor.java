package com.lt.base.outrest.interctor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import com.lt.base.manager.MyAccessDecisionManager;
import com.lt.base.manager.MyAuthenticationManager;
import com.lt.base.outrest.config.MyFilterInvocationSecurityMetadataSource;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyAbstractSecurityInterceptor extends AbstractSecurityInterceptor implements Filter{

	@Autowired
	private MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;
	
	@Autowired
	private MyAccessDecisionManager myAccessDecisionManager;
	
	@Autowired  
    private MyAuthenticationManager myAuthenticationManager;
	
	public MyAbstractSecurityInterceptor() {
		log.info("运行:MyAbstractSecurityInterceptor");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		log.info("doFilter请求路径：" + httpServletRequest.getRequestURI());
		FilterInvocation fi = new FilterInvocation( request, response, chain );  
        invoke(fi);
	}

	private void invoke(FilterInvocation fi) throws IOException, ServletException {
		InterceptorStatusToken  token = super.beforeInvocation(fi);  
        try{  
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());  
        }finally{  
            super.afterInvocation(token, null);  
        }
	}

	@Override
	public Class<?> getSecureObjectClass() {
		super.setAuthenticationManager(myAuthenticationManager);  
        super.setAccessDecisionManager(myAccessDecisionManager);
		return FilterInvocation.class; 
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.myFilterInvocationSecurityMetadataSource;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
