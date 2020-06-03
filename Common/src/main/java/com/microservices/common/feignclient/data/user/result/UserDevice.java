package com.microservices.common.feignclient.data.user.result;

import java.io.Serializable;
import java.util.Date;

public class UserDevice implements Serializable {
    public String id, did, mac, region;
    public String userID;
    public String delflag;
    public Date updateTime, createTime;
}
