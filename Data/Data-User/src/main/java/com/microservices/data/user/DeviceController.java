package com.microservices.data.user;

import com.microservices.common.feignclient.data.user.body.UserDeviceBody;
import com.microservices.common.feignclient.data.user.result.UserDevice;
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
@RequestMapping(value = "/user/device")
public class DeviceController {

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    /**
     * 设备信息 添加新数据
     * @param body
     * @return
     */
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

    /**
     * 设备信息 通过ID 获取指定数据
     *
     * @param id 表ID
     * @return
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> select(@RequestBody String id) {
        ResponseModel<UserDevice> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (!StringUtil.isEmpty(id)) {
            map.put("id", id);
        }

        UserDevice entity = sqlSessionTemplate.selectOne("com.microservices.data.user.UserDeviceMapper.select", map);
        if (entity == null) {
            responseModel.setMessage("该用户设备信息不存在：" + id);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entity);
        }

        return responseModel;
    }

    /**
     * 设备信息 列表获取 通过用户ID，设备ID，mac
     * @param body
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDevice> selectList(@RequestBody UserDeviceBody body) {
        ResponseArrayModel<UserDevice> responseModel = new ResponseArrayModel<>();

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

        List<UserDevice> entities = sqlSessionTemplate.selectList("com.microservices.data.user.UserDeviceMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("该用户设备不存在：" + body.toString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }

    /**
     * 设备信息 更新
     * @param body
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> update(@RequestBody UserDeviceBody body) {
        ResponseModel<UserDevice> responseModel = new ResponseModel<>();

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

        int count = sqlSessionTemplate.insert("com.microservices.data.user.UserDeviceMapper.update", map);
        if (count == 0) {
            responseModel.setMessage("更新用户设备信息失败");
        } else {
            return select(body.id);
        }

        return responseModel;
    }

}
