package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class Cart implements Serializable {
    public String id, userID, goodsID, typeID, typeName;

    public int number;

    public String delflag;
    public Date updateTime, createTime;
}
