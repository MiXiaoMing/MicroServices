package com.microservices.testdata.entity;

import java.io.Serializable;
import java.util.Date;

public class Interface implements Serializable {
    public String id, schemeID, url, flag;
    public float responseTime;
    public Date operateTime;
}
