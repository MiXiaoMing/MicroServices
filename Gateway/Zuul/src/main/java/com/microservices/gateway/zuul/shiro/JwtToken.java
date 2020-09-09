package com.microservices.gateway.zuul.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Shiro JwtToken对象
 *
 **/
public class JwtToken implements AuthenticationToken {

    /**
     * 登录token
     */
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
