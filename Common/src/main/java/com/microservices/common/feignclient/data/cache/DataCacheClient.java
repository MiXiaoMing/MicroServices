package com.microservices.common.feignclient.data.cache;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.cache.body.SmsCodeBody;
import com.microservices.common.feignclient.data.cache.body.TokenBody;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ClientConstants.module_data_cache)
public interface DataCacheClient {


    /********  短信 验证码  *********/

    /**
     * redis 存储短信验证码，有效时间为 5分钟
     * @param body
     * @return
     */
    @RequestMapping(value = "redis/saveSmsCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResponseModel<JSONObject> saveSmsCode(@RequestBody SmsCodeBody body);

    /**
     * redis 获取短信验证码
     * @param phoneNumber
     * @return
     */
    @RequestMapping(value = "redis/getSmsCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> getSmsCode(@RequestBody String phoneNumber);


    /********  token  *********/

    /**
     * redis 存储 token值，有效时间为 1个小时
     * @param body
     * @return
     */
    @RequestMapping(value = "redis/saveToken", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> saveToken(@RequestBody TokenBody body);

    /**
     * redis 获取 token值
     * @param userID
     * @return
     */
    @RequestMapping(value = "redis/getToken", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> getToken(@RequestBody String userID);

    /**
     * redis 获取 userID
     * @param token
     * @return
     */
    @RequestMapping(value = "redis/getUserID", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> getUserID(@RequestBody String token);
}
