package com.taikang.app.base.feign;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taikang.app.base.dto.LoginUser;
import com.taikang.app.base.dto.UserAuthority;
import com.taikang.result.basic.commons.Result;

@Service("ssoTokenService")
public class SsoTokenService {
	
	@Value(value = "${ssoa.url}")
	private String urlPre;
	
	private @Autowired RestTemplate restTemplate;
	
	@PostMapping("/basic/token/generate")
	public Result<JSONObject> login(
			String userName, 
			String ip,
			String passWord,
			String remortAddress) {
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("userName", userName);
		params.put("ip", ip);
		params.put("passWord", passWord);
		params.put("remortAddress", remortAddress);
		Result<JSONObject> result = exchange(urlPre + "/basic/token/generate", params, JSONObject.class);
		return result;
	};
	
	@SuppressWarnings("unchecked")
	private <T> Result<T> exchange(String url,Map<String,String> params, Class<T> class1){
		T t = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //提交请求
        HttpEntity<Map<String,String>> entity = new HttpEntity<Map<String,String>>(params,headers);
        String result = restTemplate.postForObject(url,entity,String.class);
        Result<JSONObject> resultObject = JSON.parseObject(result).toJavaObject(Result.class);
        JSONObject data = resultObject.getData();
        
        if (null != data) {
        	t = data.toJavaObject(class1);
		}
        
        Result<T> result2 = new Result<T>();
        result2.setCode(resultObject.getCode());
        result2.setCodeMessage(resultObject.getCodeMessage());
        result2.setStatus(resultObject.getStatus());
        result2.setStatusMessage(resultObject.getStatusMessage());
        result2.setData(t);
        return result2;
	}
	
//	private <T> Result<T> exchange(
//			String url, 
//			Map<String, String> params) {
		
//		List<NameValuePair> list = new LinkedList<NameValuePair>();
//		params.keySet().forEach(key->{
//			list.add(new BasicNameValuePair(key, params.get(key)));
//		});
//		
//		HttpPost httpPost = new HttpPost(url);
//		HttpClient httpCient = HttpClients.createDefault();
//		UrlEncodedFormEntity formEntity = null;
//		try {
//			formEntity = new UrlEncodedFormEntity(list,"utf-8");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
//        httpPost.setEntity(formEntity);
//        HttpResponse execute = null;
//		
//        try {
//			execute = httpCient.execute(httpPost);
//			org.apache.http.HttpEntity entity = execute.getEntity();
//		    String response = EntityUtils.toString(entity, "utf-8");
//		    log.info("result:"+ response);
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
//		return null;
//		
//	}

	public Result<String> logout(String token){
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("token", URLEncoder.encode(token,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Result<String> result = exchange(urlPre + "/basic/token/cancel", params, String.class);
		return result;
	};
	
	public Result<String> checkToken(String token){
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("token", URLEncoder.encode(token,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Result<String> result = exchange(urlPre + "/basic/token/check", params, String.class);
		return result;
	};
	
	public Result<LoginUser> resolver(String token){
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("token", URLEncoder.encode(token,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Result<LoginUser> result = exchange(urlPre + "/basic/token/resolver", params, LoginUser.class);
		return result;
	};

	public Result<JSONObject> authorityTokenGenerate(
			String token, 
			String subordinateSystem, 
			String subordinateApp,
			String subordinateModule, 
			String channel){
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("token", URLEncoder.encode(token,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		params.put("subordinateSystem", subordinateSystem);
		params.put("subordinateApp", subordinateApp);
		params.put("subordinateModule", subordinateModule);
		params.put("channel", channel);
		Result<JSONObject> result = exchange(urlPre + "/basic/authority/token/generate", params, JSONObject.class);
		return result;
	};
	
	public Result<String> authorityTokenCancel(String authorityToken){
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("authorityToken", URLEncoder.encode(authorityToken,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Result<String> result = exchange(urlPre + "/basic/authority/token/cancel", params, String.class);
		return result;
	};
	
	public Result<String> authorityTokenCheck(String authorityToken){
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("authorityToken", URLEncoder.encode(authorityToken,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Result<String> result = exchange(urlPre + "/basic/authority/token/check", params, String.class);
		return result;
	};
	
	public Result<UserAuthority> authorityTokenResolver(String authorityToken){
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("authorityToken", URLEncoder.encode(authorityToken,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Result<UserAuthority> result = exchange(urlPre + "/basic/authority/token/resolver", params, UserAuthority.class);
		
		return result;
	};

	public Result<JSONObject> getToken(String code){
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		Result<JSONObject> result = exchange(urlPre + "/basic/token/acquire", params, JSONObject.class);
		return result;
	};

}
