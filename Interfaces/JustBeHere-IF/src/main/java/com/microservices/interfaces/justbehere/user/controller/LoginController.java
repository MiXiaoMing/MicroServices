package com.microservices.interfaces.justbehere.user.controller;

import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.data.cache.body.SmsCodeBody;
import com.microservices.common.feignclient.middleplatform.NotifyClient;
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
    NotifyClient smsClient;

    @Autowired
    JBH_BIZ_Client userClient;

    /**
     * 发送验证码
     * @param phoneNumber
     * @return
     */
    @RequestMapping(value = "/sendSmsCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> sendSmsCode(@RequestBody String phoneNumber) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        if (!ValidateUtil.isCellphone(phoneNumber)) {
            responseModel.setMessage("请输入正确手机号");
            return responseModel;
        }

        // 发送短信
        return smsClient.sendVerificationCode(phoneNumber);
    }

    /**
     * 手机号登录
     */
    @RequestMapping(value = "/phoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> phoneNumber(@RequestBody SmsCodeBody body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        if (!ValidateUtil.isCellphone(body.phoneNumber)) {
            responseModel.setMessage("请输入正确手机号");
            return responseModel;
        }

        if (StringUtil.isEmpty(body.smsCode)) {
            responseModel.setMessage("验证码不能为空");
            return responseModel;
        }

        // 登录
        return userClient.phoneNumber(body);
    }
}
