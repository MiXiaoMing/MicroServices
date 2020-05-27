package com.microservice.interfaces.jbh.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.response.ResponseModel;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

//    @Autowired
//    private UserService userService;

    /************  用户   ************/

    //获取用户基本信息
    @HystrixCommand(fallbackMethod = "infoFall")
    @RequestMapping(value = "/info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> info(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();
        responseModel.setSuccess(true);
//        responseModel.setData(userService.getUserInfo(id));
        return responseModel;
    }

    public ResponseModel<JSONObject> infoFall(JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();
        responseModel.setSuccess(true);
//        responseModel.setData(userService.getUserInfo(id));
        return responseModel;
    }
}
