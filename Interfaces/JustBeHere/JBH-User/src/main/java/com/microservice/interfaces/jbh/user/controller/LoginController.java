package com.microservice.interfaces.jbh.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.middleplatform.SmsClient;
import com.microservices.common.response.ResponseJsonModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.microservices.common.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    SmsClient smsClient;

    /**
     * 发送验证码
     */
    @RequestMapping(value = "/sendSmsCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> sendSmsCode(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String phoneNumber = params.getString("phoneNumber");

        if (!ValidateUtil.isCellphone(phoneNumber)) {
            responseModel.setMessage("请输入正确手机号");
            return responseModel;
        }

        // 发送短信
        return smsClient.sendVerificationCode(params);
    }

    /**
     * 手机号登录
     */
    @RequestMapping(value = "/phoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> phoneNumber(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String phoneNumber = params.getString("phoneNumber");
        String smsCode = params.getString("smsCode");

        if (!ValidateUtil.isCellphone(phoneNumber)) {
            responseModel.setMessage("请输入正确手机号");
            return responseModel;
        }

        if (StringUtil.isEmpty(smsCode)) {
            responseModel.setMessage("验证码不能为空");
            return responseModel;
        }

        // 发送短信
        return smsClient.sendVerificationCode(params);
    }
}
