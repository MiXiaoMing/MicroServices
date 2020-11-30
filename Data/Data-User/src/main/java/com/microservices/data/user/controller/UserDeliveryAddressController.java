package com.microservices.data.user.controller;

import com.microservices.common.feignclient.data.user.body.UserDeliveryAddressBody;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.data.user.service.UserDeliveryAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user/deliveryAddress")
public class UserDeliveryAddressController {

    @Autowired
    UserDeliveryAddressService deliveryAddressService;

    private final Logger logger = LoggerFactory.getLogger(UserDeliveryAddressController.class);

    /**
     * 收货地址 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> add(@RequestBody UserDeliveryAddressBody body) {
        return deliveryAddressService.insert(body);
    }

    /**
     * 收货地址 通过ID 获取指定数据
     *
     * @param id 收货地址表ID
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> get(@RequestBody String id) {
        return deliveryAddressService.select(id);
    }

    /**
     * 收货地址 通过 用户ID 获取该用户所有收货地址
     *
     * @param userID 用户ID
     * @return
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDeliveryAddress> getList(@RequestBody String userID) {
        return deliveryAddressService.selectList(userID);
    }

    /**
     * 收货地址 删除
     *
     * @param id 表ID
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> delete(@RequestBody String id) {
        return deliveryAddressService.delete(id);
    }

    /**
     * 收货地址 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> update(@RequestBody UserDeliveryAddressBody body) {
        return deliveryAddressService.update(body);
    }

}
