package com.microservices.client;

import com.microservices.Constants;
import com.microservices.entities.User;
import com.microservices.support.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = Constants.module_user)
public interface UserClient {

    @RequestMapping(value = "user/info", method = RequestMethod.GET)
    ResponseModel<User> baseInfo(@RequestParam("id") String id);

}
