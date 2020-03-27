package com.microservices.testdata.service;

import com.microservices.testdata.entity.AppCode;
import com.microservices.utils.TextUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AppCodeService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public AppCode selectAppCode(String id ,String appID, String appName, String platformCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(id)) {
            map.put("id", id);
        }
        if (!TextUtils.isEmpty(appID)) {
            map.put("appID", appID);
        }

        if (!TextUtils.isEmpty(appName)) {
            map.put("appName", appName);
        }

        if (!TextUtils.isEmpty(platformCode)) {
            map.put("platformCode", platformCode);
        }

        return sqlSessionTemplate.selectOne("com.microservices.testdata.mapper.AppCodeMapper.selectAppCode", map);
    }
}
