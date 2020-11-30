package com.microservices.data.cache;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.cache.body.TokenBody;
import com.microservices.common.feignclient.data.cache.body.ExtendBody;
import com.microservices.common.feignclient.data.cache.result.ExtendResult;
import com.microservices.common.constants.entity.UserRedis;
import com.microservices.data.cache.redis.RedisService;
import com.microservices.common.response.ResponseJsonModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/redis")
public class RedisController {

    @Autowired
    RedisService redisService;


    private final Logger logger = LoggerFactory.getLogger(RedisController.class);


    /************  extend : key-value   ************/

    /**
     * 通用
     *
     * @param body 通过 key-value
     * @return
     */
    @RequestMapping(value = "/set", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> set(@RequestBody JSONObject body) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        if (StringUtil.isEmpty(body.getString("key"))) {
            responseModel.setMessage("缓存数据，key为空");
            return responseModel;
        }

        String result = redisService.set(body.getString("key"), body.getString("value"));
        if (!StringUtil.isEmpty(result) && result.endsWith("OK")) {
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("缓存失败");
        }

        return responseModel;
    }

    /**
     * 获取value值
     *
     * @param key 通过key值
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> get(@RequestBody String key) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(key)) {
            logger.error(key + " - 为空");
            return responseModel;
        }

        String value = redisService.get(key);
        if (value == null) {
            responseModel.setMessage(key + " - 不存在");
            responseModel.setErrCode("-2");
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(value);
        }

        return responseModel;
    }

    /**
     * 设置扩展key-value，添加ttl
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/setExtend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> setExtend(@RequestBody ExtendBody body) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        if (StringUtil.isEmpty(body.key)) {
            responseModel.setMessage("缓存数据，key为空");
            return responseModel;
        }

        String result = redisService.setex(body.key, body.value, body.seconds);
        if (result != null) {
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("缓存失败");
        }

        return responseModel;
    }

    /**
     * 通过key值获取扩展内容
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "/getExtend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ExtendResult> getExtend(@RequestBody String key) {
        ResponseModel<ExtendResult> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(key)) {
            logger.error(key + " - 为空");
            return responseModel;
        }

        long ttl = redisService.ttl(key);
        if (ttl == -2) {
            responseModel.setMessage(key + " - 不存在");
            responseModel.setErrCode("-2");
        } else if (ttl == -1) {
            responseModel.setMessage(key + " - 已过期");
            responseModel.setErrCode("-1");
        } else {
            responseModel.setSuccess(true);

            ExtendResult result = new ExtendResult();
            result.key = key;
            result.value = redisService.get(key);
            result.ttl = ttl;

            responseModel.setData(result);
        }

        return responseModel;
    }

}
