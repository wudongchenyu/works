package com.taikang.sso.basic.util;

import com.taikang.sso.basic.config.RedisConnection;

import redis.clients.jedis.Jedis;

public class RedisUtils {
	
	public static Jedis openJedis() {
		return RedisConnection.getInstance().jedis();
	}
	
	public static void closeJedis(Jedis jedis) {
		RedisConnection.getInstance().returnResource(jedis);
	}
	
}
