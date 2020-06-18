package com.microservices.common.feignclient.middleplatform;

import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.user.body.CreateUserBody;
import com.microservices.common.feignclient.data.user.body.UserDeliveryAddressBody;
import com.microservices.common.feignclient.data.user.body.UserDeviceBody;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.feignclient.data.user.result.UserDevice;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ClientConstants.module_middle_platform_user)
public interface UserClient {

    /************  用户 基本信息 *************/

    // 创建用户
    @RequestMapping(value = "user/createUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResponseModel<String> createUser(@RequestBody CreateUserBody body);

    // 获取用户
    @RequestMapping(value = "user/getUserByPhoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResponseModel<UserBase> getUserByPhoneNumber(@RequestBody String phoneNumber);





    /************  用户 设备信息 *************/

    /**
     * 用户设备 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "user/device/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> addDevice(@RequestBody UserDeviceBody body);

    /**
     * 用户设备 通过ID 获取指定数据
     *
     * @param id 用户设备表ID
     * @return
     */
    @RequestMapping(value = "user/device/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> getDevice(@RequestBody String id);

    /**
     * 用户设备 通过 用户ID, mac, region 获取该用户所有用户设备
     *
     * @param body 用户ID
     * @return
     */
    @RequestMapping(value = "user/device/getList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDevice> getDeviceList(@RequestBody UserDeviceBody body);

    /**
     * 用户设备 更新
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
    @RequestMapping(value = "user/deliveryAddress/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> addDeliveryAddress(@RequestBody UserDeliveryAddressBody body);

    /**
     * 收货地址 通过ID 获取指定数据
     *
     * @param id 收货地址表ID
     * @return
     */
    @RequestMapping(value = "user/deliveryAddress/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> getDeliveryAddress(@RequestBody String id);

    /**
     * 收货地址 通过 用户ID 获取该用户所有收货地址
     *
     * @param userID 用户ID
     * @return
     */
    @RequestMapping(value = "user/deliveryAddress/getList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDeliveryAddress> getAllDeliveryAddress(@RequestBody String userID);

    /**
     * 收货地址 删除
     *
     * @param id  表ID
     * @return
     */
    @RequestMapping(value = "user/deliveryAddress/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> deleteDeliveryAddress(@RequestBody String id);

    /**
     * 收货地址 更新
     * @param body
     * @return
     */
    @RequestMapping(value = "user/deliveryAddress/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> updateDeliveryAddress(@RequestBody UserDeliveryAddressBody body);
}
