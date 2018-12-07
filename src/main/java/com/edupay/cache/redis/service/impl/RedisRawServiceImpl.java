package com.edupay.cache.redis.service.impl;

import com.edupay.cache.redis.service.RedisRawService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * com.caicui.course.web.command.service.impl
 * Created by yukewi on 2015/5/25 10:11.
 */

@Component
@Service("redisRawService")
public class RedisRawServiceImpl implements RedisRawService {

    @SuppressWarnings("rawtypes")
	@Autowired
    private RedisTemplate redisTemplate;


    /**
     * 获取redis value (String)
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
	@Override
    public byte[] get(final String key) {
        return (byte[]) redisTemplate.execute(new RedisCallback<Object>() {
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.get(key.getBytes());
            }
        });
    }


    /**
     * 添加key value 并且设置存活时间
     *
     * @param key
     * @param value
     * @param liveTime 单位秒
     */
    @Override
    public void set(final String key, final String value, final Long liveTime) {
        this.set(key.getBytes(), value.getBytes(), liveTime);
    }

    /**
     * 添加key value
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {
        this.set(key, value, 0L);
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
