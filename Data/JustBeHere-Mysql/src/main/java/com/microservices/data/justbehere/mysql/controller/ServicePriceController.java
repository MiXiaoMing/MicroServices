package com.microservices.data.justbehere.mysql.controller;

import com.microservices.common.feignclient.data.justbehere.result.ServicePrice;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/service/price")
public class ServicePriceController {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(ServicePriceController.class);


    /**
     * 服务价格 通过服务ID 获取指定数据
     *
     * @param code 服务编码
     * @return
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServicePrice> select(@RequestBody String code) {
        ResponseModel<ServicePrice> responseModel = new ResponseModel<>();


        Map<String, Object> map = new HashMap<>();
        map.put("code", code);

        ServicePrice entity = sqlSessionTemplate.selectOne("com.microservices.data.justbehere.mysql.ServicePriceMapper.select", map);
        if (entity == null) {
            responseModel.setMessage("该服务价格不存在：" + code);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entity);
        }

        return responseModel;
    }
}
