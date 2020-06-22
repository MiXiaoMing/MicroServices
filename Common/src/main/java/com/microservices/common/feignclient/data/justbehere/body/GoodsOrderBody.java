package com.microservices.common.feignclient.data.justbehere.body;

import java.io.Serializable;

public class GoodsOrderBody implements Serializable {
    public String id, userID;
    public String deliveryAddressID, goodsItems, remind;
    public float price;
    public String status, content;
}
