package com.microservices.data.justbehere.mysql.service;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
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
public class ServiceClassifyService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private DataCacheClient dataCacheClient;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private final Logger logger = LoggerFactory.getLogger(ServiceClassifyService.class);


    private static final String PRE = "service_classify_";


    /**
     * 服务分类 通过code 从缓存 获取指定数据
     */
    public ServiceClassify selectFromCache(String code) {
        ResponseModel<String> serviceClassifyResponse = dataCacheClient.get(PRE + code);
        if (serviceClassifyResponse.isSuccess()) {
            return JSONObject.parseObject(serviceClassifyResponse.getData(), ServiceClassify.class);
        }
        return null;
    }


    /**
     * 服务分类 通过code 获取指定数据
     *
     * @param code 服务分类编码
     * @return
     */
    public ServiceClassify selectFromMysql(String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);

        ServiceClassify entity = sqlSessionTemplate.selectOne("com.microservices.data.justbehere.mysql.ServiceClassifyMapper.select", map);
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
