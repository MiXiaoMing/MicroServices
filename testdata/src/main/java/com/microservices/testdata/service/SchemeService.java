package com.microservices.testdata.service;

import com.microservices.testdata.entity.Scheme;
import org.coffee.falsework.core.generator.SnowflakeIdService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SchemeService {

    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public String insertScheme(String appID, String appVersion, String deviceModel, String systemVersion) {
        Scheme scheme = new Scheme();
        scheme.id = snowflakeIdService.getId();
        scheme.appID = appID;
        scheme.appVersion = appVersion;
        scheme.deviceModel = deviceModel;
        scheme.systemVersion = systemVersion;
        scheme.startTime = new Date();
        scheme.flag = "A";

        int count = sqlSessionTemplate.insert("com.microservices.testdata.mapper.SchemeMapper.insertScheme", scheme);
        if (count > 0) {
            return scheme.id;
        } else {
            return null;
        }
    }

    public int updateScheme(String id) {
        Scheme scheme = new Scheme();
        scheme.id = id;
        scheme.endTime = new Date();
        scheme.flag = "U";

        return sqlSessionTemplate.update("com.microservices.testdata.mapper.SchemeMapper.updateScheme", scheme);
    }
}
