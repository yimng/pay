package com.edupay.cache.redis.service;

/**
 * Redis 的操作开放接口
 * <p/>
 * com.caicui.course.web.command.service
 * Created by yukewei on 2015/07/25 10:03.
 */
public interface RedisRawService {
    /**
     * 获取redis value (String)
     *
     * @param key
     * @return
     */
    byte[] get(String key);

    /**
     * 添加key value 并且设置存活时间
     *
     * @param key
     * @param value
     * @param liveTime 单位秒
     */
    void set(String key, String value, Long liveTime);

    /**
     * 添加key value
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

}
