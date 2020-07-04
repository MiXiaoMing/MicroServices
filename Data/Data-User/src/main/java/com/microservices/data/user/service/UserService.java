package com.microservices.data.user.service;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.generator.SnowflakeIdService;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    /**
     * 用户信息  新增
     * @param body
     * @return
     */
    public ResponseModel<UserBase> insert(UserBase body) {
        ResponseModel<UserBase> responseModel = new ResponseModel<>();

        body.id = snowflakeIdService.getId();
        body.createTime = new Date();
        body.delflag = "A";

        int count = sqlSessionTemplate.insert("com.microservices.data.user.UserBaseMapper.insert", body);
        if (count > 0) {
            responseModel.setData(body);
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("表数据插入失败：" + body);
        }

        return responseModel;
    }

    /**
     * 用户信息 通过 userID 或者 phoneNumber + type
     *
     * @param body
     * @return
     */
    public ResponseModel<UserBase> select(JSONObject body) {
        ResponseModel<UserBase> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (!StringUtil.isEmpty(body.getString("phoneNumber"))) {
            map.put("phoneNumber", body.getString("phoneNumber"));
        }

        if (!StringUtil.isEmpty(body.getString("type"))) {
            map.put("type", body.getString("type"));
        }

        if (!StringUtil.isEmpty(body.getString("userID"))) {
            map.put("id", body.getString("userID"));
        }

        UserBase user = sqlSessionTemplate.selectOne("com.microservices.data.user.UserBaseMapper.select", map);
        if (user == null) {
            responseModel.setMessage("该用户不存在：" + body.toString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(user);
        }

        return responseModel;
    }

    /**
     * 个人信息 更新
     * @param body
     * @return
     */
    public ResponseModel<UserBase> update(UserBase body) {
        ResponseModel<UserBase> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(body.id)) {
            responseModel.setMessage("更新ID不能为空");
            return responseModel;
        }

        body.updateTime = new Date();
        body.delflag = "U";

        int count = sqlSessionTemplate.update("com.microservices.data.user.UserBaseMapper.update", body);
        if (count == 0) {
            responseModel.setMessage("更新用户设备信息失败");
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(body);
        }

        return responseModel;
    }
}
