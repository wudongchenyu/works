package com.lt.base.outrest.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.lt.base.entry.Permission;
import com.lt.base.web.jpa.PermissionPrimaryRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	
	private @Autowired PermissionPrimaryRepository permissionPrimaryRepository;
	
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return new ArrayList<ConfigAttribute>();
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		FilterInvocation filterInvocation = (FilterInvocation) object;  
        if (resourceMap == null) {
            loadResourceDefine();
        }
        Iterator<String> ite = resourceMap.keySet().iterator();
        HttpServletRequest requestUrl = filterInvocation.getHttpRequest();
        log.info("请求路径：" + requestUrl.getRequestURI());
        while (ite.hasNext()) {
            String resURL = ite.next();
            RequestMatcher requestMatcher = new AntPathRequestMatcher(resURL);
            if(requestMatcher.matches(requestUrl)) {
            	log.info("resourceMap.get(resURL):"+resourceMap.get(resURL));
                return resourceMap.get(resURL);
            }
        }
		return null;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}
	
	//用于加载所有权限
	@PostConstruct//被@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行,init()方法之前执行.
    private void loadResourceDefine() {//一定要加上@PostConstruct注解
    	log.info("SysUserService loadResourceDefine");
    	// 在Web服务器启动时，提取系统中的所有权限。
    	List<Permission> resources = permissionPrimaryRepository.findAll();
    	resourceMap = new HashMap<String, Collection<ConfigAttribute>>();  
		resources.forEach((Permission permission)->{
			String urlString = permission.getResourceName();
			if (!StringUtils.isEmpty(urlString)) {
				ConfigAttribute ca = new SecurityConfig(permission.getResourceString());
				if (resourceMap.containsKey(urlString)) {
					Collection<ConfigAttribute> value = resourceMap.get(urlString);  
	                value.add(ca);  
	                resourceMap.put(urlString, value);
	                log.info(urlString + "," + value);
				}else {  
	                Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();  
	                atts.add(ca);  
	                resourceMap.put(urlString, atts);  
	                log.info(urlString + "," + atts);
	            }
			}
		});
	}

}
