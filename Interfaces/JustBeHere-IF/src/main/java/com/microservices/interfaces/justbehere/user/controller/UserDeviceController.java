package com.microservices.interfaces.justbehere.user.controller;

import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.user.body.UserDeviceBody;
import com.microservices.common.feignclient.data.user.result.UserDevice;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserDeviceController {

    @Autowired
    JBH_BIZ_Client jbh_biz_userClient;

    @Autowired
    DataCacheClient dataCacheClient;


    /*************  用户 设备信息  ************/

    /**
     * 添加 新设备信息
     */
    @RequestMapping(value = "/addDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> addDevice(@RequestHeader("userID") String userID, @RequestBody UserDeviceBody body) {
        ResponseModel<UserDevice> responseModel = new ResponseModel<>();

        body.userID = userID;

        return jbh_biz_userClient.addDevice(body);
    }

    /**
     * 更新 设备信息
     */
    @RequestMapping(value = "/updateDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> updateDevice(@RequestHeader("userID") String userID, @RequestBody UserDeviceBody body) {
        body.userID = userID;

        return jbh_biz_userClient.updateDevice(body);
    }

    /**
     * 获取用户 所有设备信息
     */
    @RequestMapping(value = "/getAllDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDevice> getAllDevice(@RequestHeader("userID") String userID) {

        return jbh_biz_userClient.getAllDevice(userID);
    }
}
