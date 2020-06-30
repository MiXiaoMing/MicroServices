package com.microservices.data.justbehere.mysql.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.result.Carousel;
import com.microservices.common.feignclient.data.justbehere.result.Cart;
import com.microservices.common.feignclient.data.justbehere.result.Feedback;
import com.microservices.common.generator.SnowflakeIdService;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/feedback")
public class FeedbackController {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    private final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    /**
     * 用户反馈 添加新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insert(@RequestBody Feedback body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        body.id = snowflakeIdService.getId();
        body.createTime = new Date();

        int count = sqlSessionTemplate.insert("com.microservices.data.justbehere.mysql.FeedbackMapper.insert", body);
        if (count > 0) {
            responseModel.setData(body.id);
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("表数据插入失败：" + body);
        }

        return responseModel;
    }

    /**
     * 用户反馈 列表获取
     *  按照 反馈时间 降序 排序，
     *
     * @param body 通过 start（开始），number（个数）
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Feedback> selectList(@RequestBody JSONObject body) {
        ResponseArrayModel<Feedback> responseModel = new ResponseArrayModel<>();

        Map<String, Object> map = new HashMap<>();

        map.put("start", body.getIntValue("start"));
        map.put("number", body.getIntValue("number"));

        List<Feedback> entities = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.FeedbackMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("没有用户反馈数据：" + body.toJSONString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }
}
