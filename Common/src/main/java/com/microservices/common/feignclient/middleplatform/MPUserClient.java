package com.microservices.common.feignclient.middleplatform;

import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.user.CreateUserBody;
import com.microservices.common.feignclient.data.user.UserResult;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ClientConstants.module_middle_platform_user)
public interface MPUserClient {

    /********  用户  *********/

    // 创建用户
    @RequestMapping(value = "user/createUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResponseModel<String> createUser(@RequestBody CreateUserBody body);

    // 获取用户
    @RequestMapping(value = "user/getUserByPhoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResponseModel<UserResult> getUserByPhoneNumber(@RequestBody String phoneNumber);
}
