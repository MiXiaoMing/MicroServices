package com.microservices.data.justbehere.mysql.service;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.cache.body.ExtendBody;
import com.microservices.common.feignclient.data.cache.result.ExtendResult;
import com.microservices.common.feignclient.data.justbehere.result.ServiceDetail;
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
public class ServiceDetailService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private DataCacheClient dataCacheClient;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private final Logger logger = LoggerFactory.getLogger(ServiceDetailService.class);


    public final static String PRE = "service_detail_";
    /**
     * redis缓存时长
     */
    public final static int ttl = 40 * 60;


    /**
     * 服务详情 通过code 从缓存 获取指定数据
     */
    public ServiceDetail selectFromCache(String code) {
        ResponseModel<ExtendResult> responseModel = dataCacheClient.getExtend(PRE + code);
        if (responseModel.isSuccess()) {
            sendMQ(code, responseModel.getData().value);

            return JSONObject.parseObject(responseModel.getData().value, ServiceDetail.class);
        }
        return null;
    }


    /**
     * 服务详情 通过code 获取指定数据
     *
     * @param code 服务详情编码
     * @return
     */
    public ServiceDetail selectFromMysql(String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);

        ServiceDetail entity = sqlSessionTemplate.selectOne("com.microservices.data.justbehere.mysql.ServiceDetailMapper.select", map);
        if (entity != null) {
            sendMQ(code, JSONObject.toJSONString(entity));

            return entity;
        }

        return null;
    }

    /**
     * 数据缓存，方便快速获取
     *
     * @param code
     * @param value
     */
    private void sendMQ(String code, String value) {
        ExtendBody body = new ExtendBody();
        body.key = PRE + code;
        body.value = value;
        body.seconds = ttl;
        amqpTemplate.convertAndSend("redis", "setExtend", body);
    }
}
