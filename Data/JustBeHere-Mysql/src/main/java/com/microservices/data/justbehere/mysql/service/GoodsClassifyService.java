package com.microservices.data.justbehere.mysql.service;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.justbehere.result.GoodsClassify;
import com.microservices.common.feignclient.data.justbehere.result.ServiceClassify;
import com.microservices.common.response.ResponseModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoodsClassifyService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private DataCacheClient dataCacheClient;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private final Logger logger = LoggerFactory.getLogger(GoodsClassifyService.class);


    private static final String PRE = "goods_classify_";


    /**
     * 商品分类 通过code 从缓存 获取指定数据
     */
    public GoodsClassify selectFromCache(String code) {
        ResponseModel<String> responseModel = dataCacheClient.get(PRE + code);
        if (responseModel.isSuccess()) {
            return JSONObject.parseObject(responseModel.getData(), GoodsClassify.class);
        }
        return null;
    }


    /**
     * 商品分类 通过code 获取指定数据
     *
     * @param code 商品分类编码
     * @return
     */
    public GoodsClassify selectFromMysql(String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);

        GoodsClassify entity = sqlSessionTemplate.selectOne("com.microservices.data.justbehere.mysql.GoodsClassifyMapper.select", map);
        if (entity != null) {
            // 数据缓存，方便快速获取

            JSONObject body = new JSONObject();
            body.put("key", PRE + code);
            body.put("value", JSONObject.toJSONString(entity));

            amqpTemplate.convertAndSend("redis", "set", body);

            return entity;
        }

        return null;
    }
}
