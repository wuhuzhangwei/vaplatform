package com.newgrand.vaplatform.helper;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {
	public static  Properties getProperties(String filename) {
		try {
			InputStream in = null;
			// 装载程序配置
			Properties property = new Properties();
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
			property.load(in);
			in.close();
			return property;
		} catch (Exception e) {
			LogHelper.error(e);
			return null;
		}
	} 
	
	public static String getProperty(String filename, String key) throws Exception {
		Properties property = PropertiesHelper.getProperties(filename);
		if(property == null){
			throw new Exception("找不到配置文件");
		}
		String value = property.getProperty(key, "");
		return value;
	}
}
