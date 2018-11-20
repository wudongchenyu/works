package com.lt.base.web.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service(value = "successHandler")
@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	private HttpSessionRequestCache httpSessionRequestCache = new HttpSessionRequestCache();
	
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	public LoginSuccessHandler() {
		log.info("LoginSuccessHandler");
	}
	
	@Override
	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("LoginSuccessHandler handle");
		super.handle(request, response, authentication);
	}
	
	@Override
	@Transactional(readOnly=false,propagation= Propagation.REQUIRED,rollbackFor={Exception.class})
    public void onAuthenticationSuccess(
    		HttpServletRequest request,    
            HttpServletResponse response, 
            Authentication authentication) throws IOException,ServletException {
		log.info("LoginSuccessHandler onAuthenticationSuccess");
        //获得授权后可得到用户信息   可使用SUserService进行数据库操作  
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //输出登录提示信息    
//      log.info("管理员 " + userDetails.getUsername() + " 登录");    
        log.info("IP :"+getIpAddress(request));  
        SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest == null) {
			super.onAuthenticationSuccess(request, response, authentication);

			return;
		}
		String targetUrlParameter = getTargetUrlParameter();
		log.info("targetUrlParameter:"+targetUrlParameter);
		if (isAlwaysUseDefaultTargetUrl()
				|| (targetUrlParameter != null && StringUtils.hasText(request
						.getParameter(targetUrlParameter)))) {
			requestCache.removeRequest(request, response);
			super.onAuthenticationSuccess(request, response, authentication);

			return;
		}

		clearAuthenticationAttributes(request);

		// Use the DefaultSavedRequest URL
		String targetUrl = savedRequest.getRedirectUrl();
		log.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
//		request.getRequestDispatcher("/a/").forward(request, response);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
        //super.onAuthenticationSuccess(request, response, authentication);    
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

	public HttpSessionRequestCache getHttpSessionRequestCache() {
		return httpSessionRequestCache;
	}

	public void setHttpSessionRequestCache(HttpSessionRequestCache httpSessionRequestCache) {
		this.httpSessionRequestCache = httpSessionRequestCache;
	}
	
	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

}
