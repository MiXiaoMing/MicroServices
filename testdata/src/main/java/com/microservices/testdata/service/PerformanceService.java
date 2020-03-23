package com.microservices.testdata.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microservices.generator.SnowflakeIdService;
import com.microservices.testdata.entity.Performance;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PerformanceService {

    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int insertPerformanceBatch(String schemeID, JSONArray array) {
        List<Performance> entities = new ArrayList<>();

        for (int i = 0; i < array.size(); ++i) {
            JSONObject object = array.getJSONObject(i);

            Performance entity = new Performance();
            entity.id = snowflakeIdService.getId();
            entity.schemeID = schemeID;
            entity.cpuRatio = Float.valueOf(object.getString("cpuUse"));
            entity.memorySize = Float.valueOf(object.getString("memoryUse"));
            entity.operateTime = object.getDate("recordTime");
            entity.flag = "A";

            entities.add(entity);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("performances", entities);

        return sqlSessionTemplate.insert("com.microservices.testdata.mapper.PerformanceMapper.insertPerformanceBatch", map);
    }

    public List<Performance> selectPerformances(String schemeID) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("schemeID", schemeID);

        return sqlSessionTemplate.selectList("com.microservices.testdata.mapper.PerformanceMapper.selectPerformancesBySchemeID", map);
    }

    public float averageCpu(String schemeID) {
        if (selectPerformances(schemeID).size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("schemeID", schemeID);

            return sqlSessionTemplate.selectOne("com.microservices.testdata.mapper.PerformanceMapper.averageCpu", map);
        } else {
            return -1;
        }
    }

    public float averageMemory(String schemeID) {
        if (selectPerformances(schemeID).size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("schemeID", schemeID);

            return sqlSessionTemplate.selectOne("com.microservices.testdata.mapper.PerformanceMapper.averageMemory", map);
        } else {
            return -1;
        }
    }
}
