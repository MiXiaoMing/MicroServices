package com.microservices.data.cache.entity;

import java.io.Serializable;

public class ExtendBody implements Serializable {
    public String key, value;
    public int seconds;
}
