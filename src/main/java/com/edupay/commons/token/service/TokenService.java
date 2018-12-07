package com.edupay.commons.token.service;

/**
 * com.caicui.commons.token
 * Created by yukewi on 2016/4/11 18:06.
 */
public interface TokenService {
    /**
     * 根据token获取MemberId
     *
     * @param token
     * @return
     */
    String getMemberId(String token);
}
