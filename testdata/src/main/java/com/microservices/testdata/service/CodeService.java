package com.microservices.testdata.service;

import com.microservices.testdata.entity.Code;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CodeService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public Code selectPlatformCode(String nick , String code ) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nick", nick);
        map.put("type", "platform");
        map.put("code" , code);

        return sqlSessionTemplate.selectOne("com.microservices.testdata.mapper.CodeMapper.selectPlatformCodeByNick", map);
    }
}
