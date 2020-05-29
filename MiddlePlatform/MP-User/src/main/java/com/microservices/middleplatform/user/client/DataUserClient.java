package com.microservices.middleplatform.user.client;

import com.microservices.common.feignclient.data.user.CreateUserBody;
import com.microservices.common.feignclient.data.user.UserResult;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "data-user")
public interface DataUserClient {

    @RequestMapping(value = "user/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insert(@RequestBody CreateUserBody body);

    @RequestMapping(value = "user/selectByPhoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserResult> getUserByPhoneNumber(@RequestBody String phoneNumber);
}
