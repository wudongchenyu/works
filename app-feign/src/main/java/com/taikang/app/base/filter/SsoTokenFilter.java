package com.taikang.app.base.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import com.taikang.app.base.dto.UserAuthority;
import com.taikang.app.base.feign.SsoTokenService;
import com.taikang.result.basic.commons.Result;
import com.taikang.result.basic.commons.enums.ResultEnum;
import com.taikang.result.basic.util.ResultUtils;

import lombok.extern.log4j.Log4j2;

@Component
//@WebFilter(urlPatterns = "/api/*", filterName = "ssoTokenFilter")
@Log4j2
public class SsoTokenFilter implements Filter{
	
	public static AntPathMatcher matcher = new AntPathMatcher();
	
	private @Autowired SsoTokenService ssoTokenService;
	
	@Value(value = "${ssoa.url}")
	private String ssoaUrl;
	
	@Value(value = "${app.from.url}")
	private String fromUrl;
	
	@Value(value = "${app.subordinate.app}")
	private String subordinateApp;
	
	@Value(value = "${app.subordinate.system}")
	private String subordinateSystem;
	
	@Value(value = "${app.subordinate.module}")
	private String subordinateModule;
	
	@Value(value = "${app.subordinate.channel}")
	private String channel;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;
		log.info("SsoTokenFilter.doFilter():" + System.nanoTime());
		
		
		String ip = getIpAddress(request);
		String uri = request.getRequestURI();
		StringBuffer url = request.getRequestURL();
		
		
		String accessToken = request.getHeader("Access-Token");
		String authoritiesToken = request.getHeader("Authority-Token");
		log.info("accessToken:" + accessToken);
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
			log.info("没有token，跳转到登录页");
			response.sendRedirect(ssoaUrl + "/index?fromUrl=" + URLEncoder.encode(fromUrl, "UTF-8"));
		}else {
			log.info("token:" + accessToken);
			log.info("有token检验token");
			Result<String> checkToken = ssoTokenService.checkToken(accessToken);
			if (null != checkToken && checkToken.getStatus() == HttpStatus.OK.value() 
					&& checkToken.getCode().equals(ResultEnum.CHECK_TOKEN_SUCCESS.getCode())) {
				log.info("检验token成功，验证权限");
				
				if (StringUtils.isEmptyOrWhitespaceOnly(authoritiesToken)) {
					log.info("没有权限token，获取权限token");
					Result<JSONObject> generate = ssoTokenService.authorityTokenGenerate(accessToken, subordinateSystem, subordinateApp, subordinateModule, channel);
					if (generate.getStatus() == HttpStatus.OK.value()
							&& generate.getCode().equals(ResultEnum.GENERATE_TOKEN_SUCCESS.getCode())) {
						log.info("获取权限token成功");
						authoritiesToken = generate.getData().getString("authoritiesToken");
					}else {
						log.info("获取权限token失败");
						this.setResponse(response, ResultEnum.SEARCH_AUTHORTY_ERROR);
					}
				}
				
				if (StringUtils.isEmptyOrWhitespaceOnly(authoritiesToken)) {
					log.info("权限token不存在");
					this.setResponse(response, ResultEnum.NO_SUCH_AUTHORITY_ERROR);
					return;
				}
				
				//解析权限token
				Result<UserAuthority> result = ssoTokenService.authorityTokenResolver(authoritiesToken);
				Boolean isMatcher = false;
				
				//解析失败
				if (null == result || checkToken.getStatus() != HttpStatus.OK.value() 
						|| !checkToken.getCode().equals(ResultEnum.RESOLVER_TOKEN_SUCCESS.getCode())) {
					log.info("解析权限token失败");
					this.setResponse(response, ResultEnum.RESOLVER_TOKEN_ERROR);
					return;
				}
				
				log.info("验证权限");
				List<String> authorities = result.getData().getAuthorities();
				for (String authority : authorities) {
					if (matcher.match(authority, request.getRequestURI())) {
						isMatcher = true;
						break;
					}
				}
				if (!isMatcher) {
					log.info("没有权限");
					this.setResponse(response, ResultEnum.NO_SUCH_AUTHORITY_ERROR);
					return;
				}
				log.info("权限验证通过");
				chain.doFilter(request, response);
			}else {
				this.setResponse(response, ResultEnum.AUTHORITY_NOT_EXIT_ERROR);
				return;
			}
		}
	}

	private void setResponse(HttpServletResponse response, ResultEnum resultEnum) throws IOException {
		response.setContentType("application/json; charset=utf-8"); 
		PrintWriter writer = response.getWriter(); 
		writer.print(JSON.toJSONString(ResultUtils.error(resultEnum))); 
		writer.close(); 
		response.flushBuffer();
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
