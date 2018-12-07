package com.edupay.cache.redis.service;

import java.util.List;
import java.util.Map;

/**
 * Redis 的操作开放接口
 * <p/>
 * com.caicui.course.web.command.service
 * Created by yukewei on 2015/07/25 10:03.
 */
public interface RedisKeyService {

    /**
     * 获取key的树结构列表
     *
     * @return
     */
    List<Map<String, Object>> getKeyTree(String pattern);

}
