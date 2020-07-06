package com.microservices.data.justbehere.mysql.controller;

import com.microservices.common.feignclient.data.justbehere.result.ServicePrice;
import com.microservices.common.response.ResponseArrayModel;
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
import java.util.List;
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
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<ServicePrice> selectList(@RequestBody String code) {
        ResponseArrayModel<ServicePrice> responseModel = new ResponseArrayModel<>();


        Map<String, Object> map = new HashMap<>();
        map.put("code", code);

        List<ServicePrice> entities = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.ServicePriceMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("该服务价格不存在：" + code);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }
}
