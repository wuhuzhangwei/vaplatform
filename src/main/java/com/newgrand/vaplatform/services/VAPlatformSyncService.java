package com.newgrand.vaplatform.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.newgrand.log4ng.ngLoggerFactory;
import com.newgrand.log4ng.ngLoggerFactory.loggerType;
import com.newgrand.vaplatform.cache.VAPropertiesCache;
import com.newgrand.vaplatform.filesrv.OSSObjectHelper;
import com.newgrand.vaplatform.helper.PropertiesHelper;

/**
 * 企业验证平台同步服务
 * @author zhangwei
 *
 */
@Repository
public class VAPlatformSyncService {
	private static String CloudUrl = "";
	
	public static List<Object> getProperties(String esn) {
		try {
			List<Object> object = (List<Object>)getPropertiesFromCache();
			if(object == null){
				byte[] bts = OSSObjectHelper.downloadFile(OSSObjectHelper.getBucketName(), getFileName(esn));
				List<Object> properties = null;
				
				//调用徐桦的jar包进行解析
				
				savePropertiesToCache(properties);	//保存到缓存
			}
			return object;	
		} catch (Exception e) {
			ngLoggerFactory.getLogger(loggerType.cache).error(e.getMessage(),e);
			return null;
		}
	}
	
	private static String getFileName(String esn){
		return esn + ".dat";
	}
	
	public static Object getPropertiesFromCache() throws Exception{
		try {
			Object object = VAPropertiesCache.getValueByKey(VAPropertiesCache.CACHEKEY);
			return object;
		} catch (Exception e) {
			return e;
		}
	}
	
	public static synchronized boolean savePropertiesToCache(Object object) throws Exception{
		if(VAPropertiesCache.updateElement(VAPropertiesCache.CACHEKEY, object) != true){
			throw new Exception("更新到缓存失败");
		}
		return true;
	}
}
