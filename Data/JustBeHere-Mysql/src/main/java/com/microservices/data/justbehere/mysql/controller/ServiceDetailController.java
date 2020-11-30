package com.microservices.data.justbehere.mysql.controller;

import com.microservices.common.feignclient.data.justbehere.result.ServiceDetail;
import com.microservices.common.response.ResponseModel;
import com.microservices.data.justbehere.mysql.service.ServiceDetailService;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/service/detail")
public class ServiceDetailController {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private ServiceDetailService serviceDetailService;

    private final Logger logger = LoggerFactory.getLogger(ServiceDetailController.class);


    /**
     * 服务 通过code 获取指定数据
     *
     * @param code 服务编号
     * @return
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceDetail> select(@RequestBody String code) {
        ResponseModel<ServiceDetail> responseModel = new ResponseModel<>();

        ServiceDetail entity = serviceDetailService.selectFromCache(code);
        if (entity != null) {
            responseModel.setSuccess(true);
            responseModel.setData(entity);

            return responseModel;
        }

        entity = serviceDetailService.selectFromMysql(code);
        if (entity != null) {
            responseModel.setSuccess(true);
            responseModel.setData(entity);

            return responseModel;
        }

        responseModel.setMessage("该服务不存在：" + code);
        return responseModel;
    }
}
