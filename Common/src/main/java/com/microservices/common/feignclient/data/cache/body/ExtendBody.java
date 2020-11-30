package com.microservices.common.feignclient.data.cache.body;

import java.io.Serializable;

public class ExtendBody implements Serializable {
    public String key, value;
    public int seconds;
}
