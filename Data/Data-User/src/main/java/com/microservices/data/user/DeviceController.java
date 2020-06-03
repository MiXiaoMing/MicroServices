package com.microservices.data.user;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.user.result.UserDevice;
import com.microservices.common.feignclient.data.user.body.UserDeviceBody;
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
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger	= LoggerFactory.getLogger(DeviceController.class);


    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insert(@RequestBody UserDeviceBody body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        UserDevice entity = new UserDevice();
        entity.id = snowflakeIdService.getId();
        entity.userID = body.userID;
        entity.did = body.did;
        entity.mac = body.mac;
        entity.region = body.region;
        entity.createTime = new Date();
        entity.delflag = "A";

        int count = sqlSessionTemplate.insert("com.microservices.data.user.UserDeviceMapper.insert", entity);
        if (count > 0) {
            responseModel.setData(entity.id);
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("表数据插入失败：" + body);
        }

        return responseModel;
    }

    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> getUserDevice(@RequestBody UserDeviceBody body) {
        ResponseModel<UserDevice> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (!StringUtil.isEmpty(body.userID)) {
            map.put("userID", body.userID);
        }

        if (!StringUtil.isEmpty(body.did)) {
            map.put("did", body.did);
        }

        if (!StringUtil.isEmpty(body.mac)) {
            map.put("mac", body.mac);
        }

        UserDevice userDevice = sqlSessionTemplate.selectOne("com.microservices.data.user.UserDeviceMapper.select", map);
        if (userDevice == null) {
            responseModel.setMessage("该用户设备不存在：" + body.toString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(userDevice);
        }

        return  responseModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> update(@RequestBody UserDeviceBody body) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (StringUtil.isEmpty(body.id)) {
            responseModel.setMessage("更新ID不能为空");
            return responseModel;
        }

        map.put("id", body.id);

        if (!StringUtil.isEmpty(body.userID)) {
            map.put("userID", body.userID);
        }

        if (!StringUtil.isEmpty(body.region)) {
            map.put("region", body.region);
        }

        map.put("updateTime", new Date());
        map.put("delflag", "U");

        int count = sqlSessionTemplate.insert("com.microservices.data.user.UserDeviceMapper.updateDeliveryAddress", map);
        if (count == 0) {
            responseModel.setMessage("更新用户设备信息失败");
        } else {
            responseModel.setSuccess(true);
        }

        return  responseModel;
    }

}
