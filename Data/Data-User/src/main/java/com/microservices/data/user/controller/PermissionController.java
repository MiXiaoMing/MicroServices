package com.microservices.data.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.constants.RedisConstants;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.user.result.Permission;
import com.microservices.common.feignclient.data.user.result.RolePermission;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.microservices.data.user.service.PermissionService;
import com.microservices.data.user.service.RolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/user/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private DataCacheClient dataCacheClient;

    @Autowired
    private AmqpTemplate amqpTemplate;


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
     * 权限 通过角色ID 获取指定数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getListByRole", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> getListByRole(@RequestBody String id) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        ResponseModel<String> roleResponse = dataCacheClient.get(RedisConstants.role_pre + id);
        if (roleResponse.isSuccess()) {
            responseModel.setSuccess(true);
            responseModel.setData(roleResponse.getData());
        } else {
            ResponseArrayModel<RolePermission> rolePermissionResponseArray = rolePermissionService.selectList(id);
            if (rolePermissionResponseArray.isSuccess()) {
                List<String> permissionIDs = new ArrayList<>();
                for (int i = 0; i < rolePermissionResponseArray.getData().size(); ++i) {
                    RolePermission rolePermission = rolePermissionResponseArray.getData().get(i);
                    permissionIDs.add(rolePermission.permission_id);
                }

                if (permissionIDs.size() > 0) {
                    // 获取权限内容
                    ResponseArrayModel<Permission> permissionResponse = getList(permissionIDs);
                    String permissionStr = "";
                    for (int i = 0; i < permissionResponse.getData().size(); ++i) {
                        Permission permissionItem = permissionResponse.getData().get(i);
                        if (!StringUtil.isEmpty(permissionStr)) {
                            permissionStr += ",";
                        }
                        permissionStr += permissionItem.code;
                    }

                    // 缓存数据
                    JSONObject body = new JSONObject();
                    body.put("key", RedisConstants.role_pre + id);
                    body.put("value", permissionStr);
                    amqpTemplate.convertAndSend("redis", "set", body);

                    responseModel.setSuccess(true);
                    responseModel.setData(roleResponse.getData());
                }
            }
        }

        return responseModel;
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
