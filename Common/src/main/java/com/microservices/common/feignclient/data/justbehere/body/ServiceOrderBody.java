package com.microservices.common.feignclient.data.justbehere.body;

import java.io.Serializable;

public class ServiceOrderBody implements Serializable {
    public String id, userID;
    public String deliveryAddressID, serviceCode, serviceName, serviceItems, remind;
    public float totalPrice, discountPrice, payPrice;
    public long serviceTime;
    public String status, content;
}
