package com.microservices.user.controller;

import com.microservices.entities.User;
import com.microservices.support.ResponseModel;
import com.microservices.user.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    /************  用户   ************/

    //获取用户基本信息
    @HystrixCommand(fallbackMethod = "infoFall")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseModel<User> info(@RequestParam("id") String id) {
        ResponseModel<User> responseModel = new ResponseModel<>();
        responseModel.setSuccess(true);
        responseModel.setData(userService.getUserInfo(id));
        return responseModel;
    }

    public ResponseModel<User> infoFall(String id) {
        ResponseModel<User> responseModel = new ResponseModel<>();
        responseModel.setSuccess(true);
        responseModel.setData(userService.getUserInfo(id));
        return responseModel;
    }
}
