/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.microservices.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.microservices.common.generator.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

/**
 * JWT工具类
 * https://github.com/auth0/java-jwt
 *
 **/
@Component
public class JwtUtil {

    /**
     * 密码
     */
    public static String secret = "666666";

    /**
     * 签发人
     */
    public static String issuer = "ServiceFrame";

    /**
     * 主题
     */
    public static String subject = "Jwt";

    /**
     * 签发的目标
     */
    public static String audience = "Client";

    /**
     * token失效时间,默认1小时，60*60=3600
     */
    public static Long expire_second = 3600L;




    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public JwtUtil() {
    }

    /**
     * 生成JWT Token
     *
     * @param userID       用户名
     * @param salt           盐值
     * @param expireDuration 过期时间和单位
     * @return token
     */
    public static String generateToken(String userID, String salt, Duration expireDuration) {
        try {
            if (StringUtils.isBlank(userID)) {
                logger.error("username不能为空");
                return null;
            }
            logger.debug("userID:{}", userID);

            // 如果盐值为空，则使用默认值：666666
            if (StringUtils.isBlank(salt)) {
                salt = secret;
            }
            logger.debug("salt:{}", salt);

            // 过期时间，单位：秒
            Long expireSecond;
            // 默认过期时间为1小时
            if (expireDuration == null) {
                expireSecond = expire_second;
            } else {
                expireSecond = expireDuration.getSeconds();
            }
            logger.debug("expireSecond:{}", expireSecond);
            Date expireDate = DateUtils.addSeconds(new Date(), expireSecond.intValue());
            logger.debug("expireDate:{}", expireDate);

            // 生成token
            Algorithm algorithm = Algorithm.HMAC256(salt);
            String token = JWT.create()
                    .withClaim("userID", userID)
                    // jwt唯一id
                    .withJWTId(UUIDUtil.getUuid())
                    // 签发人
                    .withIssuer(issuer)
                    // 主题
                    .withSubject(subject)
                    // 签发的目标
                    .withAudience(audience)
                    // 签名时间
                    .withIssuedAt(new Date())
                    // token过期时间
                    .withExpiresAt(expireDate)
                    // 签名
                    .sign(algorithm);
            return token;
        } catch (Exception e) {
            logger.error("generateToken exception", e);
        }
        return null;
    }

    public static boolean verifyToken(String token, String salt) {
        try {
            // 如果盐值为空，则使用默认值：666666
            if (StringUtils.isBlank(salt)) {
                salt = secret;
            }
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    // 签发人
                    .withIssuer(issuer)
                    // 主题
                    .withSubject(subject)
                    // 签发的目标
                    .withAudience(audience)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            if (jwt != null) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Verify Token Exception", e);
        }
        return false;
    }

    /**
     * 解析token，获取token数据
     *
     * @param token
     * @return
     */
    public static DecodedJWT getJwtInfo(String token) {
        return JWT.decode(token);
    }

    /**
     * 获取用户名
     *
     * @param token
     * @return
     */
    public static String getUserID(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        DecodedJWT decodedJwt = getJwtInfo(token);
        if (decodedJwt == null) {
            return null;
        }
        return decodedJwt.getClaim("userID").asString();
    }

    /**
     * 获取创建时间
     *
     * @param token
     * @return
     */
    public static Date getIssuedAt(String token) {
        DecodedJWT decodedJwt = getJwtInfo(token);
        if (decodedJwt == null) {
            return null;
        }
        return decodedJwt.getIssuedAt();
    }

    /**
     * 获取过期时间
     *
     * @param token
     * @return
     */
    public static Date getExpireDate(String token) {
        DecodedJWT decodedJwt = getJwtInfo(token);
        if (decodedJwt == null) {
            return null;
        }
        return decodedJwt.getExpiresAt();
    }

    /**
     * 判断token是否已过期
     *
     * @param token
     * @return
     */
    public static boolean isExpired(String token) {
        Date expireDate = getExpireDate(token);
        if (expireDate == null) {
            return true;
        }
        return expireDate.before(new Date());
    }

}
