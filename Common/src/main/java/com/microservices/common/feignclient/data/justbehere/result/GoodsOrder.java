package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class GoodsOrder implements Serializable {
    public String id;
    public String goodsItems, remind;

    public float price;
}
