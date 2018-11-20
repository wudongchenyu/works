package com.author.test;

import java.util.Set;

import com.author.util.RedisUtils;

import redis.clients.jedis.Jedis;

public class TestMain {
	
	public static void main(String[] args) {
		Jedis jedis = RedisUtils.openJedis();
		Set<String> set = jedis.keys("token_*,user_lisi");
		for (String string : set) {
			System.out.println(string);
			jedis.del(string);
		}
	}

}
