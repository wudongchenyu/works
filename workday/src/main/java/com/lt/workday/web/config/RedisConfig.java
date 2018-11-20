package com.lt.workday.web.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
	
	//连接池
	public @Bean JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		//连接池的最大数据库连接数
		jedisPoolConfig.setMaxTotal(1000);
		//最大空闲数
		jedisPoolConfig.setMaxIdle(300);
		//最大建立连接等待时间
		jedisPoolConfig.setMaxWaitMillis(1000);
		//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		jedisPoolConfig.setMinEvictableIdleTimeMillis(30000);
		//每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
		jedisPoolConfig.setNumTestsPerEvictionRun(1024);
		//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
		//是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个-
		jedisPoolConfig.setTestOnBorrow(true);
		//在空闲时检查有效性, 默认false
		jedisPoolConfig.setTestWhileIdle(true);
		return jedisPoolConfig;
	}
		
	//连接factory
//	public @Bean JedisConnectionFactory jedisConnectionFactory() {
//		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(
//				jedisPoolConfig());
//		jedisConnectionFactory.setHostName("127.0.0.1");
//		jedisConnectionFactory.setPort(6379);
//		return jedisConnectionFactory;
//	}
		
	//连接factory
	public @Bean JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(
				redisStandaloneConfiguration(),
				jedisClientConfiguration());
		return jedisConnectionFactory;
	}
		
	public @Bean(value = "redisTemplate") StringRedisTemplate redisTemplate() {
		StringRedisTemplate redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setEnableTransactionSupport(true);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
	
	public @Bean JedisClientConfiguration  jedisClientConfiguration() {
		JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration
				.builder()
				.usePooling()
				.poolConfig(jedisPoolConfig())
				.and()
				.connectTimeout(Duration.ofSeconds(2))
				.readTimeout(Duration.ZERO)
					.build();
		return jedisClientConfiguration;
	}
	
	public @Bean RedisStandaloneConfiguration redisStandaloneConfiguration() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = 
				new RedisStandaloneConfiguration("127.0.0.1", 6379);
		return redisStandaloneConfiguration;
	}
	
	public static void main(String[] args) {
		RedisConfig redisConfig = new RedisConfig();
		StringRedisTemplate redisTemplate = redisConfig.redisTemplate();
		System.out.println(redisTemplate.opsForValue().get("asda"));
	}
		
		/*//集群
		 * public @Bean RedisSentinelConfiguration sentinelConfiguration() {
			RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();
			sentinelConfiguration.setMaster(redisNode());
			RedisNode redisNode = new RedisNode("127.0.0.1", 6379);
			Set<RedisNode> set = new HashSet<RedisNode>();
			set.add(redisNode);
			sentinelConfiguration.setSentinels(set);
			return sentinelConfiguration;
		}
		
		public @Bean RedisNode redisNode() {
			RedisNode redisNode = RedisNode.newRedisNode().build();
			redisNode.setName("mymaster");
			return redisNode;
		}
		
		public @Bean JedisConnectionFactory jedisConnectionFactory() {
			JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(
					sentinelConfiguration(), 
					jedisPoolConfig());
			return jedisConnectionFactory;
		}*/

}
