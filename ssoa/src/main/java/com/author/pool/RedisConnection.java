package com.author.pool;

import java.util.Properties;

import com.author.util.PropertiesUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {
	
	private static volatile RedisConnection redisConnection = null;
	
	private static JedisPool jedisPool = null;
	
	static {
		jedisPool = jedisPoolConfig();
	}
	
	private RedisConnection() {
		
	}
	
	public static RedisConnection getInstance() {
		if (null == redisConnection) {
			synchronized(RedisConnection.class) {
				if (null == redisConnection) {
					redisConnection = new RedisConnection();
				}
			}
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
		
		Properties properties = PropertiesUtils.getProperties();
		String host = properties.getProperty("redis.sso.host");
		Integer port = Integer.parseInt(properties.getProperty("redis.sso.port"));
		String passWord = properties.getProperty("redis.sso.password");
		String usePassWord = properties.getProperty("redis.sso.usePassWord");
		JedisPool pool = null;
		if (null != usePassWord && "true".equals(usePassWord)) {
			pool = new JedisPool(config, host, port, 10000, passWord);
		} else {
			pool = new JedisPool(config, host, port, 10000);
		}
		return pool;
	}
	
	public synchronized Jedis jedis() {
		
		Jedis jedis = null;
		if (jedisPool != null) {
			try {
				if (jedis == null) {
					jedis = jedisPool.getResource();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jedis;
	}
	
	public synchronized void returnResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

}
