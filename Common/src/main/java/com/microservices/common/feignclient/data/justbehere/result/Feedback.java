package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class Feedback implements Serializable {
    public String id, userID, content;

    public Date createTime;
}
