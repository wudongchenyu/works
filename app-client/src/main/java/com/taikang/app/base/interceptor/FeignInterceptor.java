package com.taikang.app.base.interceptor;

import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignInterceptor implements RequestInterceptor{

	@Override
	public void apply(RequestTemplate template) {
		System.out.println("调用FeignInterceptor.apply");
		template.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	}

}
