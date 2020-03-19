package com.microservices.testdata.service;

import com.microservices.testdata.entity.Code;
import com.microservices.testdata.entity.Page;
import org.coffee.falsework.core.generator.SnowflakeIdService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CodeService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public Code selectPlatformCode(String nick) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nick", nick);
        map.put("type", "platform");

        return sqlSessionTemplate.selectOne("com.microservices.testdata.mapper.CodeMapper.selectPlatformCodeByNick", map);
    }
}
