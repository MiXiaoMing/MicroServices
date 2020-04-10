package com.microservices.testdata.response;

import java.io.Serializable;
import java.util.Date;

public class SchemeResponse implements Serializable {
    public String recordId, recordStatus;
    public String appName, appVersion, platForm;
    public String phoneNum, phoneSystem;
    public String recordStartTime, recordEndTime;
    public Date startTime, endTime;
    public float classConsumeTime, urlConsumeTime, cpuUse, memoryUse;
}
