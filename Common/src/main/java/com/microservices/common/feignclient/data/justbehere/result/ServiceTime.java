package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class ServiceTime implements Serializable {
    public Timestamp serviceTime;
    public int count;
}
