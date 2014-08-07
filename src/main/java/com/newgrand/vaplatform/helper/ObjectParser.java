package com.newgrand.vaplatform.helper;

import java.util.Date;

public class ObjectParser {
	public static String ParseObjectToString(Object obj){
		if(obj == null)	return "";
		return obj.toString();
	}
	
	public static boolean ParseObjectToBool(Object obj){
		if(obj == null || obj == "" || obj.equals("0") || obj.equals("false"))	return false;
		return true;
	}
	
	public static Date ParseObjectToDate(Object obj){
		if(obj == null)	return new Date();
		return (Date)obj;
	}
}
