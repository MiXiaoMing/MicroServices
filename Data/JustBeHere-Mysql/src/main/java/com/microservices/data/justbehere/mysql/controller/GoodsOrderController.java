package com.microservices.data.justbehere.mysql.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.body.GoodsOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.GoodsOrder;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/order/goods")
public class GoodsOrderController {

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(GoodsOrderController.class);

    /**
     * 商品订单 添加新数据
     * @param body
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insert(@RequestBody GoodsOrderBody body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        GoodsOrder entity = new GoodsOrder();
        entity.id = snowflakeIdService.getId();
        entity.userID = body.userID;
        entity.tradeID = entity.id + System.currentTimeMillis();
        entity.deliveryAddressID = body.deliveryAddressID;
        entity.goodsItems = body.goodsItems;
        entity.price = body.price;
        entity.remind = body.remind;
        entity.status = "01";
        entity.createTime = new Date();
        entity.delflag = "A";

        int count = sqlSessionTemplate.insert("com.microservices.data.justbehere.mysql.GoodsOrderMapper.insert", entity);
        if (count > 0) {
            responseModel.setData(entity.id);
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("表数据插入失败：" + body);
        }

        return responseModel;
    }

    /**
     * 商品订单 通过ID 获取指定数据
     *
     * @param id 表ID
     * @return
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsOrder> select(@RequestBody String id) {
        ResponseModel<GoodsOrder> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (!StringUtil.isEmpty(id)) {
            map.put("id", id);
        }

        GoodsOrder entity = sqlSessionTemplate.selectOne("com.microservices.data.justbehere.mysql.GoodsOrderMapper.select", map);
        if (entity == null) {
            responseModel.setMessage("该用户商品订单不存在：" + id);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entity);
        }

        return responseModel;
    }

    /**
     * 商品订单 列表获取 通过userID, status
     * @param body
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<GoodsOrder> selectList(@RequestBody JSONObject body) {
        ResponseArrayModel<GoodsOrder> responseModel = new ResponseArrayModel<>();

        Map<String, Object> map = new HashMap<>();

        if (!StringUtil.isEmpty(body.getString("userID"))) {
            map.put("userID", body.getString("userID"));
        }

        if (!StringUtil.isEmpty(body.getString("status"))) {
            map.put("status", body.getString("status"));
        }

        List<GoodsOrder> entities = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.GoodsOrderMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("该用户没有订单数据：" + body.toJSONString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }

    /**
     * 商品订单 更新
     * @param body 通过id, status, content
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsOrder> update(@RequestBody JSONObject body) {
        ResponseModel<GoodsOrder> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (StringUtil.isEmpty(body.getString("id"))) {
            responseModel.setMessage("更新ID不能为空");
            return responseModel;
        }

        map.put("id", body.getString("id"));

        if (!StringUtil.isEmpty(body.getString("status"))) {
            map.put("status", body.getString("status"));
        }

        if (!StringUtil.isEmpty(body.getString("content"))) {
            map.put("content", body.getString("content"));
        }

        map.put("updateTime", new Date());
        map.put("delflag", "U");

        int count = sqlSessionTemplate.insert("com.microservices.data.justbehere.mysql.GoodsOrderMapper.update", map);
        if (count == 0) {
            responseModel.setMessage("更新用户商品订单失败");
        } else {
            return select(body.getString("id"));
        }

        return responseModel;
    }

}
