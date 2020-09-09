package com.microservices.data.user.controller;

import com.microservices.common.feignclient.data.user.result.RolePermission;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.data.user.service.RolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user/rolePermission")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;


    private final Logger logger = LoggerFactory.getLogger(RolePermissionController.class);


    /**
     * 角色+权限 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<RolePermission> add(@RequestBody RolePermission body) {
        return rolePermissionService.insert(body);
    }

    /**
     * 角色+权限 通过ID 获取指定数据
     *
     * @param id 角色+权限表ID
     * @return
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<RolePermission> get(@RequestBody String id) {
        return rolePermissionService.selectList(id);
    }

    /**
     * 角色+权限 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<RolePermission> update(@RequestBody RolePermission body) {
        return rolePermissionService.update(body);
    }
}
