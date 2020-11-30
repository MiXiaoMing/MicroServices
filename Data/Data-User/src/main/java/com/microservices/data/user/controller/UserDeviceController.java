package com.microservices.data.user.controller;

import com.microservices.common.feignclient.data.user.body.UserDeviceBody;
import com.microservices.common.feignclient.data.user.result.UserDevice;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.data.user.service.UserDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user/device")
public class UserDeviceController {

    @Autowired
    UserDeviceService userDeviceService;

    private final Logger logger = LoggerFactory.getLogger(UserDeviceController.class);

    /**
     * 用户设备 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> add(@RequestBody UserDeviceBody body) {
        return userDeviceService.insert(body);
    }

    /**
     * 用户设备 通过ID 获取指定数据
     *
     * @param id 用户设备表ID
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> get(@RequestBody String id) {
        return userDeviceService.select(id);
    }

    /**
     * 用户设备 通过 用户ID, mac, region 获取该用户所有用户设备
     *
     * @param body 用户ID
     * @return
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDevice> getList(@RequestBody UserDeviceBody body) {
        return userDeviceService.selectList(body);
    }

    /**
     * 用户设备 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> update(@RequestBody UserDeviceBody body) {
        return userDeviceService.update(body);
    }

}
