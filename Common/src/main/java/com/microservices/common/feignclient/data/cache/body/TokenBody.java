package com.microservices.common.feignclient.data.cache.body;

import java.io.Serializable;

public class TokenBody implements Serializable {
    public String userID, token;
}
