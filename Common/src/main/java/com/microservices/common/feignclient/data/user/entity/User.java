package com.microservices.common.feignclient.data.user.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    public String id, name, type, phoneNumber;
    public String delflag;
    public Date updateTime, createTime;
}
