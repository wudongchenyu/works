package com.taikang.sso.basic.config;

import com.taikang.sso.basic.util.PropertiesLoader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {

	private static RedisConnection redisConnection = null;

	private static JedisPool jedisPool = null;

	static {
		jedisPool = jedisPoolConfig();
	}

	private RedisConnection() {

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

		PropertiesLoader loader = new PropertiesLoader("/sso.properties");
		String host = loader.getProperty("redis.sso.host");
		Integer port = loader.getInteger("redis.sso.port");
		String passWord = loader.getProperty("redis.sso.password");
		String usePassWord = loader.getProperty("redis.sso.usePassWord");
		JedisPool pool = null;
		if (null != usePassWord && "true".equals(usePassWord)) {
			pool = new JedisPool(config, host, port, 10000, passWord);
		} else {
			pool = new JedisPool(config, host, port, 10000);
		}
		return pool;
	}

	public Jedis jedis() {

		Jedis jedis = null;
		if (jedisPool != null) {
			try {
				synchronized(this) {
					if (jedis == null) {
						jedis = jedisPool.getResource();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jedis;
	}

	public void returnResource(Jedis jedis) {
		if (jedis != null) {
			synchronized(this) {
				jedis.close();
			}
		}
	}

}
