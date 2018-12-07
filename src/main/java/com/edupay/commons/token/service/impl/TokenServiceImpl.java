package com.edupay.commons.token.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.edupay.cache.redis.service.RedisRawService;
import com.edupay.commons.assertion.CommonAssert;
import com.edupay.commons.token.service.TokenService;

/**
 * com.caicui.course.web.command.service.impl
 * Created by yukewi on 2015/5/25 10:11.
 */

@Component
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisRawService redisRawService;

    /**
     * 根据token获取MemberId
     *
     * @param token
     * @return
     */
    @Override
    public String getMemberId(String token) {
        CommonAssert.assertNotNull("token不能为空", token);
        final byte[] o = redisRawService.get(token);
        if (o == null) {
            throw new RuntimeException("用户未登录");
        }
        String memberId = new String(o);
        redisRawService.set(token, memberId, 60 * 60L);
        return memberId;
    }

}
