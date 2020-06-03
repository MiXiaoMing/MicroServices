package com.microservices.common.feignclient.data.user.body;

import java.io.Serializable;
import java.util.Date;

public class CreateUserBody implements Serializable {
    public String name, type, phoneNumber;
}
