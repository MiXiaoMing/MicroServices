package com.microservices.data.order.service;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.order.body.OrderBody;
import com.microservices.common.feignclient.data.order.result.Order;
import com.microservices.common.generator.SnowflakeIdService;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     * 订单服务 添加新数据
     *
     * @param body
     * @return
     */
    public ResponseModel<String> insert(OrderBody body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        Order entity = new Order();
        entity.id = snowflakeIdService.getId();
        entity.userID = body.userID;
        entity.tradeID = entity.id + System.currentTimeMillis();
        entity.deliveryAddressID = body.deliveryAddressID;
        entity.status = "01";
        entity.createTime = new Date();
        entity.delflag = "A";

        int count = sqlSessionTemplate.insert("com.microservices.data.order.OrderMapper.insert", entity);
        if (count > 0) {
            responseModel.setData(entity.id);
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("表数据插入失败：" + body);
        }

        return responseModel;
    }

    /**
     * 订单服务 通过ID 获取指定数据
     *
     * @param id 表ID
     * @return
     */
    public ResponseModel<Order> select(String id) {
        ResponseModel<Order> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        Order entity = sqlSessionTemplate.selectOne("com.microservices.data.order.OrderMapper.select", map);
        if (entity == null) {
            responseModel.setMessage("该用户订单服务不存在：" + id);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entity);
        }

        return responseModel;
    }

    /**
     * 订单服务 列表获取 通过userID, status
     *
     * @param body
     * @return
     */
    public ResponseArrayModel<Order> selectList(JSONObject body) {
        ResponseArrayModel<Order> responseModel = new ResponseArrayModel<>();

        Map<String, Object> map = new HashMap<>();

        if (!StringUtil.isEmpty(body.getString("userID"))) {
            map.put("userID", body.getString("userID"));
        }

        if (!StringUtil.isEmpty(body.getString("status"))) {
            map.put("status", body.getString("status"));
        }

        List<Order> entities = sqlSessionTemplate.selectList("com.microservices.data.order.OrderMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("该用户没有订单数据：" + body.toJSONString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }

    /**
     * 订单服务 更新
     *
     * @param body 通过Order
     * @return
     */
    public ResponseModel<Order> update(Order body) {
        ResponseModel<Order> responseModel = new ResponseModel<>();

        int count = sqlSessionTemplate.update("com.microservices.data.order.OrderMapper.update", body);
        if (count == 0) {
            responseModel.setMessage("更新用户订单服务失败");
        } else {
            return select(body.id);
        }

        return responseModel;
    }

}
