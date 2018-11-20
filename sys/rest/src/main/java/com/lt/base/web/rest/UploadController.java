package com.lt.base.web.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.lt.base.contants.Result;
import com.lt.base.dto.TestDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "角色信息相关API")
@Controller
@RequestMapping("/upload")
public class UploadController {
	
	@ApiOperation(value = "删除角色", notes = "添加角色信息")
	@PostMapping(value="up",headers="content-type=multipart/form-data")
	public Result<Boolean> remove(@ApiParam(value="图片",required=true)MultipartFile[] files,HttpServletRequest request
			,TestDto testDto
			) {
		Result<Boolean> result = new Result<Boolean>();
		System.out.println(files[0].getOriginalFilename());
		String originalFilename = files[0].getOriginalFilename();
		System.out.println(originalFilename);
		return result;
	}

}
