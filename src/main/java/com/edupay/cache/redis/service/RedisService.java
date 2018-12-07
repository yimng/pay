package com.edupay.cache.redis.service;

import java.util.Set;

/**
 * Redis 的操作开放接口
 * <p/>
 * com.caicui.course.web.command.service
 * Created by yukewei on 2015/07/25 10:03.
 */
public interface RedisService<T> {

    /**
     * 查看redis里有多少数据
     */
    Long dbSize();

    /**
     * 通过key删除
     *
     * @param keys
     */
    Long delete(String... keys);

    /**
     * 检查key是否已经存在
     *
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * 清空redis所有数据
     *
     * @return
     */
    String flushDb();

    /**
     * 获取redis value (String)
     *
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 通过正则匹配keys
     *
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 检查是否连接成功
     *
     * @return
     */
    String ping();

    /**
     * 序列化对象
     *
     * @param t
     * @return
     */
    byte[] serialize(T t);

    /**
     * 添加key value 并且设置存活时间
     *
     * @param key
     * @param value
     * @param liveTime 单位秒
     */
    void set(String key, T value, Long liveTime);

    /**
     * 添加key value
     *
     * @param key
     * @param value
     */
    void set(String key, T value);

}
