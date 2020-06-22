package com.microservices.data.justbehere.mysql.controller;

import com.microservices.common.feignclient.data.justbehere.result.GoodsPrice;
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
@RequestMapping(value = "/goods/price")
public class GoodsPriceController {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(GoodsPriceController.class);


    /**
     * 商品价格 通过商品ID 获取指定数据
     *
     * @param goodsID 商品ID
     * @return
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsPrice> select(@RequestBody String goodsID) {
        ResponseModel<GoodsPrice> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(goodsID)) {
            return  responseModel;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("goodsID", goodsID);

        GoodsPrice entity = sqlSessionTemplate.selectOne("com.microservices.data.justbehere.mysql.GoodsPriceMapper.select", map);
        if (entity == null) {
            responseModel.setMessage("该商品价格不存在：" + goodsID);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entity);
        }

        return responseModel;
    }
}
