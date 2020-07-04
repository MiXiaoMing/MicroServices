package com.microservices.common.feignclient.data.order.result;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    public String id, userID, tradeID, deliveryAddressID;

    public String status, content;

    public long remainTime;

    public String delflag;
    public Date updateTime, createTime;
}
