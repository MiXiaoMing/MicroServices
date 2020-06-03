package com.microservices.data.user;

import com.microservices.common.feignclient.data.user.body.CreateUserBody;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.generator.SnowflakeIdService;
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
import java.util.Map;

@RestController
@RequestMapping(value = "/user/base")
public class UserController {

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger	= LoggerFactory.getLogger(UserController.class);


    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insert(@RequestBody CreateUserBody body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        UserBase entity = new UserBase();
        entity.id = snowflakeIdService.getId();
        entity.name = body.name;
        entity.type = body.type;
        entity.phoneNumber = body.phoneNumber;
        entity.createTime = new Date();
        entity.delflag = "A";

        int count = sqlSessionTemplate.insert("com.microservices.data.user.UserBaseMapper.insert", entity);
        if (count > 0) {
            responseModel.setData(entity.id);
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("表数据插入失败：" + body);
        }

        return responseModel;
    }

    @RequestMapping(value = "/selectByPhoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> getUserByPhoneNumber(@RequestBody String phoneNumber) {
        ResponseModel<UserBase> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();
        if (!StringUtil.isEmpty(phoneNumber)) {
            map.put("phoneNumber", phoneNumber);
            UserBase user = sqlSessionTemplate.selectOne("com.microservices.data.user.UserBaseMapper.selectUserByPhoneNumber", map);
            if (user == null) {
                responseModel.setMessage("该用户不存在：" + phoneNumber);
            } else {
                responseModel.setSuccess(true);
                responseModel.setData(user);
            }
        } else {
            responseModel.setMessage("手机号为空");
        }

        return  responseModel;
    }

}
