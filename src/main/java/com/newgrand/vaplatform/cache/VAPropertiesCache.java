package com.newgrand.vaplatform.cache;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 属性缓存
 * @author zhangwei
 *
 */
public class VAPropertiesCache {
	public static final String CACHEKEY = "NGCPROPERTIES";
	private static Cache properiesCache = null;
	private static Lock syncLock = new ReentrantLock();

	static {
		InitCache();
	}
	
	/**
	 * 初始化缓存
	 */
	public static void InitCache(){
		CacheManager singletonManager = CacheManager.create();
		properiesCache = new Cache(CACHEKEY, 5000, true, false, 60 * 60, 60 * 60);
		singletonManager.addCache(properiesCache);
	}
	
	/**
	 * 得到缓存（同时初始化）
	 * @return
	 */
	public static Cache getCache() throws Exception{
		if (properiesCache == null) {
			syncLock.lock();
			try {
				if(properiesCache!= null){	//防止并发时其实已经初始化了
					return properiesCache;
				}
				InitCache();
			} catch (Exception e) {
				throw e;
			}
			finally{
				syncLock.unlock();
			}
			// Cache test = singletonManager.getCache("testCache");
		}
		return properiesCache;
	}

	/**
	 * 利用键得到值
	 * @param key
	 * @return
	 */
	public static Object getValueByKey(Object key) throws Exception {
		try {
			Cache cache = getCache();
			if (cache == null) {
				return null;
			} else {
				Element element = cache.get(key);
				if(element != null){
					return element.getObjectValue();
				}
				return null;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 将得到的值转为字符串
	 */
	public static String getValueToString(Object key) throws Exception{
		try {
			Object value = getValueByKey(key);
			if(value != null)	return value.toString();
			return "";
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 更新、插入元素
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean updateElement(Object key, Object value) throws Exception {
		try {
			Cache cache = getCache();
			if (cache != null) {
				cache.put(new Element(key, value));
				return true;
			}
			return false;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 更新、插入元素
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean removeElement(Object key) throws Exception{
		try {
			Cache cache = getCache();
			if (cache != null) {
				cache.remove(key);
				return true;
			}
			return false;
		} catch (Exception e) {
			throw e;
		}
	}
}
