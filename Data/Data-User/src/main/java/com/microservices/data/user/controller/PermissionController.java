package com.microservices.data.user.controller;

import com.microservices.common.feignclient.data.user.result.Permission;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.data.user.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;


    private final Logger logger = LoggerFactory.getLogger(PermissionController.class);


    /**
     * 权限 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Permission> add(@RequestBody Permission body) {
        return permissionService.insert(body);
    }

    /**
     * 权限 通过ID 获取指定数据
     *
     * @param id 权限表ID
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Permission> get(@RequestBody String id) {
        return permissionService.select(id);
    }

    /**
     * 权限 通过ID列表 获取指定数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Permission> getList(@RequestBody List<String> body) {
        return permissionService.selectList(body);
    }

    /**
     * 权限 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Permission> update(@RequestBody Permission body) {
        return permissionService.update(body);
    }
}
