package com.microservices.common.feignclient.business.body;

import java.io.Serializable;

public class CreateGoodsOrderBody implements Serializable {
    public String userID, deliveryAddressID;
    public String goodsItems, remind;
    public float price;
}
