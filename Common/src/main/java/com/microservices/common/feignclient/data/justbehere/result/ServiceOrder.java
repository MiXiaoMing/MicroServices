package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class ServiceOrder implements Serializable {
    public String id, userID, tradeID, deliveryAddressID;

    public Date serviceTime;
    public String serviceCode, serviceName, serviceItems, remind;

    public float totalPrice, discountPrice, payPrice;
    public String status, content;

    public long remainTime;

    public String delflag;
    public Date updateTime, createTime;
}
