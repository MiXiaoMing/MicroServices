package com.microservices.common.feignclient.data.user;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ClientConstants.module_data_cache)
public interface UserClient {

    /********  redis  *********/

    // extend：key-value
    @RequestMapping(value = "redis/setExtend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResponseModel<JSONObject> redisSetExtend(@RequestBody JSONObject params);

    @RequestMapping(value = "redis/getExtend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResponseModel<JSONObject> redisGetExtend(@RequestBody JSONObject params);
}
