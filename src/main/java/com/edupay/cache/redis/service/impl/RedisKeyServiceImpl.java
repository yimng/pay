package com.edupay.cache.redis.service.impl;

import com.edupay.cache.redis.service.RedisKeyService;
import com.edupay.cache.redis.service.RedisService;
import com.edupay.cache.redis.utils.NameUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * com.caicui.course.web.command.service.impl
 * Created by yukewi on 2015/5/25 10:11.
 */

@Component
@Service("redisKeyService")
public class RedisKeyServiceImpl implements RedisKeyService {
    @SuppressWarnings("rawtypes")
	@Autowired
    private RedisService redisService;

    /**
     * 获取key的树结构列表
     *
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public List<Map<String, Object>> getKeyTree(String pattern) {
        final Set keys = redisService.keys(pattern);
        return NameUtils.getInstance().toNodeMap(keys);
    }


}
