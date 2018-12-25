package com.taikang.sso.basic.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class PropertiesLoader {
	
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	private final Properties properties; 
	
	public PropertiesLoader(String... resourcesPaths) { 
		properties = loadProperties(resourcesPaths); 
	} 
	
	public Properties getProperties() { 
		return properties; 
	} 
	
	/**
     * 取出Property，但以System的Property优先,取不到返回空字符串.
     */ 
	private String getValue(String key) { 
		String systemProperty = System.getProperty(key); 
		if (systemProperty != null) { 
			return systemProperty; 
		} if (properties.containsKey(key)) { 
			return properties.getProperty(key); 
		} return ""; 
		
	} 
	
	/**
     * 取出String类型的Property，但以System的Property优先,如果都为Null则抛出异常.
     */ public String getProperty(String key) { 
    	 String value = getValue(key); 
    	 if (value == null) { 
    		 throw new NoSuchElementException(); 
    		 
    	 } 
    	 return value; 
    	 
     } 
     
     /**
     * 取出String类型的Property，但以System的Property优先.如果都为Null则返回Default值.
     */ 
     public String getProperty(String key, String defaultValue) { 
    	 String value = getValue(key); 
    	 return value != null ? value : defaultValue; 
    	 
     } 
     
     /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     */ 
     public Integer getInteger(String key) { 
    	 String value = getValue(key); 
    	 if (value == null) { 
    		 throw new NoSuchElementException(); 
    		 } 
    	 return Integer.valueOf(value); 
    	 
     } 
     
     /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     */ 
     public Integer getInteger(String key, Integer defaultValue) { 
    	 String value = getValue(key); 
    	 return value != null ? Integer.valueOf(value) : defaultValue; 
    	 
     } 
     
     /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     */ 
     public Double getDouble(String key) { 
    	 String value = getValue(key); 
    	 if (value == null) {
    		 throw new NoSuchElementException(); 
    	 } 
    	 return Double.valueOf(value); 
    		 
     } 
     /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     */ 
     public Double getDouble(String key, Integer defaultValue) { 
    	 String value = getValue(key); 
    	 return value != null ? Double.valueOf(value) : defaultValue; 
    	 
     } 
     
     /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null抛出异常,如果内容不是true/false则返回false.
     */ 
     public Boolean getBoolean(String key) { 
    	 String value = getValue(key); 
    	 if (value == null) { 
    		 throw new NoSuchElementException(); 
    		 
    	 } 
    	 return Boolean.valueOf(value); 
    	 
     } 
     
     /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null则返回Default值,如果内容不为true/false则返回false.
     */ 
     public Boolean getBoolean(String key, boolean defaultValue) { 
    	 String value = getValue(key); 
    	 return value != null ? Boolean.valueOf(value) : defaultValue; 
    	 
     }
     
     /**
     * 载入多个文件, 文件路径使用Spring Resource格式.
     */ 
     private Properties loadProperties(String... resourcesPaths) { 
    	 Properties props = new Properties(); 
    	 for (String location : resourcesPaths) { 
    		 InputStream is = null; 
    		 try { 
    			 Resource resource = resourceLoader.getResource(location); 
    			 is = resource.getInputStream(); 
    			 props.load(is); 
    			 
    		 } catch (IOException ex) { 
    			 
    		 } finally {
    			try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		 } 
    		 
    	 } 
    	 return props; 
    	 
     }
     
     /**
     * 载入多个文件 classpath 文件
     */ 
     public Properties loadClassPathProperties(String resourcesPaths) { 
    	 Properties props = new Properties(); 
    	 Resource resource = new ClassPathResource(resourcesPaths); 
    	 InputStream is = null; 
    	 try { 
    		 is = resource.getInputStream(); 
    		 props.load(is); 
    		 
    	 } catch (IOException ex) { 
    		 
    	 } finally { 
    		try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	 } 
    	 return props; 
    	 
     }
     
     /**
     * 合并两个配置
     *
     * @param source source
     * @param dist   dist
     */ public void mergerProperties(Properties source, Properties dist) { 
    	 Enumeration<?> enum1 = source.propertyNames();
    	 //得到配置文件的名字 
    	 while (enum1.hasMoreElements()) { 
    		 Object strKey = enum1.nextElement(); 
    		 Object value = source.get(strKey); 
    		 dist.put(strKey, value); 
    		 
    	 } 
    	 
     }
     
     public static void main(String[] args) {
		PropertiesLoader loader = new PropertiesLoader("application.properties");
		System.out.println(loader.getProperty("redis.port"));
		
	}
     
}