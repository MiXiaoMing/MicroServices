package com.microservices.data.justbehere.mysql.service;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.cache.body.ExtendBody;
import com.microservices.common.feignclient.data.cache.result.ExtendResult;
import com.microservices.common.feignclient.data.justbehere.result.ServiceDetail;
import com.microservices.common.feignclient.data.justbehere.result.ServicePrice;
import com.microservices.common.response.ResponseModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServicePriceService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private DataCacheClient dataCacheClient;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private final Logger logger = LoggerFactory.getLogger(ServicePriceService.class);


    public final static String PRE = "service_price_";
    /**
     * redis缓存时长
     */
    public final static int ttl = 40 * 60;


    /**
     * 服务价格 通过code 从缓存 获取指定数据
     */
    public List<ServicePrice> selectFromCache(String code) {
        ResponseModel<ExtendResult> responseModel = dataCacheClient.getExtend(PRE + code);
        if (responseModel.isSuccess()) {
            sendMQ(code, responseModel.getData().value);

            return JSONObject.parseArray(responseModel.getData().value, ServicePrice.class);
        }
        return null;
    }


    /**
     * 服务价格 通过code 获取指定数据
     *
     * @param code 服务价格编码
     * @return
     */
    public List<ServicePrice> selectFromMysql(String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);

        List<ServicePrice> entity = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.ServicePriceMapper.selectList", map);
        if (entity != null) {
            sendMQ(code, JSONObject.toJSONString(entity));

            return entity;
        }

        return null;
    }

    /**
     *  数据缓存，方便快速获取
     * @param code
     * @param value
     */
    private void sendMQ(String code, String value) {
        ExtendBody body = new ExtendBody();
        body.key = PRE + code;
        body.value =  value;
        body.seconds = ttl;
        amqpTemplate.convertAndSend("redis", "setExtend", body);
    }
}
