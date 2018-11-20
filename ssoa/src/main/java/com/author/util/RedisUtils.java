package com.author.util;

import com.author.pool.RedisConnection;

import redis.clients.jedis.Jedis;

public class RedisUtils {
	
	public static Jedis openJedis() {
		return RedisConnection.getInstance().jedis();
	}
	
	public static void closeJedis(Jedis jedis) {
		jedis.close();
	}

}
