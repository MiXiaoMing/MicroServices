package com.microservices.testdata.entity;

import java.io.Serializable;

public class Performance implements Serializable {
    public String id, schemeID, flag;
    public float cpuRatio, memorySize;
}
