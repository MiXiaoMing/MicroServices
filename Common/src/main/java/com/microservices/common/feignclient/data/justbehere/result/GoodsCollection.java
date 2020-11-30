package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GoodsCollection implements Serializable {
    public String code;
    public Goods goods;
    public List<GoodsPrice> goodsPrices;
    public List<Carousel> carousels;
}
