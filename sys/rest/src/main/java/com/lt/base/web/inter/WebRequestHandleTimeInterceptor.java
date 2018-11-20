package com.lt.base.web.inter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * 拦截器
 * @author wudon
 *
 */
@Component
@Slf4j
public class WebRequestHandleTimeInterceptor implements HandlerInterceptor{

	/**
	 * 预处理回调方法，实现处理器的预处理（如登录检查），第三个参数为响应的处理器
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Long start = System.nanoTime();
		request.setAttribute("StartTime", start);
		return true;
	}

	/**
	 * 后处理回调方法，实现处理器的后处理（但在渲染视图之前），此时我们可以通过modelAndView
	 * （模型和视图对象）对模型数据进行处理或对视图进行处理，modelAndView也可能为null。
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Long start = (Long) request.getAttribute("StartTime");
		request.removeAttribute("StartTime");
		Long end = System.nanoTime();
		log.info("本次请求" + request.getRequestURL() + "处理时间为：" + (end - start) + "ns");
		request.setAttribute("handlingTime", end - start);
	}

	/**
	 * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，如性能监控中我们可以在此记录结束时间并输出消耗时间，还可以进行一些资源清理，
	 * 类似于try-catch-finally中的finally，但仅调用处理器执行链中preHandle返回true的拦截器的afterCompletion。
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
	
}
