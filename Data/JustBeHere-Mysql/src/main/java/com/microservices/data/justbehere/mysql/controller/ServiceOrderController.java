package com.microservices.data.justbehere.mysql.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.ServiceOrder;
import com.microservices.common.generator.SnowflakeIdService;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/order/service")
public class ServiceOrderController {

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(ServiceOrderController.class);

    /**
     * 服务订单 添加新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insert(@RequestBody ServiceOrderBody body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        ServiceOrder entity = new ServiceOrder();
        entity.id = body.id;
        entity.serviceCode = body.serviceCode;
        entity.serviceName = body.serviceName;
        entity.serviceItems = body.serviceItems;
        entity.serviceTime = new Timestamp(body.serviceTime);
        entity.totalPrice = body.totalPrice;
        entity.discountPrice = body.discountPrice;
        entity.payPrice = body.payPrice;
        entity.remind = body.remind;

        int count = sqlSessionTemplate.insert("com.microservices.data.justbehere.mysql.ServiceOrderMapper.insert", entity);
        if (count > 0) {
            responseModel.setData(entity.id);
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("表数据插入失败：" + body);
        }

        return responseModel;
    }

    /**
     * 服务订单 通过ID 获取指定数据
     *
     * @param id 表ID
     * @return
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceOrder> select(@RequestBody String id) {
        ResponseModel<ServiceOrder> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        ServiceOrder entity = sqlSessionTemplate.selectOne("com.microservices.data.justbehere.mysql.ServiceOrderMapper.select", map);
        if (entity == null) {
            responseModel.setMessage("该用户服务订单不存在：" + id);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entity);
        }

        return responseModel;
    }

    /**
     * 服务订单 列表获取
     *
     * @param body 通过订单ID列表
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<ServiceOrder> selectList(@RequestBody List<String> body) {
        ResponseArrayModel<ServiceOrder> responseModel = new ResponseArrayModel<>();

        List<ServiceOrder> entities = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.ServiceOrderMapper.selectList", body);
        if (entities == null) {
            responseModel.setMessage("该用户没有订单数据：" + body.toString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }

    /**
     * 服务订单 列表获取
     *
     * @param body 通过serviceTime
     * @return
     */
    @RequestMapping(value = "/selectListByTime", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<ServiceOrder> selectList(@RequestBody JSONObject body) {
        ResponseArrayModel<ServiceOrder> responseModel = new ResponseArrayModel<>();

        Map<String, Object> map = new HashMap<>();

        Timestamp timestamp = body.getTimestamp("serviceTime");
        if (timestamp != null) {
            map.put("serviceTime", timestamp);
        }

        List<ServiceOrder> entities = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.ServiceOrderMapper.selectListByTime", map);
        if (entities == null) {
            responseModel.setMessage("该用户没有订单数据：" + body.toString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }
}
