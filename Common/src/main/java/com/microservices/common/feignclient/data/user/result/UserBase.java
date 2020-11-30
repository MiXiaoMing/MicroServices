package com.microservices.common.feignclient.data.user.result;

import java.io.Serializable;
import java.util.Date;

public class UserBase implements Serializable {
    public String id, name, type, phoneNumber;
    public String role_id;

    public String delflag;
    public Date updateTime, createTime;
}
