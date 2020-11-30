package com.microservices.common.utils;

public class RandomUtil {

    /**
     * 6位 验证码
     */
    public static String smsCode() {
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int) (Math.random() * 9);
        }
        return vcode;
    }
}
