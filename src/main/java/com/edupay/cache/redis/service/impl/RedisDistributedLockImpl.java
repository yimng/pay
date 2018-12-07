/**
 *
 * @Title RedisDistributedLockImpl.java
 * @author wanglu
 * @date 2018年11月26日
 */
package com.edupay.cache.redis.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.edupay.cache.redis.service.RedisDistributedLockService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;


/**  
 *
 * Title RedisDistributedLockImpl 
 * @author wanglu  
 * @date 2018年11月26日  
 */
@Component
@Service("redisDistributedLockService")
public class RedisDistributedLockImpl implements RedisDistributedLockService {
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public static final String UNLOCK_LUA;
    
    private static Logger logger = LoggerFactory.getLogger(RedisDistributedLockImpl.class);
    
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }
    
    /**
     * 加redis锁
     * @param key key
     * @param value 值
     * @param expire 过期时间
     * @return 
     */
    public boolean setLock(final String key,final String value, final long expire) {
    	try {
    		 RedisCallback<String> callback = new RedisCallback<String>() {
                 public String doInRedis(RedisConnection connection){
                 	JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                 	return commands.set(key, value, "NX", "EX", expire);
                 }
             };
            String result = redisTemplate.execute(callback);
            return !StringUtils.isEmpty(result);
        } catch (Exception e) {
            logger.error("set redis occured an exception", e);
        }
        return false;
    }
 
    /**
     * 获取锁资源
     * @param key
     * @return
     */
    public String get(final String key) {
        try {
            RedisCallback<String> callback = new RedisCallback<String>() {
                public String doInRedis(RedisConnection connection){
                	JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                    return commands.get(key);
                }
            };
            return redisTemplate.execute(callback);
        } catch (Exception e) {
            logger.error("get redis occured an exception", e);
        }
        return "";
    }
 
    /**
     * 释放redis锁资源
     * @param key
     * @param requestId
     * @return
     */
    public boolean releaseLock(final String key,String requestId) {
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
        	final List<String> keys = new ArrayList<>();
            keys.add(key);
            final List<String> args = new ArrayList<>();
            args.add(requestId);
 
            // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
            // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
            RedisCallback<Long> callback = new RedisCallback<Long>(){
            	public Long doInRedis(RedisConnection connection){
            		Object nativeConnection = connection.getNativeConnection();
            		// 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
            		// 集群模式
            		if (nativeConnection instanceof JedisCluster) {
            			return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
            		}
  
            		// 单机模式
            		else if (nativeConnection instanceof Jedis) {
            			return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, args);
            		}
            		return 0L;
                };
                
            };
            Long result = redisTemplate.execute(callback);
            return result != null && result > 0;
        } catch (Exception e) {
            logger.error("release lock occured an exception", e);
        } finally {
            // 清除掉ThreadLocal中的数据，避免内存溢出
            //lockFlag.remove();
        }
        return false;
    }
    
}
