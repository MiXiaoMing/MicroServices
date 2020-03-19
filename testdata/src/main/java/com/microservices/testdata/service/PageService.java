package com.microservices.testdata.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microservices.testdata.entity.Page;
import org.coffee.falsework.core.generator.SnowflakeIdService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PageService {

    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int insertPageBatch(String schemeID, JSONArray array) {
        List<Page> pages = new ArrayList<>();

        for (int i = 0; i < array.size(); ++i) {
            JSONObject object = array.getJSONObject(i);

            Page page = new Page();
            page.id = snowflakeIdService.getId();
            page.schemeID = schemeID;
            page.pageName = object.getString("name");
            page.responseTime = Float.valueOf(object.getString("duration"));
            page.operateTime = new Date(Long.valueOf(object.getString("recordTime")));
            page.flag = "A";

            pages.add(page);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pages", pages);

        return sqlSessionTemplate.insert("com.microservices.testdata.mapper.PageMapper.insertPageBatch", map);
    }

    public List<Page> selectPages(String schemeID) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("schemeID", schemeID);

        return sqlSessionTemplate.selectList("com.microservices.testdata.mapper.PageMapper.selectPagesBySchemeID", map);
    }

    public float averageTime(String schemeID) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("schemeID", schemeID);

        return sqlSessionTemplate.selectOne("com.microservices.testdata.mapper.PageMapper.averageTime", map);
    }
}
