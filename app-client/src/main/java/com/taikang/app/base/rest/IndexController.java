package com.taikang.app.base.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import com.taikang.app.base.feign.SsoTokenClient;
import com.taikang.result.basic.commons.Result;
import com.taikang.result.basic.commons.enums.ResultEnum;
import com.taikang.result.basic.util.ResultUtils;

import io.swagger.annotations.Api;

@Api(tags = "首页")
@Controller
public class IndexController {
	
	private @Autowired SsoTokenClient ssoTokenClient;
	
	@GetMapping("/index")
	public String index() {
		return "index";
	}
	
	@GetMapping("/center")
	public String center(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String code = request.getParameter("code");
		if (StringUtils.isEmptyOrWhitespaceOnly(code)) {
			request.setAttribute("result", JSON.toJSONString(ResultUtils.error(ResultEnum.CODE_ERROR)));
		    return "center";
		}
		Result<JSONObject> token = ssoTokenClient.getToken(code);
		String result = JSON.toJSONString(token);
		System.out.println("result:" + result);
		request.setAttribute("result", result.replace("\"", "'"));
		return "center";
	}
	
}
