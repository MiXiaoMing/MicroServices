package com.microservices.common.feignclient.data.user.body;

import java.io.Serializable;

public class UserDeliveryAddressBody implements Serializable {
    public String id, userID, contact, phoneNumber, region, detail;
}
