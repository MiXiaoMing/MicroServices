package com.microservices.middleplatform.user;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.user.body.UserBaseBody;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.microservices.middleplatform.user.client.DataUserClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user/base")
public class UserController {

    @Autowired
    DataUserClient dataUserClient;

    private final Logger logger	= LoggerFactory.getLogger(UserController.class);

    /**
     * 用户信息 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> add(@RequestBody UserBase body) {
        return dataUserClient.insertUser(body);
    }

    /**
     * 用户信息 通过ID 获取指定数据
     *
     * @param id 用户信息表ID
     * @return
     */
    @RequestMapping(value = "/getByID", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> get(@RequestBody String id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", id);
        return dataUserClient.selectUser(jsonObject);
    }

    /**
     * 用户信息 通过 获取指定数据
     *
     * @param body phoneNumber + type
     * @return
     */
    @RequestMapping(value = "/getByPhoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> get(@RequestBody JSONObject body) {
        ResponseModel<UserBase> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(body.getString("phoneNumber"))) {
            responseModel.setMessage("phoneNumber为空");
            return responseModel;
        }

        if (StringUtil.isEmpty(body.getString("type"))) {
            responseModel.setMessage("type为空");
            return responseModel;
        }

        return dataUserClient.selectUser(body);
    }

    /**
     * 用户信息 更新
     * @param body
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> update(@RequestBody UserBase body) {
        return dataUserClient.updateUser(body);
    }
}
