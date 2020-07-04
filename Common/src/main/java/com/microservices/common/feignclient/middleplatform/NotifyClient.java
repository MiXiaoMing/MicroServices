package com.microservices.common.feignclient.middleplatform;

import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ClientConstants.module_middle_platform_notify)
public interface NotifyClient {

    /********  sms  *********/

    /**
     * 发送验证码
     * @param phoneNumber
     * @return
     */
    @RequestMapping(value = "sms/sendVerificationCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResponseModel<String> sendVerificationCode(@RequestBody String phoneNumber);

}
