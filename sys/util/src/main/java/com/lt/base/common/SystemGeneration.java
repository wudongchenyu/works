package com.lt.base.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 用于生成编号（编号唯一）
 * @author wudon
 *
 */
public class SystemGeneration {
	
	private static Random random = new Random();
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	public static String getTimeNumber(String prefix){
		return prefix + sdf.format(new Date());
	}
	
	public static String getUuidNumber(String prefix){
		return prefix + UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String getRandomTimeNumber(String prefix,int num){
		if (num<=0) {
			num = 0;
		}
		return prefix + sdf.format(new Date()) + getRandom(4);
	}
	
	public static String getRandom(int num) {
		StringBuffer randomNumber = new StringBuffer("");
		for (int i = 0; i < num; i++) {
			randomNumber.append(random.nextInt(10));
		}
		return randomNumber.toString();
	}

}
