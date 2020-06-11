package com.microservices.middleplatform.user.client;

import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.user.body.CreateUserBody;
import com.microservices.common.feignclient.data.user.body.UserDeviceBody;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.feignclient.data.user.body.UserDeliveryAddressBody;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.feignclient.data.user.result.UserDevice;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ClientConstants.module_data_user)
public interface DataUserClient {

    /************  用户基本信息  *************/

    /**
     * 用户 插入新用户数据
     * @param body
     * @return
     */
    @RequestMapping(value = "user/base/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insert(@RequestBody CreateUserBody body);

    /**
     * 用户 通过手机号 获取用户基本信息
     * @param phoneNumber
     * @return
     */
    @RequestMapping(value = "user/base/selectByPhoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> getUserByPhoneNumber(@RequestBody String phoneNumber);




    /************  用户 设备信息  *************/

    /**
     * 设备信息 添加新数据
     * @param body
     * @return
     */
    @RequestMapping(value = "user/device/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insertDevice(@RequestBody UserDeviceBody body);

    /**
     * 设备信息 通过ID 获取指定数据
     *
     * @param id 表ID
     * @return
     */
    @RequestMapping(value = "user/device/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> selectDevice(@RequestBody String id);

    /**
     * 设备信息 列表获取 通过用户ID，设备ID，mac
     * @param body
     * @return
     */
    @RequestMapping(value = "user/device/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDevice> selectDeviceList(@RequestBody UserDeviceBody body);

    /**
     * 设备信息 更新
     * @param body
     * @return
     */
    @RequestMapping(value = "user/device/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> updateDevice(@RequestBody UserDeviceBody body);





    /************  用户 收货地址  *************/

    /**
     * 收货地址 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "user/deliveryAddress/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insert(@RequestBody UserDeliveryAddressBody body);

    /**
     * 收货地址 通过ID 获取指定数据
     *
     * @param id 收货地址表ID
     * @return
     */
    @RequestMapping(value = "user/deliveryAddress/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> select(@RequestBody String id);

    /**
     * 收货地址 通过 用户ID 获取该用户所有收货地址
     *
     * @param userID 用户ID
     * @return
     */
    @RequestMapping(value = "user/deliveryAddress/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDeliveryAddress> selectList(@RequestBody String userID);

    /**
     * 删除 收货地址
     * @param id
     * @return
     */
    @RequestMapping(value = "user/deliveryAddress/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> delete(@RequestBody String id);

    /**
     * 收货地址 更新
     * @param body
     * @return
     */
    @RequestMapping(value = "user/deliveryAddress/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> update(@RequestBody UserDeliveryAddressBody body);
}
