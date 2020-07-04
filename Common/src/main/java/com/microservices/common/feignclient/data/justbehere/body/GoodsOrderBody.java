package com.microservices.common.feignclient.data.justbehere.body;

import java.io.Serializable;

public class GoodsOrderBody implements Serializable {
    public String id, goodsItems, remind;
    public float price;
}
