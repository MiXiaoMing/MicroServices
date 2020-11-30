package com.microservices.data.user.controller;

import com.microservices.common.feignclient.data.user.result.Role;
import com.microservices.common.response.ResponseModel;
import com.microservices.data.user.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    private final Logger logger = LoggerFactory.getLogger(RoleController.class);


    /**
     * 角色 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Role> add(@RequestBody Role body) {
        return roleService.insert(body);
    }

    /**
     * 角色 通过ID 获取指定数据
     *
     * @param id 角色表ID
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Role> get(@RequestBody String id) {
        return roleService.select(id);
    }

    /**
     * 角色 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Role> update(@RequestBody Role body) {
        return roleService.update(body);
    }
}
