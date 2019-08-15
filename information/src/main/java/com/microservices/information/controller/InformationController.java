package com.microservices.information.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.client.UserClient;
import com.microservices.entities.User;
import com.microservices.support.ResponseModel;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/info")
public class InformationController {
    @Autowired
    private UserClient userClient;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "testFall")
    public ResponseModel<JSONObject> test() {
        ResponseModel<JSONObject> responseModel = new ResponseModel<JSONObject>();

        JSONObject row = new JSONObject();
        row.put("test", "1234");
        responseModel.setData(row);
        responseModel.setSuccess(true);
        return responseModel;
    }

    public ResponseModel<JSONObject> testFall() {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();
        responseModel.setSuccess(true);
        responseModel.setData(new JSONObject());
        return responseModel;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel<JSONObject> list(HttpServletRequest request) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<JSONObject>();

        JSONObject row = new JSONObject();
        addUserInfo(row, "1");
        responseModel.setData(row);
        responseModel.setSuccess(true);
        return responseModel;
    }

    private void addUserInfo(JSONObject object, String id) {
        ResponseModel<User> responseModel = userClient.baseInfo(id);
        if (responseModel != null && responseModel.isSuccess() && responseModel.getData() != null) {
            User user = responseModel.getData();
            object.put("userID", id);
            object.put("telphone", user.telephone);
            object.put("createTime", user.createTime);
        }
    }

}
