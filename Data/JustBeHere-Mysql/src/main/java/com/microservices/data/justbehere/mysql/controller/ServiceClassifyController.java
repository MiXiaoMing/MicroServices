package com.microservices.data.justbehere.mysql.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.justbehere.result.GoodsClassify;
import com.microservices.common.feignclient.data.justbehere.result.ServiceClassify;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.microservices.data.justbehere.mysql.service.ServiceClassifyService;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/service/classify")
public class ServiceClassifyController {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private ServiceClassifyService serviceClassifyService;

    private final Logger logger = LoggerFactory.getLogger(ServiceClassifyController.class);


    /**
     * 服务分类 通过code 获取指定数据
     *
     * @param code 服务分类编码
     * @return
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceClassify> select(@RequestBody String code) {
        ResponseModel<ServiceClassify> responseModel = new ResponseModel<>();

        ServiceClassify serviceClassify = serviceClassifyService.selectFromCache(code);
        if (serviceClassify != null) {
            responseModel.setSuccess(true);
            responseModel.setData(serviceClassify);

            return responseModel;
        }

        serviceClassify = serviceClassifyService.selectFromMysql(code);
        if (serviceClassify != null) {
            responseModel.setSuccess(true);
            responseModel.setData(serviceClassify);

            return responseModel;
        }

        return responseModel;
    }
}
