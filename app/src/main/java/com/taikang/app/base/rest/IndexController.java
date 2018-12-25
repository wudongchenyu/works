package com.taikang.app.base.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;

@Api(tags = "首页")
@Controller
public class IndexController {
	
	@GetMapping("/index")
	public String test() {
		return "index";
	}

}
