package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class Goods implements Serializable {
    public String id, code, title, icon, tag, desc, content;

    public String classify, level;

    public String delflag;
    public Date updateTime, createTime;
}
