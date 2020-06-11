package com.microservices.interfaces.justbehere.user.controller;

import com.microservices.common.feignclient.business.justbehere.JBH_BIZ_UserClient;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.user.body.UserDeviceBody;
import com.microservices.common.feignclient.data.user.body.UserDeviceBody;
import com.microservices.common.feignclient.data.user.result.UserDevice;
import com.microservices.common.feignclient.data.user.result.UserDevice;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.microservices.common.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserDeviceController {

    @Autowired
    JBH_BIZ_UserClient jbh_biz_userClient;

    @Autowired
    DataCacheClient dataCacheClient;


    /*************  用户 设备信息  ************/

    /**
     * 添加 新设备信息
     */
    @RequestMapping(value = "/addDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> addDevice(@RequestHeader("token") String token, @RequestBody UserDeviceBody body) {
        ResponseModel<UserDevice> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(token)) {
            responseModel.setErrCode("401");
            responseModel.setMessage("请登录后重试");
            return responseModel;
        }

        ResponseModel<String> tokenResponse = dataCacheClient.getUserID(token);
        if (!tokenResponse.isSuccess()) {
            responseModel.setMessage(tokenResponse.getMessage());
            return responseModel;
        }

        body.userID = tokenResponse.getData();

        return jbh_biz_userClient.addDevice(body);
    }

    /**
     * 更新 设备信息
     */
    @RequestMapping(value = "/updateDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> updateDevice(@RequestHeader("token") String token,
                                                                 @RequestBody UserDeviceBody body) {
        ResponseModel<UserDevice> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(token)) {
            responseModel.setErrCode("401");
            responseModel.setMessage("请登录后重试");
            return responseModel;
        }

        ResponseModel<String> tokenResponse = dataCacheClient.getUserID(token);
        if (!tokenResponse.isSuccess()) {
            responseModel.setMessage(tokenResponse.getMessage());
            return responseModel;
        }

        body.userID = tokenResponse.getData();

        return jbh_biz_userClient.updateDevice(body);
    }

    /**
     * 获取用户 所有设备信息
     */
    @RequestMapping(value = "/getAllDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDevice> getAllDevice(@RequestHeader("token") String token) {
        ResponseArrayModel<UserDevice> responseModel = new ResponseArrayModel<>();

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

        return jbh_biz_userClient.getAllDevice(tokenResponse.getData());
    }
}
