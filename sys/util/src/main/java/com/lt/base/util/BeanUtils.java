package com.lt.base.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BeanUtils {
	
	public static <T> T copy(T from, T to) {
		Field[] fromFields = from.getClass().getDeclaredFields();
		Field[] toFields = to.getClass().getDeclaredFields();
		Map<String, Field> map = Arrays.asList(fromFields)
			.parallelStream()
			.collect(
					Collectors.toMap(Field::getName, Function.identity()));
		for (Field field : toFields) {
			String key = field.getName();
			if (map.containsKey(key)) {
				field.setAccessible(true);
				Field fromField = map.get(key);
				try {
					if (fromField.get(from)== null 
							|| fromField.get(from).toString().equals("") 
							|| fromField.get(from).toString().trim().equals("")) {
						initParam(field, to);
						continue;
					}
					field.set(to, fromField.get(from));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
			}
		}
		return to;
	}
	
	private static <T> void initParam(Field field, T to) {
		try {
			if (field.getType().equals(String.class)) {
				field.set(to, null);
			}
			if (field.getType().equals(Boolean.class)) {
				field.set(to, true);
			}
			if (field.getType().equals(Integer.class)) {
				field.set(to, 0);
			}
			if (field.getType().equals(Float.class)) {
				field.set(to, 0F);
			}
			if (field.getType().equals(Double.class)) {
				field.set(to, 0D);
			}
			if (field.getType().equals(BigDecimal.class)) {
				field.set(to, new BigDecimal(0));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
