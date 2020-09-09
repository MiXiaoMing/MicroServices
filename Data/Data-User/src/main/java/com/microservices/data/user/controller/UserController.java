package com.microservices.data.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.microservices.data.user.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user/base")
public class UserController {

    @Autowired
    private UserService userService;


    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    /**
     * 用户信息 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> add(@RequestBody UserBase body) {
        return userService.insert(body);
    }

    /**
     * 用户信息 通过ID 获取指定数据
     *
     * @param id 用户信息表ID
     * @return
     */
    @RequestMapping(value = "/getByID", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> get(@RequestBody String id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", id);
        return userService.select(jsonObject);
    }

    /**
     * 用户信息 通过 获取指定数据
     *
     * @param body phoneNumber + type
     * @return
     */
    @RequestMapping(value = "/getByPhoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> get(@RequestBody JSONObject body) {
        ResponseModel<UserBase> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(body.getString("phoneNumber"))) {
            responseModel.setMessage("phoneNumber为空");
            return responseModel;
        }

        if (StringUtil.isEmpty(body.getString("type"))) {
            responseModel.setMessage("type为空");
            return responseModel;
        }

        return userService.select(body);
    }

    /**
     * 用户信息 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> update(@RequestBody UserBase body) {
        return userService.update(body);
    }
}
