package com.taikang.client.base.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taikang.client.base.commons.Result;

@FeignClient(name = "sso", url = "http://localhost:8080/ssoa")
public interface SsoClient {
	
	@PostMapping("/login")
	public Result<String> login(
			@RequestParam String userName, 
			@RequestParam String ip,
			@RequestParam String passWord,
			@RequestParam String remortAddress);
	
	@PostMapping("/logout")
	public Result<String> logout(@RequestParam String token);
	
	@PostMapping("/checkToken")
	public Result<String> checkToken(@RequestParam String token);

}
