package com.author.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.author.util.RedisUtils;
import com.author.util.SystemGeneration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import redis.clients.jedis.Jedis;

public class TestMain {

	public static void main(String[] args) {
		IntStream.range(0, 30).forEach(i->System.out.println(createToken()));
	}

	public static void deleteKeys() {
		Jedis jedis = RedisUtils.openJedis();
		Set<String> set = jedis.keys("token_*,user_lisi");
		for (String string : set) {
			System.out.println(string);
			jedis.del(string);
		}
	}

	public static String createToken() {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", "123qweasd");
		String token = Jwts.builder().setClaims(map).setId(SystemGeneration.getUuidNumber("")).setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + 6000)).setSubject("strUserId")
				.signWith(SignatureAlgorithm.HS256, "1231a2314").compact();

		return token;
	}

}
