package com.microservices.data.justbehere.mysql.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.result.Goods;
import com.microservices.common.feignclient.data.justbehere.result.ServiceClassify;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.microservices.data.justbehere.mysql.service.GoodsService;
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
@RequestMapping(value = "/goods")
public class GoodsController {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private GoodsService goodsService;

    private final Logger logger = LoggerFactory.getLogger(GoodsController.class);


    /**
     * 商品 通过code 获取指定数据
     *
     * @param code 商品编号
     * @return
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Goods> select(@RequestBody String code) {
        ResponseModel<Goods> responseModel = new ResponseModel<>();

        Goods entity = goodsService.selectFromCache(code);
        if (entity != null) {
            responseModel.setSuccess(true);
            responseModel.setData(entity);

            return responseModel;
        }

        entity = goodsService.selectFromMysql(code);
        if (entity != null) {
            responseModel.setSuccess(true);
            responseModel.setData(entity);

            return responseModel;
        }

        return responseModel;
    }

    /**
     * 商品 列表获取 通过classify，start（开始），number（个数）
     * 按照等级、编号升序排序，
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Goods> selectList(@RequestBody JSONObject body) {
        ResponseArrayModel<Goods> responseModel = new ResponseArrayModel<>();

        Map<String, Object> map = new HashMap<>();

        if (!StringUtil.isEmpty(body.getString("classify"))) {
            map.put("classify", body.getString("classify"));
        }

        map.put("start", body.getIntValue("start"));
        map.put("number", body.getIntValue("number"));

        List<Goods> entities = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.GoodsMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("没有商品数据：" + body.toJSONString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }
}
