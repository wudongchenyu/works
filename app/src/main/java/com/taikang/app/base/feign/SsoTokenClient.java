package com.taikang.app.base.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.taikang.app.base.dto.UserAuthority;
import com.taikang.result.basic.commons.Result;
import com.taikang.sso.basic.dto.LoginUser;

@FeignClient(name = "sso", url = "http://localhost:8080")
public interface SsoTokenClient {
	
	@PostMapping("/ssoa/basic/token/generate")
	public Result<JSONObject> login(
			@RequestParam String userName, 
			@RequestParam String ip,
			@RequestParam String passWord,
			@RequestParam String remortAddress);
	
	@PostMapping("/ssoa/basic/token/cancel")
	public <T> Result<T> logout(@RequestParam String token);
	
	@PostMapping("/ssoa/basic/token/check")
	public Result<String> checkToken(@RequestParam String token);
	
	@PostMapping("/ssoa/basic/token/resolver")
	public Result<LoginUser> resolver(@RequestParam String token);

	@PostMapping("/ssoa/basic/authority/token/generate")
	public Result<JSONObject> authorityTokenGenerate(@RequestParam String token);
	
	@PostMapping("/ssoa/basic/authority/token/cancel")
	public Result<String> authorityTokenCancel(@RequestParam String authorityToken);
	
	@PostMapping("/ssoa/basic/authority/token/check")
	public Result<String> authorityTokenCheck(@RequestParam String authorityToken);
	
	@PostMapping("/ssoa/basic/authority/token/resolver")
	public Result<UserAuthority> authorityTokenResolver(@RequestParam String authorityToken);

}
