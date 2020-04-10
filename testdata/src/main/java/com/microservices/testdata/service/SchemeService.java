package com.microservices.testdata.service;

import com.microservices.generator.SnowflakeIdService;
import com.microservices.testdata.entity.Scheme;
import com.microservices.testdata.response.SchemeResponse;
import com.microservices.utils.TextUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchemeService {

    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public String insertScheme(String appCode, String appVersion, String deviceModel, String systemVersion) {
        Scheme scheme = new Scheme();
        scheme.id = snowflakeIdService.getId();
        scheme.appCode = appCode;
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

    public List<SchemeResponse> selectSchemes(String appCode, String appVersion) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(appCode)) {
            map.put("appCode", appCode);
        }

        if (!TextUtils.isEmpty(appVersion)) {
            map.put("appVersion", appVersion);
        }

        return sqlSessionTemplate.selectList("com.microservices.testdata.mapper.SchemeMapper.selectSchemes", map);
    }
}
