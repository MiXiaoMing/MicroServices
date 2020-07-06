package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class ServicePrice implements Serializable {
    public String id, code, name;

    public float price, discount;
    public int minimum;
    public String unit;

    public String delflag;
    public Date updateTime, createTime;
}
