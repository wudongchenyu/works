package com.taikang.client.base.interceptor;

import java.net.InetAddress;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SsoHandlerInterceptor extends HandlerInterceptorAdapter{
	
	public static AntPathMatcher matcher = new AntPathMatcher();
	
	@Value(value = "${url.patterns}")
	private String[] patterns;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ip = getIpAddress(request);
		String uri = request.getRequestURI();
		StringBuffer url = request.getRequestURL();
		
		
		String accessToken = request.getHeader("Access-Token");
		if (!StringUtils.isEmpty(accessToken)) {
			log.info("token:" + accessToken);
		}
		log.info("请求url：" + url);
		log.info("请求URI：" + uri);
		log.info("请求IP：" + ip);
		log.info("getRemoteAddr:" + request.getRemoteAddr() + ",getRemoteHost:" 
		+ request.getRemoteHost() + ",getRemotePort:"
		+ request.getRemotePort() + ",getRemoteUser:"
		+ request.getRemoteUser()
		);
		
		Boolean isMatcher = false;
		
		for (String pattern : patterns) {
			if (matcher.match(pattern, request.getRequestURI())) {
				isMatcher = true;
				break;
			}
			
		}
		if (isMatcher) {
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println(InetAddress.getLocalHost().getHostAddress());
			response.sendRedirect("http://" + hostAddress + ":8080/ssoa/index?fromUrl=" + URLEncoder.encode(url.toString(), "UTF-8"));
			return true;
		}
		
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}
	
	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}
	
	public String getIpAddress(HttpServletRequest request){      
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getHeader("Proxy-Client-IP");      
        }      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getHeader("WL-Proxy-Client-IP");      
        }      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getHeader("HTTP_CLIENT_IP");      
        }      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");      
        }      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getRemoteAddr();      
        }      
        return ip;      
    }

}
