package com.microservices.testdata.entity;

import java.io.Serializable;
import java.util.Date;

public class Page implements Serializable {
    public String id, schemeID, pageName, flag;
    public float responseTime;
    public Date operateTime;
}
