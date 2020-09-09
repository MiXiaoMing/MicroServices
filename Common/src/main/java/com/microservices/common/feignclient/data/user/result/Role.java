package com.microservices.common.feignclient.data.user.result;

import java.io.Serializable;
import java.util.Date;

public class Role implements Serializable {
    public String id, name, code, state, remark;
    public Date updateTime, createTime;
}
