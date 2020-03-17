package com.microservices.testdata.entity;

import java.io.Serializable;
import java.util.Date;

public class Scheme implements Serializable {
    public String id, appID, appVersion, deviceModel, systemVersion, flag;
    public Date startTime, endTime;
}
