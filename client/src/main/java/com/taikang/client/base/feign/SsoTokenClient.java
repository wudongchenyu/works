package com.taikang.client.base.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.taikang.client.base.commons.Result;
import com.taikang.client.base.dto.LoginUser;
import com.taikang.client.base.dto.UserAuthority;

@FeignClient(name = "sso", url = "http://localhost:8080/ssoa")
public interface SsoTokenClient {
	
	@PostMapping("/basic/token/generate")
	public Result<JSONObject> login(
			@RequestParam String userName, 
			@RequestParam String ip,
			@RequestParam String passWord,
			@RequestParam String remortAddress);
	
	@PostMapping("/basic/token/cancel")
	public <T> Result<T> logout(@RequestParam String token);
	
	@PostMapping("/basic/token/check")
	public <T> Result<T> checkToken(@RequestParam String token);
	
	@PostMapping("/basic/token/resolver")
	public Result<LoginUser> resolver(@RequestParam String token);

	@PostMapping("/basic/authority/token/generate")
	public Result<JSONObject> authorityTokenGenerate(@RequestParam String token);
	
	@PostMapping("/basic/authority/token/cancel")
	public Result<String> authorityTokenCancel(@RequestParam String authorityToken);
	
	@PostMapping("/basic/authority/token/check")
	public Result<String> authorityTokenCheck(@RequestParam String authorityToken);
	
	@PostMapping("/basic/authority/token/resolver")
	public Result<UserAuthority> authorityTokenResolver(@RequestParam String authorityToken);

}
