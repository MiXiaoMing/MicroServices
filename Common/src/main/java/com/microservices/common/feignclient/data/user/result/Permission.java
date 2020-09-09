package com.microservices.common.feignclient.data.user.result;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限
 */
public class Permission implements Serializable {
    public String id, name, parent_id, code, state, remark;
    public Date updateTime, createTime;
}
