package com.lt.base.web.lock;

import java.util.concurrent.locks.Lock;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.lt.base.web.config.RedisConfig;

public class TestA {

	public static void main(String[] args) {
		final Printer outer = new Printer();
		new Thread(new Runnable() {
			@Override
			public void run() {
				outer.output("I am a boy.");
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				outer.output("You are a girl.");
			}
		}).start();
	}

}

class Printer {
	public void output(String name) {
		RedisConfig redisConfig = new RedisConfig();
		StringRedisTemplate redisTemplate = redisConfig.redisTemplate();
		Lock lock = new RedisLock(redisTemplate, "lock1");
		lock.lock();
		try {
			for (int i = 0; i < name.length(); i++) {
				System.out.print(name.charAt(i));
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			
		}finally {
			lock.unlock();
		}
	}
}