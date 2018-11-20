package com.author.pool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {
	
	private static RedisConnection redisConnection = null;
	
	private static JedisPool jedisPool = null;
	
	static {
		jedisPool = jedisPoolConfig();
	}
	
	public static synchronized RedisConnection getInstance() {
		if (null == redisConnection) {
			redisConnection = new RedisConnection();
		}
		return redisConnection;
	}

	private static JedisPool jedisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(1000);
		config.setMaxIdle(100);
		config.setMinIdle(50);
		config.setMaxWaitMillis(10000);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		config.setTestWhileIdle(true);
		config.setNumTestsPerEvictionRun(50);
		config.setTimeBetweenEvictionRunsMillis(30000);
		
		JedisPool pool = new JedisPool(config, "127.0.0.1", 6379);
		return pool;
	}
	
	public Jedis jedis() {
		return jedisPool.getResource();
	}

}
