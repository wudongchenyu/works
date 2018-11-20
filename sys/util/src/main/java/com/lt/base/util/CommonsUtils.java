package com.lt.base.util;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.lt.base.contants.ConstantsEnum;
import com.lt.base.id.SnowflakeIdMaker;

public class CommonsUtils {
	
	public static long generationId(int type) {
		return new SnowflakeIdMaker(type).nextId();
	}
	
	public static String generationCode(int type) {
		Map<Integer, String> map = Arrays
			.asList(ConstantsEnum.values())
			.stream()
			.collect(
					Collectors
						.toMap(
								ConstantsEnum::getType, 
								ConstantsEnum::getPrefix, 
								(key1, key2) -> key2));
		return map.get(type) + new SnowflakeIdMaker(type).nextId();
	}
	
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static void main(String[] args) {
//		System.out.println(getUUID().length());
		System.out.println(genId("2"));
	}
	
	public synchronized static String genId(String type){
		Map<Integer, String> map = Arrays
				.asList(ConstantsEnum.values())
				.stream()
				.collect(
						Collectors
							.toMap(
									ConstantsEnum::getType, 
									ConstantsEnum::getPrefix, 
									(key1, key2) -> key2));
		String prxString = (System.currentTimeMillis() + "").substring(1);
		String nextString = (System.nanoTime() + "").substring(7, 10);
	    String orderId = map.get(Integer.parseInt(type)) + prxString + nextString + type;
	    return orderId;
	}

}
