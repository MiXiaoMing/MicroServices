package com.microservices.common.feignclient.data.user.result;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色/权限 关联
 */
public class RolePermission implements Serializable {
    public String id, role_id, permission_id;
    private String delflag;
    public Date updateTime, createTime;
}
