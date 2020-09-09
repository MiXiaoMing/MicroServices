package com.microservices.gateway.zuul.shiro;

import com.microservices.common.utils.JwtUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JWT证书匹配
 *
 **/
public class JwtCredentialsMatcher implements CredentialsMatcher {

    private static final Logger logger = LoggerFactory.getLogger(JwtCredentialsMatcher.class);

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        String token = authenticationToken.getCredentials().toString();
        try {
            return JwtUtil.verifyToken(token, "");
        } catch (Exception e) {
            logger.error("JWT Token CredentialsMatch Exception:" + e.getMessage(), e);
        }
        return false;
    }

}
