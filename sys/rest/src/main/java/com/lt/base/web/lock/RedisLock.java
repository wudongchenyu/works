package com.lt.base.web.lock;

import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import com.google.common.collect.Maps;

public class RedisLock implements Lock {
	
	protected StringRedisTemplate redisTemplate;
	
	private static final String LOCKED = "TRUE";
	
	//ms
	private static final long TIME_OUT = 300;
	
	//s
	public static final int EXPIRE = 3;
	
	private String key;
	
	private volatile boolean locked = false;
	
	private static ConcurrentMap<String, RedisLock> map = Maps.newConcurrentMap();

	public RedisLock(StringRedisTemplate redisTemplate, String key) {
        this.key = "_LOCK_" + key;
        this.redisTemplate = redisTemplate;
    }
	
	public static RedisLock getInstance(StringRedisTemplate redisTemplate,String key) {
        return map.getOrDefault(key, new RedisLock(redisTemplate, key));
    }
	
	public void lock(long timeout) {
		long nano = System.nanoTime();
        timeout *= 1000000;
        final Random r = new Random();
        try {
            while ((System.nanoTime() - nano) < timeout) {
            	if (redisTemplate
            			.getConnectionFactory()
            			.getConnection()
            			.set(
            					key.getBytes(), 
            					LOCKED.getBytes(), 
            					Expiration.seconds(EXPIRE), 
            					SetOption.SET_IF_ABSENT)) {
            		locked = true;
                    break;
				}
                Thread.sleep(3, r.nextInt(500));
            }
            System.out.println();
        } catch (Exception e) {
        	
        }
	}
	
	@Override
	public void lock() {
		lock(TIME_OUT);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		
	}

	@Override
	public boolean tryLock() {
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public void unlock() {
		if (locked) {
            redisTemplate.delete(key);
        }
	}

	@Override
	public Condition newCondition() {
		return null;
	}
	
	

}
