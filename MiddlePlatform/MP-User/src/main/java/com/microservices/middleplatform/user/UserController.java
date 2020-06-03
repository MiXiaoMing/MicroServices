package com.microservices.middleplatform.user;

import com.microservices.common.feignclient.data.user.body.CreateUserBody;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.response.ResponseModel;
import com.microservices.middleplatform.user.client.DataUserClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    DataUserClient dataUserClient;

    private final Logger logger	= LoggerFactory.getLogger(UserController.class);


    @RequestMapping(value = "/createUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> createUser(@RequestBody CreateUserBody body) {
        return dataUserClient.insert(body);
    }

    @RequestMapping(value = "/getUserByPhoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> getUserByPhoneNumber(@RequestBody String phoneNumber) {
        return dataUserClient.getUserByPhoneNumber(phoneNumber);
    }

}
