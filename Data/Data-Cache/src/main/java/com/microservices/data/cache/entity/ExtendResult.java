package com.microservices.data.cache.entity;

import java.io.Serializable;

public class ExtendResult implements Serializable {
    public String key, value;
    public long ttl;
}
