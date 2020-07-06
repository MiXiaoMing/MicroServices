package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class ServiceDetail implements Serializable {
    public String id, code, note, effect, introduce, refertime, others, scope, tools, assurance, flow;

    public String classify, level;

    public String delflag;
    public Date updateTime, createTime;
}
