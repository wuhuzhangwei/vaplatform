package com.newgrand.vaplatform.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConstantHelper {
	public final static String SESSION_NGUSER = "NGUSER";
	
	public final static String DEAULTENTERPRISEPASSWORD = "123456";
	
	public static  String getGuid(){
		String guid = java.util.UUID.randomUUID().toString();
		return guid.substring(0,8) + guid.substring(9,13) + guid.substring(14,18) + guid.substring(19,23) + guid.substring(24); 
    } 
	
	/**
	 * 得到文件名
	 * @return
	 */
	public static String getRandomFileName() {
		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString() + getGuid();
	}
}
