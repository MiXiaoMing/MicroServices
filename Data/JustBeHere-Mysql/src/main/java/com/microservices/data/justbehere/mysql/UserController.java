package com.microservices.data.justbehere.mysql;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.response.ResponseJsonModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.microservices.data.cache.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/redis")
public class UserController {

    @Autowired
    RedisService redisService;

    private final Logger logger	= LoggerFactory.getLogger(UserController.class);


    /************  extend : key-value   ************/

    @RequestMapping(value = "/setExtend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> setExtend(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String key = params.getString("key");
        String value = params.getString("value");
        int seconds = params.getInteger("seconds");

        if (StringUtil.isEmpty(key)) {
            responseModel.setMessage("缓存数据，key为空");
            return responseModel;
        }

        String result = redisService.setex(key, value, seconds);
        if (result != null) {
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("缓存失败");
        }

        return responseModel;
    }

    @RequestMapping(value = "/getExtend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getExtend(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String key = params.getString("key");

        if (!StringUtil.isEmpty(key)) {
            return responseModel;
        }

        long result = redisService.ttl(key);
        if (result == -2) {
            responseModel.setMessage(key + " - 不存在");
            responseModel.setErrCode("-2");
        } else if (result == -1){
            responseModel.setMessage(key + " - 已过期");
            responseModel.setErrCode("-1");
        } else {
            responseModel.setSuccess(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", key);
            jsonObject.put("value", redisService.get(key));
            jsonObject.put("ttl", result);
            responseModel.setData(jsonObject);
        }

        return responseModel;
    }

}
