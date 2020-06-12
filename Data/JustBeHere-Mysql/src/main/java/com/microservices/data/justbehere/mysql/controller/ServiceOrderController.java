package com.microservices.data.justbehere.mysql.controller;

import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.ServiceOrder;
import com.microservices.common.generator.SnowflakeIdService;
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

import java.sql.Timestamp;
import java.util.Date;
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
     * @param body
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insert(@RequestBody ServiceOrderBody body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        ServiceOrder entity = new ServiceOrder();
        entity.id = snowflakeIdService.getId();
        entity.userID = body.userID;
        entity.tradeID = entity.id + System.currentTimeMillis();
        entity.deliveryAddressID = body.deliveryAddressID;
        entity.serviceCode = body.serviceCode;
        entity.serviceName = body.serviceName;
        entity.serviceItems = body.serviceItems;
        entity.serviceTime = new Timestamp(body.serviceTime);
        entity.totalPrice = body.totalPrice;
        entity.discountPrice = body.discountPrice;
        entity.payPrice = body.payPrice;
        entity.remind = body.remind;
        entity.status = "01";
        entity.createTime = new Date();
        entity.delflag = "A";

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

        if (!StringUtil.isEmpty(id)) {
            map.put("id", id);
        }

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
     * 服务订单 列表获取 通过用户ID
     * @param userID
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<ServiceOrder> selectList(@RequestBody String userID) {
        ResponseArrayModel<ServiceOrder> responseModel = new ResponseArrayModel<>();

        Map<String, Object> map = new HashMap<>();

        if (!StringUtil.isEmpty(userID)) {
            map.put("userID", userID);
        }

        List<ServiceOrder> entities = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.ServiceOrderMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("该用户没有订单数据：" + userID);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }

    /**
     * 服务订单 更新
     * @param body
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceOrder> update(@RequestBody ServiceOrderBody body) {
        ResponseModel<ServiceOrder> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (StringUtil.isEmpty(body.id)) {
            responseModel.setMessage("更新ID不能为空");
            return responseModel;
        }

        map.put("id", body.id);

        if (!StringUtil.isEmpty(body.status)) {
            map.put("status", body.status);
        }

        map.put("updateTime", new Date());
        map.put("delflag", "U");

        int count = sqlSessionTemplate.insert("com.microservices.data.justbehere.mysql.ServiceOrderMapper.update", map);
        if (count == 0) {
            responseModel.setMessage("更新用户服务订单失败");
        } else {
            return select(body.id);
        }

        return responseModel;
    }

}
