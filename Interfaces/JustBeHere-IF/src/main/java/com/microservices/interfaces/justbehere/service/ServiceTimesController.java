package com.microservices.interfaces.justbehere.service;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/service")
public class ServiceTimesController {

    @Autowired
    JBH_BIZ_Client jbh_biz_client;



    /*************  服务 时间 ************/

    /**
     * 获取 服务时间 列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getTimeList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getGoodsList() {
        return jbh_biz_client.getTimeList();
    }
}
