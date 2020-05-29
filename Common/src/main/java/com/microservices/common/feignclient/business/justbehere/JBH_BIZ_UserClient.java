package com.microservices.common.feignclient.business.justbehere;

import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.cache.body.SmsCodeBody;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ClientConstants.module_jbh_biz_user)
public interface JBH_BIZ_UserClient {

    // 登录
    @RequestMapping(value = "login/phoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResponseModel<String> phoneNumber(@RequestBody SmsCodeBody body);

}