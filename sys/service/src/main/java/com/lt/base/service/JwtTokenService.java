package com.lt.base.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lt.base.dto.LoginUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtTokenService {
	
	 /**
     * 密钥
     */
    private static final String secret = "lt-azc";

    private String generateToken(Claims claims) {
        return Jwts
        		.builder()
        		.setClaims(claims)
        		.signWith(SignatureAlgorithm.HS512, secret)
        		.compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts
            		.parser()
            		.setSigningKey(secret)
            		.parseClaimsJws(token)
            		.getBody();
        } catch (Exception e) {
        	e.printStackTrace();
            claims = null;
        }
        return claims;
    }

    /**
     * 生成令牌
     *
     * @param userDetails 用户
     * @return 令牌
     */
    public String generateToken(LoginUser userDetails) {
    	  Claims setSubject = Jwts.claims()
    			//token的发行者
    			.setIssuer("lt")
	    	    //token的客户
	    	  	.setAudience(userDetails.getUsername())
	    	  	//经常使用的，以数字时间定义失效期，也就是当前时间以后的某个时间本token失效。30分钟
	    	  	.setExpiration(new Date(System.currentTimeMillis() + 30*60*1000))
	    	  	//JWT唯一标识. 能用于防止 JWT重复使用，一次只用一个token
	    	  	.setId(userDetails.getUsername())
	    	  	//JWT发布时间，能用于决定JWT年龄
	    	  	.setIssuedAt(new Date())
    	  	
	    	  	//定义在此时间之前，JWT不会接受处理。
	    	  	.setNotBefore(new Date(System.currentTimeMillis() - 1000))
	    	  	//token的题目JSON.toJSONString(userDetails)
	    	  	.setSubject(userDetails.getUsername());
    	setSubject.put("user", JSON.toJSONString(userDetails));
        return generateToken(setSubject);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }
    
    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public UserDetails getUserFromToken(String token) {
    	LoginUser user;
        try {
            Claims claims = getClaimsFromToken(token);
            String userString = claims.get("user").toString();
            log.info("从token中获取用户：" + userString);
            JSONObject parseObject = JSON.parseObject(userString);
            System.out.println(parseObject.get("authorities").toString());
    		JSONArray  authorities = JSONObject.parseArray(parseObject.get("authorities").toString());
    		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
    		
    		authorities.forEach(i->{
    			JSONObject jsonObject = (JSONObject) i;
    			list.add(new SimpleGrantedAuthority(jsonObject.getString("authority")));
    		});
    		
    		user = new LoginUser(
    				parseObject.getString("userCode"),
    				parseObject.getString("name"),
    				parseObject.getString("type"),
    				parseObject.getString("pass"), 
    				parseObject.getBoolean("enabled"), 
    				parseObject.getBoolean("accountNonExpired"), 
    				parseObject.getBoolean("credentialsNonExpired"), 
    				parseObject.getBoolean("accountNonLocked"), 
    				list);
        } catch (Exception e) {
        	e.printStackTrace();
        	user = null;
        }
        return user;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.setIssuedAt(new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证令牌
     *
     * @param token       令牌
     * @param userDetails 用户
     * @return 是否有效
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        log.info("验证token用户：" + username);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
}
