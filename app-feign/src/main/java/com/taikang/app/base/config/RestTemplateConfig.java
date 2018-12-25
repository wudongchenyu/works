package com.taikang.app.base.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Configuration
public class RestTemplateConfig {
	
	public @Bean HttpClientConnectionManager connectionManager() {
		
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		
		//整个连接池的并发
		connectionManager.setMaxTotal(50);
		
		//每个主机的并发
		connectionManager.setDefaultMaxPerRoute(50);
		return connectionManager;
	}
	
	public @Bean HttpRequestRetryHandler retryHandler() {
		//开启重试 重试次数/激活请求重试
		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(2, true);
		return retryHandler;
	}
	
	public @Bean List<Header> defaultHeaders (){
		BasicHeader acceptEncoding = new BasicHeader("Accept-Encoding", "gzip,deflate");
		BasicHeader acceptLanguage = new BasicHeader("Accept-Language", "zh-CN");
		List<Header> list = new ArrayList<>();
		list.add(acceptEncoding);
		list.add(acceptLanguage);		
		return list;
	}
	
	public @Bean HttpClientBuilder httpClientBuilder() {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setConnectionManager(connectionManager());
		httpClientBuilder.setRetryHandler(retryHandler());
		httpClientBuilder.setDefaultHeaders(defaultHeaders());
		return httpClientBuilder;
	}
	
	public @Bean CloseableHttpClient httpClient() {
	    CloseableHttpClient httpClient = httpClientBuilder().build();
	    return httpClient;
	}
	
	public @Bean RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setMessageConverters(messageConverters());
		restTemplate.setRequestFactory(requestFactory());
		return restTemplate;
	}
	
	public @Bean List<HttpMessageConverter<?>> messageConverters() {
		List<HttpMessageConverter<?>> list = new ArrayList<>();
		list.add(stringHttpMessageConverter());
		list.add(fastJsonHttpMessageConverter());
		list.add(resourceHttpMessageConverter());
		list.add(allEncompassingFormHttpMessageConverter());
		list.add(formHttpMessageConverter());
		return list;
	}
	
	public @Bean StringHttpMessageConverter stringHttpMessageConverter() {
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
		List<MediaType> types = new ArrayList<>();
		types.add(MediaType.APPLICATION_JSON_UTF8);
		types.add(MediaType.TEXT_HTML);
		types.add(MediaType.APPLICATION_FORM_URLENCODED);
		stringHttpMessageConverter.setSupportedMediaTypes(types);
		return stringHttpMessageConverter;
	}
	
	public @Bean FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		List<MediaType> types = new ArrayList<>();
		types.add(MediaType.APPLICATION_JSON_UTF8);
		types.add(MediaType.TEXT_HTML);
		types.add(MediaType.APPLICATION_FORM_URLENCODED);
		fastJsonHttpMessageConverter.setSupportedMediaTypes(types);
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setCharset(Charset.forName("UTF-8"));
		fastJsonConfig.setSerializerFeatures(
				//是否输出值为null的字段,默认为false
				SerializerFeature.WriteMapNullValue,
				//输出key时是否使用双引号,默认为true
				SerializerFeature.QuoteFieldNames);
		fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		return fastJsonHttpMessageConverter;
	}
	
	public @Bean ResourceHttpMessageConverter resourceHttpMessageConverter(){
		return new ResourceHttpMessageConverter();
	}
	
	public @Bean AllEncompassingFormHttpMessageConverter allEncompassingFormHttpMessageConverter(){
		return new AllEncompassingFormHttpMessageConverter();
	}
	
	public @Bean FormHttpMessageConverter formHttpMessageConverter(){
		return new FormHttpMessageConverter();
	}
	
	public @Bean HttpComponentsClientHttpRequestFactory requestFactory(){
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient());
		requestFactory.setConnectionRequestTimeout(5000);
		requestFactory.setConnectTimeout(60000);
		requestFactory.setReadTimeout(60000);
		return requestFactory;
	}

}
