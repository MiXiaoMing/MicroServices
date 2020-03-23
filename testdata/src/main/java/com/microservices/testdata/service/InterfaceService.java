package com.microservices.testdata.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microservices.generator.SnowflakeIdService;
import com.microservices.testdata.entity.Interface;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InterfaceService {

    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int insertInterfaceBatch(String schemeID, JSONArray array) {
        List<Interface> interfaces = new ArrayList<>();

        for (int i = 0; i < array.size(); ++i) {
            JSONObject object = array.getJSONObject(i);

            Interface inter = new Interface();
            inter.id = snowflakeIdService.getId();
            inter.schemeID = schemeID;
            inter.url = object.getString("name");
            inter.responseTime = Float.valueOf(object.getString("duration"));
            inter.operateTime = new Date(Long.valueOf(object.getString("recordTime")));
            inter.flag = "A";

            interfaces.add(inter);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("interfaces", interfaces);

        return sqlSessionTemplate.insert("com.microservices.testdata.mapper.InterfaceMapper.insertInterfaceBatch", map);
    }

    public List<Interface> selectInterfaces(String schemeID) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("schemeID", schemeID);

        return sqlSessionTemplate.selectList("com.microservices.testdata.mapper.InterfaceMapper.selectInterfacesBySchemeID", map);
    }

    public float averageTime(String schemeID) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("schemeID", schemeID);

        return sqlSessionTemplate.selectOne("com.microservices.testdata.mapper.InterfaceMapper.averageTime", map);
    }
}
