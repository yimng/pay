package com.edupay.cache.redis.service.impl;

import com.edupay.cache.redis.service.RedisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;

/**
 * com.caicui.course.web.command.service.impl
 * Created by yukewi on 2015/5/25 10:11.
 */

@Component
@Service("redisService")
public class RedisServiceImpl<T extends Serializable> implements RedisService<T> {

    @SuppressWarnings("rawtypes")
	@Autowired
    private RedisTemplate redisTemplate;

    /**
     * 反序列化对象
     *
     * @param bytes
     * @return
     */
    @SuppressWarnings("rawtypes")
	public Object deserialize(byte[] bytes) {
        final RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
        final RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
        final RedisSerializer defaultSerializer = redisTemplate.getDefaultSerializer();
        Object result = deserialize(valueSerializer, bytes);
        if (result != null) return result;
        result = deserialize(stringSerializer, bytes);
        if (result != null) return result;
        result = deserialize(defaultSerializer, bytes);
        if (result != null) return result;
        return hex(bytes);
    }

    /**
     * 通过正则匹配keys
     *
     * @param pattern
     * @return
     */
    @SuppressWarnings("unchecked")
	@Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 查看redis里有多少数据
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Long dbSize() {
        return (Long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    /**
     * 通过key删除
     *
     * @param keys
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Long delete(final String... keys) {
        return (Long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                Long result = 0L;
                for (int i = 0; i < keys.length; i++) {
                    result = connection.del(keys[i].getBytes());
                }
                return result;
            }
        });
    }

    /**
     * 检查key是否已经存在
     *
     * @param key
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public boolean exists(final String key) {
        return (Boolean) redisTemplate.execute(new RedisCallback() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(key.getBytes());
            }
        });
    }

    /**
     * 清空redis所有数据
     *
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public String flushDb() {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "success";
            }
        });
    }

    /**
     * 获取redis value (String)
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
	@Override
    public Object get(final String key) {
        return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return deserialize(connection.get(key.getBytes()));
            }
        });
    }

    /**
     * 检查是否连接成功
     *
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public String ping() {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.ping();
            }
        });
    }

    /**
     * 序列化对象
     *
     * @param t
     * @return
     */
    @SuppressWarnings("unchecked")
	public byte[] serialize(T t) {
        return redisTemplate.getValueSerializer().serialize(t);
    }

    /**
     * 添加key value 并且设置存活时间
     *
     * @param key
     * @param value
     * @param liveTime 单位秒
     */
    @Override
    public void set(final String key, final T value, final Long liveTime) {
        this.set(key.getBytes(), serialize(value), liveTime);
    }

    /**
     * 添加key value
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, T value) {
        this.set(key, value, 0L);
    }

    private String hex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    @SuppressWarnings("rawtypes")
	private Object deserialize(RedisSerializer serializer, byte[] bytes) {
        try {
            return serializer.deserialize(bytes);
        } catch (SerializationException e) {
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void set(final byte[] key, final byte[] value, final Long liveTime) {
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key, value);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }
}
