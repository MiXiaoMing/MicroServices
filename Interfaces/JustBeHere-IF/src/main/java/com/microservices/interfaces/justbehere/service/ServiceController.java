package com.microservices.interfaces.justbehere.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.data.justbehere.result.GoodsClassify;
import com.microservices.common.feignclient.data.justbehere.result.ServiceClassify;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/service")
public class ServiceController {

    @Autowired
    JBH_BIZ_Client jbh_biz_client;



    /*************  服务 分类 ************/

    /**
     * 获取 所有服务分类
     */
    @RequestMapping(value = "/getServiceClassify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getServiceClassify(@RequestBody String code) {
        return jbh_biz_client.getServiceClassify(code);
    }

    /**
     * 获取 服务详情
     *
     * @param code  服务编码
     * @return
     */
    @RequestMapping(value = "/getService", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getGoodsList(@RequestBody String code) {
        return jbh_biz_client.getService(code);
    }
}
