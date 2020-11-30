package com.microservices.common.constants;

public class RedisConstants {

    /*******  短信  ********/

    // 登录短信
    public final static String sms_code_pre_login = "sms_code_login_";
    // 登录短信 有效时长
    public final static int sms_code_login_expired = 5 * 60;



    /*******  用户  ********/

    // 用户信息
    public final static String user_pre = "user_";

    // token
    public final static String token_pre = "token_";
    // token 有效时长
    public final static int token_expired = 60 * 60;


    /*******  权限  ********/

    // 角色 -> 权限
    public final static String role_pre = "role_";

}
