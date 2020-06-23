package com.microservices.common.feignclient.data.user.body;

import java.io.Serializable;
import java.util.Date;

public class UserBaseBody implements Serializable {
    public String id, name, type, phoneNumber;
}
