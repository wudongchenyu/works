package com.taikang.client.base.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.taikang.client.base.commons.Result;

import feign.Headers;

@FeignClient(name = "sso", url = "http://localhost:8080/ssoa")
public interface SsoClient {
	
	@GetMapping("/login")
	public Result<JSONObject> login(
			@RequestParam String userName, 
			@RequestParam String ip,
			@RequestParam String passWord,
			@RequestParam String remortAddress);
	
	@PostMapping("/logout")
	@Headers({"Content-Type: application/x-www-form-urlencoded;charset=utf-8"})
	public <T> Result<T> logout(@RequestParam String token);
	
	@PostMapping("/checkToken")
	@Headers({"Content-Type: application/x-www-form-urlencoded;charset=utf-8"})
	public <T> Result<T> checkToken(@RequestParam String token);

}
