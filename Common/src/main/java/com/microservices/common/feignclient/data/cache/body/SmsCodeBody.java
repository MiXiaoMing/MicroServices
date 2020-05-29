package com.microservices.common.feignclient.data.cache.body;

import java.io.Serializable;

public class SmsCodeBody implements Serializable {
    public String phoneNumber, smsCode;
}
