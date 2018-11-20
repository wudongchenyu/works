package com.lt.base.outrest.web;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestMain {

	public static void main(String[] args) {
		String s = "{\"accountNonExpired\":true,\"accountNonLocked\":true,\"authorities\":[{\"authority\":\"ROLE_ADD_USER\"},{\"authority\":\"ROLE_ADMIN\"},{\"authority\":\"ROLE_ALL_RESOURCE\"},{\"authority\":\"ROLE_ALL_TREE_RESOURCE\"},{\"authority\":\"ROLE_CENTER\"},{\"authority\":\"ROLE_INCREASE_RESOURCE\"},{\"authority\":\"ROLE_INCREAS_EROLE\"},{\"authority\":\"ROLE_LOGOUT\"},{\"authority\":\"ROLE_REMOVE_RESOURCE\"},{\"authority\":\"ROLE_REMOVE_ROLE\"},{\"authority\":\"ROLE_SHOW\"},{\"authority\":\"ROLE_USER_LIST\"}],"
				+ "\"credentialsNonExpired\":true,\"enabled\":true,\"password\":\"8211ee9afe0c05dabfdf021a642b91bfe68646bd34981a658da2c5b0353232ffa439129b3619d9c7\","
				+ "\"username\":\"U20180621125302815\"}";
		JSONObject parseObject = JSON.parseObject(s);
		List<GrantedAuthority>  errors = JSONObject.parseArray(parseObject.get("authorities").toString(), GrantedAuthority.class);
		UserDetails user = new User(parseObject.getString("username"), 
				parseObject.getString("password"), 
				parseObject.getBoolean("enabled"), 
				parseObject.getBoolean("accountNonExpired"), 
				parseObject.getBoolean("credentialsNonExpired"), 
				parseObject.getBoolean("accountNonLocked"), 
				errors);
		System.out.println(user.getAuthorities().size());
	}

}
