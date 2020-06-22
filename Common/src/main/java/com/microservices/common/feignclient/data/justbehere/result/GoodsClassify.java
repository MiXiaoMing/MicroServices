package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class GoodsClassify implements Serializable {
    public String id, code, title, name, desc, icon;

    public String delflag;
    public Date updateTime, createTime;
}
