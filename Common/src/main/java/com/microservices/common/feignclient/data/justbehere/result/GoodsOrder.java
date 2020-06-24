package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class GoodsOrder implements Serializable {
    public String id, userID, tradeID, deliveryAddressID;

    public String goodsItems, remind;

    public float price;
    public String status, content;

    public long remainTime;

    public String delflag;
    public Date updateTime, createTime;
}
