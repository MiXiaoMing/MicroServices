package com.microservices.interfaces.justbehere.user.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    public String id, loginID, telephone;
    public Date updateTime, createTime;
}
