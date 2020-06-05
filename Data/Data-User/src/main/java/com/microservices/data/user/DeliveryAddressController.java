package com.microservices.data.user;

import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.feignclient.data.user.body.UserDeliveryAddressBody;
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

/**
 * 用户 收货地址
 */
@RestController
@RequestMapping(value = "/user/deliveryAddress")
public class DeliveryAddressController {

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(DeliveryAddressController.class);


    /**
     * 收货地址 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insert(@RequestBody UserDeliveryAddressBody body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        UserDeliveryAddress entity = new UserDeliveryAddress();
        entity.id = snowflakeIdService.getId();
        entity.userID = body.userID;
        entity.contact = body.contact;
        entity.phoneNumber = body.phoneNumber;
        entity.region = body.region;
        entity.detail = body.detail;
        entity.createTime = new Date();
        entity.delflag = "A";

        int count = sqlSessionTemplate.insert("com.microservices.data.user.UserDeliveryAddressMapper.insert", entity);
        if (count > 0) {
            responseModel.setData(entity.id);
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("表数据插入失败：" + body);
        }

        return responseModel;
    }

    /**
     * 收货地址 通过ID 获取指定数据
     *
     * @param id 收货地址表ID
     * @return
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> select(@RequestBody String id) {
        ResponseModel<UserDeliveryAddress> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (!StringUtil.isEmpty(id)) {
            map.put("id", id);
        }

        UserDeliveryAddress entity = sqlSessionTemplate.selectOne("com.microservices.data.user.UserDeliveryAddressMapper.select", map);
        if (entity == null) {
            responseModel.setMessage("该用户地址信息不存在：" + id);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entity);
        }

        return responseModel;
    }

    /**
     * 收货地址 通过 用户ID 获取该用户所有收货地址
     *
     * @param userID 用户ID
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDeliveryAddress> selectList(@RequestBody String userID) {
        ResponseArrayModel<UserDeliveryAddress> responseModel = new ResponseArrayModel<>();

        Map<String, Object> map = new HashMap<>();

        if (StringUtil.isEmpty(userID)) {
            return responseModel;
        }

        map.put("userID", userID);

        List<UserDeliveryAddress> entities = sqlSessionTemplate.selectList("com.microservices.data.user.UserDeliveryAddressMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("该用户收货地址不存在：" + userID);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }

    /**
     * 收货地址 更新
     * @param body
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> update(@RequestBody UserDeliveryAddressBody body) {
        ResponseModel<UserDeliveryAddress> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (StringUtil.isEmpty(body.id)) {
            responseModel.setMessage("更新ID不能为空");
            return responseModel;
        }

        map.put("id", body.id);

        if (!StringUtil.isEmpty(body.userID)) {
            map.put("userID", body.userID);
        }

        if (!StringUtil.isEmpty(body.contact)) {
            map.put("contact", body.contact);
        }

        if (!StringUtil.isEmpty(body.region)) {
            map.put("region", body.region);
        }

        if (!StringUtil.isEmpty(body.detail)) {
            map.put("detail", body.detail);
        }

        map.put("updateTime", new Date());
        map.put("delflag", "U");

        int count = sqlSessionTemplate.update("com.microservices.data.user.UserDeliveryAddressMapper.update", map);
        if (count == 0) {
            responseModel.setMessage("更新用户收货地址失败");
        } else {
            return select(body.id);
        }

        return responseModel;
    }


    /**
     * 删除 收货地址
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> delete(@RequestBody String id) {
        ResponseModel<UserDeliveryAddress> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (StringUtil.isEmpty(id)) {
            responseModel.setMessage("收货地址更新ID不能为空");
            return responseModel;
        }

        map.put("id", id);
        map.put("updateTime", new Date());
        map.put("delflag", "D");

        int count = sqlSessionTemplate.update("com.microservices.data.user.UserDeliveryAddressMapper.update", map);
        if (count == 0) {
            responseModel.setMessage("更新用户收货地址失败");
        } else {
            return select(id);
        }

        return responseModel;
    }

}
