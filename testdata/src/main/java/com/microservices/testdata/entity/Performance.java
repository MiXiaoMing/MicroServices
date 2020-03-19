package com.microservices.testdata.entity;

import java.io.Serializable;
import java.util.Date;

public class Performance implements Serializable {
    public String id, schemeID, flag;
    public float cpuRatio, memorySize;
    public Date operateTime;
}
