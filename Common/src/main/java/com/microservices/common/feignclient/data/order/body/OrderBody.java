package com.microservices.common.feignclient.data.order.body;

import java.io.Serializable;

public class OrderBody implements Serializable {
    public String userID;
    public String deliveryAddressID;
}
