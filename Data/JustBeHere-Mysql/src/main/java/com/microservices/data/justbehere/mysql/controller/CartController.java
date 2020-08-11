package com.microservices.data.justbehere.mysql.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.result.Cart;
import com.microservices.common.feignclient.data.justbehere.result.Cart;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
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
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(CartController.class);

    /**
     * 购物车 添加新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insert(@RequestBody CartBody body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        Cart entity = new Cart();
        entity.id = snowflakeIdService.getId();
        entity.userID = body.userID;
        entity.goodsID = body.goodsID;
        entity.typeID = body.typeID;
        entity.typeName = body.typeName;
        entity.number = body.number;
        entity.createTime = new Date();
        entity.delflag = "A";

        int count = sqlSessionTemplate.insert("com.microservices.data.justbehere.mysql.CartMapper.insert", entity);
        if (count > 0) {
            responseModel.setData(entity.id);
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("表数据插入失败：" + body);
        }

        return responseModel;
    }

    /**
     * 购物车 通过ID 获取指定数据
     *
     * @param id 表ID
     * @return
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> select(@RequestBody String id) {
        ResponseModel<Cart> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (!StringUtil.isEmpty(id)) {
            map.put("id", id);
        }

        Cart entity = sqlSessionTemplate.selectOne("com.microservices.data.justbehere.mysql.CartMapper.select", map);
        if (entity == null) {
            responseModel.setMessage("该用户购物车内容不存在：" + id);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entity);
        }

        return responseModel;
    }

    /**
     * 购物车 列表获取 通过userID, goodsID, typeID
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Cart> selectList(@RequestBody JSONObject body) {
        ResponseArrayModel<Cart> responseModel = new ResponseArrayModel<>();

        Map<String, Object> map = new HashMap<>();

        if (!StringUtil.isEmpty(body.getString("userID"))) {
            map.put("userID", body.getString("userID"));
        }

        if (!StringUtil.isEmpty(body.getString("goodsID"))) {
            map.put("goodsID", body.getString("goodsID"));
        }

        if (!StringUtil.isEmpty(body.getString("typeID"))) {
            map.put("typeID", body.getString("typeID"));
        }

        List<Cart> entities = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.CartMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("该用户没有购物车数据：" + body.toJSONString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }

    /**
     * 购物车 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> update(@RequestBody CartBody body) {
        ResponseModel<Cart> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (StringUtil.isEmpty(body.id)) {
            responseModel.setMessage("更新ID不能为空");
            return responseModel;
        }

        map.put("id", body.id);
        map.put("number", body.number);
        map.put("updateTime", new Date());
        map.put("delflag", "U");

        int count = sqlSessionTemplate.insert("com.microservices.data.justbehere.mysql.CartMapper.update", map);
        if (count == 0) {
            responseModel.setMessage("更新用户购物车失败");
        } else {
            return select(body.id);
        }

        return responseModel;
    }

    /**
     * 删除 购物车 具体数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> delete(@RequestBody String id) {
        ResponseModel<Cart> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (StringUtil.isEmpty(id)) {
            responseModel.setMessage("购物车更新ID不能为空");
            return responseModel;
        }

        map.put("id", id);
        map.put("updateTime", new Date());
        map.put("delflag", "D");

        int count = sqlSessionTemplate.update("com.microservices.data.justbehere.mysql.CartMapper.update", map);
        if (count == 0) {
            responseModel.setMessage("更新购物车状态失败");
        } else {
            return select(id);
        }

        return responseModel;
    }

}
