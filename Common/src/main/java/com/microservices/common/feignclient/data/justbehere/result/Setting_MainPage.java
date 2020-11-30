package com.microservices.common.feignclient.data.justbehere.result;

import java.io.Serializable;
import java.util.Date;

public class Setting_MainPage implements Serializable {
    public String id, type, classify, code;

    public String delflag;
    public Date updateTime, createTime;
}
