package com.microservices.common.feignclient.data.user;

import java.io.Serializable;
import java.util.Date;

public class UserResult implements Serializable {
    public String id, name, type, phoneNumber;
    public String delflag;
    public Date updateTime, createTime;
}
