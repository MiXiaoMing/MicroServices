package com.microservices.common.feignclient.data.user.body;

import java.io.Serializable;

public class UserDeviceBody implements Serializable {
    public String id, userID, did, mac, region;
}
