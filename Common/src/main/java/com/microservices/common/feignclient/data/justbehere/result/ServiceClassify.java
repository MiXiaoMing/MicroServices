package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class ServiceClassify implements Serializable {
    public String id, code, name, desc, icon, cover, link;

    public String delflag;
    public Date updateTime, createTime;
}
