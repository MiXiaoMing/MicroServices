package com.microservices.data.justbehere.mysql.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.result.Carousel;
import com.microservices.common.feignclient.data.justbehere.result.Goods;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
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
@RequestMapping(value = "/carousel")
public class CarouselController {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(CarouselController.class);


    /**
     * 轮播图 列表获取
     *
     * @param body 通过carouselCode, code
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Carousel> selectList(@RequestBody JSONObject body) {
        ResponseArrayModel<Carousel> responseModel = new ResponseArrayModel<>();

        Map<String, Object> map = new HashMap<>();

        if (!StringUtil.isEmpty(body.getString("carouselCode"))) {
            map.put("carouselCode", body.getString("carouselCode"));
        }

        if (!StringUtil.isEmpty(body.getString("code"))) {
            map.put("code", body.getString("code"));
        }

        List<Carousel> entities = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.CarouselMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("没有轮播图数据：" + body.toJSONString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }
}
