package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class ServiceOrder implements Serializable {
    public String id;

    public Date serviceTime;
    public String serviceCode, serviceName, serviceItems, remind;

    public float totalPrice, discountPrice, payPrice;
}
