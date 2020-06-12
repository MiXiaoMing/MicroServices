package com.microservices.interfaces.justbehere.user.controller;

import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.user.body.UserDeliveryAddressBody;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.microservices.common.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserDeliveryAddressController {

    @Autowired
    JBH_BIZ_Client jbh_biz_userClient;

    @Autowired
    DataCacheClient dataCacheClient;


    /*************  收货地址  ************/

    /**
     * 添加 新收货地址
     */
    @RequestMapping(value = "/addDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> addDeliveryAddress(@RequestHeader("token") String token,
                                                                 @RequestBody UserDeliveryAddressBody body) {
        ResponseModel<UserDeliveryAddress> responseModel = new ResponseModel<>();

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

        if (!ValidateUtil.isCellphone(body.phoneNumber)) {
            responseModel.setMessage("请输入正确手机号");
            return responseModel;
        }

        if (StringUtil.isEmpty(body.contact)) {
            responseModel.setMessage("请填写联系人");
            return responseModel;
        }

        if (StringUtil.isEmpty(body.region) || StringUtil.isEmpty(body.detail)) {
            responseModel.setMessage("请完善联系人地址信息");
            return responseModel;
        }

        return jbh_biz_userClient.addDeliveryAddress(body);
    }

    /**
     * 更新 收货地址
     */
    @RequestMapping(value = "/updateDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> updateDeliveryAddress(@RequestHeader("token") String token,
                                                                    @RequestBody UserDeliveryAddressBody body) {
        ResponseModel<UserDeliveryAddress> responseModel = new ResponseModel<>();

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

        return jbh_biz_userClient.updateDeliveryAddress(body);
    }

    /**
     * 删除 收货地址
     */
    @RequestMapping(value = "/deleteDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> deleteDeliveryAddress(@RequestHeader("token") String token,
                                                                    @RequestBody String id) {
        ResponseModel<UserDeliveryAddress> responseModel = new ResponseModel<>();

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

        if (StringUtil.isEmpty(id)) {
            responseModel.setMessage("收货地址ID为空");
            return responseModel;
        }

        return jbh_biz_userClient.deleteDeliveryAddress(id);
    }

    /**
     * 获取用户 所有收货地址
     */
    @RequestMapping(value = "/getAllDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDeliveryAddress> getAllDeliveryAddress(@RequestHeader("token") String token) {
        ResponseArrayModel<UserDeliveryAddress> responseModel = new ResponseArrayModel<>();

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

        return jbh_biz_userClient.getAllDeliveryAddress(tokenResponse.getData());
    }
}
