package com.taikang.app.base.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import com.taikang.app.base.dto.UserAuthority;
import com.taikang.app.base.feign.SsoTokenClient;
import com.taikang.result.basic.commons.Result;
import com.taikang.result.basic.commons.enums.ResultEnum;
import com.taikang.result.basic.util.ResultUtils;

import lombok.extern.slf4j.Slf4j;

@WebFilter(urlPatterns = "/api/*", filterName = "ssoTokenFilter")
@Component
@Slf4j
public class SsoTokenFilter implements Filter{
	
	public static AntPathMatcher matcher = new AntPathMatcher();
	
	private @Autowired SsoTokenClient ssoTokenClient;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain)
			throws IOException, ServletException {
		log.info("SsoTokenFilter.doFilter():" + System.nanoTime());
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;
		
		String ip = getIpAddress(request);
		String uri = request.getRequestURI();
		StringBuffer url = request.getRequestURL();
		
		
		String accessToken = request.getHeader("Access-Token");
		
		log.info("请求url：" + url);
		log.info("请求URI：" + uri);
		log.info("请求IP：" + ip);
		log.info("getRemoteAddr:" + request.getRemoteAddr() + ",getRemoteHost:" 
		+ request.getRemoteHost() + ",getRemotePort:"
		+ request.getRemotePort() + ",getRemoteUser:"
		+ request.getRemoteUser()
		);
		
		//拥有token的时候验证token
		if (StringUtils.isEmptyOrWhitespaceOnly(accessToken)) {
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			response.sendRedirect("http://" + hostAddress + ":8080/ssoa/index?fromUrl=" + URLEncoder.encode("http://" + hostAddress + ":8081/index", "UTF-8"));
//				response.sendRedirect("/index");
		}else {
			log.info("token:" + accessToken);
			Result<String> checkToken = ssoTokenClient.checkToken(accessToken);
			if (checkToken.getStatus() == HttpStatus.OK.value() 
					&& checkToken.getCode().equals(ResultEnum.CHECK_TOKEN_SUCCESS.getCode())) {
				//获取权限token
				Result<JSONObject> generate = ssoTokenClient.authorityTokenGenerate(accessToken);
				String authoritiesToken = generate.getData().getString("authoritiesToken");
				
				//解析权限token
				Result<UserAuthority> resolver = ssoTokenClient.authorityTokenResolver(authoritiesToken);
				Boolean isMatcher = false;
				
				for (String authority : resolver.getData().getAuthorities()) {
					if (matcher.match(authority, request.getRequestURI())) {
						isMatcher = true;
						break;
					}
				}
				if (!isMatcher) {
					response.setContentType("application/json; charset=utf-8"); 
					PrintWriter writer = response.getWriter(); 
					writer.print(JSON.toJSONString(ResultUtils.error(ResultEnum.NO_SUCH_AUTHORITY_ERROR))); 
					writer.close(); 
					response.flushBuffer();
					return;
				}
				chain.doFilter(request, response);
			}else {
				response.setContentType("application/json; charset=utf-8"); 
				PrintWriter writer = response.getWriter(); 
				writer.print(JSON.toJSONString(ResultUtils.error(ResultEnum.AUTHORITY_NOT_EXIT_ERROR))); 
				writer.close(); 
				response.flushBuffer();
				return;
			}
		}
	}

	@Override
	public void destroy() {
		
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
