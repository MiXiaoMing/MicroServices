package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class Carousel implements Serializable {
    public String id, carouselCode, code, path;

    public String delflag;
    public Date updateTime, createTime;
}
