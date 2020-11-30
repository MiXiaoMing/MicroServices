package com.microservices.common.feignclient.data.user.result;

import java.io.Serializable;
import java.util.Date;

/**
 *  用户 收货地址
 */
public class UserDeliveryAddress implements Serializable {
    public String id, contact, phoneNumber, region, detail;
    public String userID;
    public String delflag;
    public Date updateTime, createTime;
}
