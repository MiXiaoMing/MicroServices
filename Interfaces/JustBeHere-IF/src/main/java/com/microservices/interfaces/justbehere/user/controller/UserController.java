package com.microservices.interfaces.justbehere.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {


    @Autowired
    JBH_BIZ_Client jbh_biz_client;

    @Autowired
    DataCacheClient dataCacheClient;

    /************  用户   ************/

    // TODO: 2020/6/3 hystrix 使用

//    @HystrixCommand(fallbackMethod = "infoFall")


    /**
     * 获取用户基本信息
     * @param token
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> info(@RequestHeader("token") String token) {
        ResponseModel<UserBase> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(token)) {
            responseModel.setErrCode("401");
            responseModel.setMessage("请登录后重试");
            return responseModel;
        }

        ResponseModel<String> tokenResponse = dataCacheClient.getUserID(token);
        if (!tokenResponse.isSuccess()) {
            responseModel.setErrCode("401");
            responseModel.setMessage(tokenResponse.getMessage());
            return responseModel;
        }

        return jbh_biz_client.getUser(tokenResponse.getData());
    }

    public ResponseModel<JSONObject> infoFall(JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();
        responseModel.setSuccess(false);
        return responseModel;
    }


}
