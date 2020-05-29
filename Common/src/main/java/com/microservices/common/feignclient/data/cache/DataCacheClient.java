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

    @RequestMapping(value = "/saveSmsCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResponseModel<JSONObject> saveSmsCode(@RequestBody SmsCodeBody body);

    @RequestMapping(value = "/getSmsCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> getSmsCode(@RequestBody String phoneNumber);

    /********  token  *********/

    @RequestMapping(value = "/saveToken", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> saveToken(@RequestBody TokenBody body);

    @RequestMapping(value = "/getToken", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> getToken(@RequestBody String userID);

}
