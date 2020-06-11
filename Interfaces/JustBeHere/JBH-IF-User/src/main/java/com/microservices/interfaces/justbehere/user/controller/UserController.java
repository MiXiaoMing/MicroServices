package com.microservices.interfaces.justbehere.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.business.justbehere.JBH_BIZ_UserClient;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.user.body.UserDeliveryAddressBody;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.microservices.common.utils.ValidateUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

//    @Autowired``
//    private UserService userService;

    /************  用户   ************/

    // TODO: 2020/6/3 hystrix 使用
    //获取用户基本信息
    @HystrixCommand(fallbackMethod = "infoFall")
    @RequestMapping(value = "/info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> info(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();
        responseModel.setSuccess(true);
//        responseModel.setData(userService.getUserInfo(id));
        return responseModel;
    }

    public ResponseModel<JSONObject> infoFall(JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();
        responseModel.setSuccess(true);
//        responseModel.setData(userService.getUserInfo(id));
        return responseModel;
    }



}
