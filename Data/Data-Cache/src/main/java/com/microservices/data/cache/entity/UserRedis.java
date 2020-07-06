package com.microservices.data.cache.entity;

import java.io.Serializable;

public class UserRedis implements Serializable {
    public String token;

    @Override
    public String toString() {
        return "{" +
                "\"token\":\"" + token + "\""
                + "}";
    }
}
