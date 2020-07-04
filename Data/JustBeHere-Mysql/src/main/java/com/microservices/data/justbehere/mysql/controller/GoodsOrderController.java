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

import java.util.*;

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
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insert(@RequestBody GoodsOrderBody body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        GoodsOrder entity = new GoodsOrder();
        entity.id = body.id;
        entity.goodsItems = body.goodsItems;
        entity.price = body.price;
        entity.remind = body.remind;

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
     * 商品订单 列表获取 通过id列表
     *
     * @param body  ID列表
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<GoodsOrder> selectList(@RequestBody ArrayList<String> body) {
        ResponseArrayModel<GoodsOrder> responseModel = new ResponseArrayModel<>();

        List<GoodsOrder> entities = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.GoodsOrderMapper.selectList", body);
        if (entities == null) {
            responseModel.setMessage("没有订单数据：" + body.toString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }

}
