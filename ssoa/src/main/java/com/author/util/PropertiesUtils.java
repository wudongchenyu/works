package com.author.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
	
	public static Properties getProperties() {
		Properties prop = new Properties();
		try {
			InputStream inputStream = PropertiesUtils.class.getResourceAsStream("/application.properties");
			prop.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public static String getProperties(String keyWord) {
		Properties prop = new Properties();
		String value = null;
		try {
			InputStream inputStream = PropertiesUtils.class.getResourceAsStream("/application.properties");
			prop.load(inputStream);
			value = prop.getProperty(keyWord);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static Integer getIntegerProperties(String keyWord) {
		return Integer.parseInt(getProperties(keyWord));
	}
}
