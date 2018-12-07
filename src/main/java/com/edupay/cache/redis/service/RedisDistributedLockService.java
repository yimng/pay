/**
 *
 * @Title RedisDistributedLockService.java
 * @author wanglu
 * @date 2018年11月26日
 */
package com.edupay.cache.redis.service;

/**  
 *
 * Title RedisDistributedLockService 
 * @author wanglu  
 * @date 2018年11月26日  
 */
public interface RedisDistributedLockService {

	/**
     * 加redis锁
     * @param key key
     * @param value 值
     * @param expire 过期时间
     * @return
     */
    boolean setLock(final String key,final String value, final long expire);
    
    /**
     * 获取锁资源
     * @param key
     * @return
     */
    String get(final String key);
    
    /**
     * 释放redis锁资源
     * @param key
     * @param requestId
     * @return
     */
    boolean releaseLock(final String key,String requestId);
    
}
