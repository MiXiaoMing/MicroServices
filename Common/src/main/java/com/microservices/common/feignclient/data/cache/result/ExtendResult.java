package com.microservices.common.feignclient.data.cache.result;

import java.io.Serializable;

public class ExtendResult implements Serializable {
    public String key, value;
    public long ttl;
}
