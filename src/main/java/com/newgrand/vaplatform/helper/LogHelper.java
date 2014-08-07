package com.newgrand.vaplatform.helper;

import org.apache.log4j.Logger;

public class LogHelper {
	 private static Logger logger = Logger.getLogger(LogHelper.class);
	 
	 private static Boolean CheckMsg(String msg){
		 if(msg == null || msg == ""){
			 return false;
		 }
		 return true;
	 }
	 
	 public static Boolean error(String msg){
		 if(!CheckMsg(msg))	return false;
		 logger.error(msg);
		 return true;
	 }

	 public static Boolean error(Exception ex){		 
		 error(getStack(ex.getStackTrace()));
		 error(ex.getMessage());
		 return true;
	 }
	 
	 private static String getStack(StackTraceElement[] traces){
		 if(traces.length == 0)	return "";
		 StringBuffer sbBuffer = new StringBuffer();
		 for (StackTraceElement stackTraceElement : traces) {
			 sbBuffer.append(stackTraceElement.toString());
		}
		 return sbBuffer.toString();
	 }
	 
	 public static Boolean warn(String msg){
		 if(!CheckMsg(msg))	return false;
		 logger.warn(msg);
		 return true;
	 }
	 
	 public static Boolean info(String msg){
		 if(!CheckMsg(msg))	return false;
		 logger.info(msg);
		 return true;
	 }
	 
	 public static Boolean debug(String msg){
		 if(!CheckMsg(msg))	return false;
		 logger.debug(msg);
		 return true;
	 }
}
