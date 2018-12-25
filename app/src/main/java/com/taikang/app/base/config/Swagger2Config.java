package com.taikang.app.base.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Order(8)
@Configuration
@EnableSwagger2
@Profile(value = { "dev" })
public class Swagger2Config {
	
	public @Bean Docket createRestApi() {
		
		List<Parameter> pars = new ArrayList<Parameter>();  
		Parameter parameter = new ParameterBuilder()
			.name("Access-Token")
			.description("token /api/* 开头的接口必传")
			.modelRef(new ModelRef("string"))
			.parameterType("header") 
			.required(false)
			.build();
    	pars.add(parameter);
		
		return new Docket(DocumentationType.SWAGGER_2)
					.globalOperationParameters(pars)
					.apiInfo(apiInfo())
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.taikang.app.base.rest"))
					.paths(PathSelectors.any())
					.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
						.title("springboot利用swagger2构建文档")
						.description("简单的restful风格")
						.termsOfServiceUrl("taikang")
						.version("1.0")
						.build();
	}

}
