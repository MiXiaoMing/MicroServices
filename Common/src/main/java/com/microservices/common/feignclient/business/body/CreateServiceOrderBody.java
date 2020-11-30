package com.microservices.common.feignclient.business.body;

import java.io.Serializable;

public class CreateServiceOrderBody implements Serializable {
    public String userID;
    public String deliveryAddressID, serviceCode, serviceName, serviceItems, remind;
    public float totalPrice, discountPrice, payPrice;
    public long serviceTime;
}
