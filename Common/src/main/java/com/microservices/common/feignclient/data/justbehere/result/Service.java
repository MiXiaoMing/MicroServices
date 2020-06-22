package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class Service implements Serializable {
    public String id, code, name, summary, desc, icon, bigIcon, cover;

    public String classify, level;

    public String delflag;
    public Date updateTime, createTime;
}
