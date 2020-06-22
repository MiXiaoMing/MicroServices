package com.microservices.data.justbehere.mysql.controller;

import com.microservices.common.feignclient.data.justbehere.result.Setting_MainPage;
import com.microservices.common.response.ResponseArrayModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/setting/mainPage")
public class Setting_MainPageController {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(Setting_MainPageController.class);


    /**
     * 配置项 列表获取
     *
     * @param type 通过主页配置项类型
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Setting_MainPage> selectList(@RequestBody String type) {
        ResponseArrayModel<Setting_MainPage> responseModel = new ResponseArrayModel<>();

        Map<String, Object> map = new HashMap<>();
        map.put("type", type);

        List<Setting_MainPage> entities = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.Setting_MainPageMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("没有配置项数据：" + type);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }
}
