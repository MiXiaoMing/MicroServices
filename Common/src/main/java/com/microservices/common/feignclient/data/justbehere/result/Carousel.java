package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class Carousel implements Serializable {
    public String id, carouselCode, classify, code, path;

    public String delflag;
    public Date updateTime, createTime;
}
