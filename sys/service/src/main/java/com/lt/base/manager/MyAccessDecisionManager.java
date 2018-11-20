package com.lt.base.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service(value = "myAccessDecisionManager")
@Slf4j
public class MyAccessDecisionManager implements AccessDecisionManager{
	
	public MyAccessDecisionManager() {
		log.info("MyAccessDecisionManager()");
	}

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		log.info("MyAccessDecisionManager decide()");
		 if (configAttributes == null) {
	            return;
	        }

	        Iterator<ConfigAttribute> ite = configAttributes.iterator();

	        while (ite.hasNext()) {

	            ConfigAttribute ca = ite.next();
	            String needRole = ((SecurityConfig) ca).getAttribute();
	            log.info("needRole.trim():"+needRole.trim());
	            // ga 为用户所被赋予的权限。 needRole 为访问相应的资源应该具有的权限。
	            List<String> collect = authentication.getAuthorities().parallelStream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
	            if (collect.contains(needRole.trim())) {
	            	return;
				}
	            
	            for (GrantedAuthority ga : authentication.getAuthorities()) {
	            	log.info("ga.getAuthority().trim():"+ga.getAuthority().trim());
	            	log.info("needRole.trim():"+needRole.trim());
	            	log.info("needRole.trim().equals(ga.getAuthority().trim()):" + needRole.trim().equals(ga.getAuthority().trim()));
	                if (needRole.trim().equals(ga.getAuthority().trim())) {
	                    return;
	                }
	            }
	        }
	        throw new AccessDeniedException("MyAccessDecisionManager decide 权限不足");
	}
			
	@Override
	public boolean supports(ConfigAttribute configAttribute) {
		log.info("MyAccessDecisionManager supports(ConfigAttribute configAttribute)");
		return true;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		log.info("MyAccessDecisionManager supports(Class<?> arg0)");
		return true;
	}

}
