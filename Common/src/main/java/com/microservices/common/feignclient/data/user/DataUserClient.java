package com.microservices.common.feignclient.data.user;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.justbehere.result.ServiceTime;
import com.microservices.common.feignclient.data.user.body.UserBaseBody;
import com.microservices.common.feignclient.data.user.body.UserDeliveryAddressBody;
import com.microservices.common.feignclient.data.user.body.UserDeviceBody;
import com.microservices.common.feignclient.data.user.result.*;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Timestamp;
import java.util.List;

@FeignClient(value = ClientConstants.module_data_user)
public interface DataUserClient {


    /************  用户 基本信息 *************/

    /**
     * 用户信息 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "user/base/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> addUser(@RequestBody UserBase body);

    /**
     * 用户信息 通过ID 获取指定数据
     *
     * @param id 用户信息表ID
     * @return
     */
    @RequestMapping(value = "user/base/getByID", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> getUser(@RequestBody String id);

    /**
     * 用户信息 通过 获取指定数据
     *
     * @param body phoneNumber + type
     * @return
     */
    @RequestMapping(value = "user/base/getByPhoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> getUser(@RequestBody JSONObject body);

    /**
     * 用户信息 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "user/base/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> updateUser(@RequestBody UserBase body);


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
     *
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
     * @param id 表ID
     * @return
     */
    @RequestMapping(value = "user/deliveryAddress/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> deleteDeliveryAddress(@RequestBody String id);

    /**
     * 收货地址 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "user/deliveryAddress/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> updateDeliveryAddress(@RequestBody UserDeliveryAddressBody body);


    /************  角色  *************/

    /**
     * 角色 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "user/role/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Role> addRole(@RequestBody Role body);

    /**
     * 角色 通过ID 获取指定数据
     *
     * @param id 角色表ID
     * @return
     */
    @RequestMapping(value = "user/role/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Role> getRole(@RequestBody String id);

    /**
     * 角色 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "user/role/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Role> updateRole(@RequestBody Role body);


    /************  权限  *************/

    /**
     * 权限 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "user/permission/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Permission> addPermission(@RequestBody Permission body);

    /**
     * 权限 通过ID 获取指定数据
     *
     * @param id 权限表ID
     * @return
     */
    @RequestMapping(value = "user/permission/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Permission> getPermission(@RequestBody String id);

    /**
     * 权限 通过ID列表 获取指定数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "user/permission/getList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Permission> getPermissionList(@RequestBody List<String> body);

    /**
     * 权限 通过角色ID 获取指定数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "user/permission/getListByRole", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> getListByRole(@RequestBody String id);

    /**
     * 权限 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "user/permission/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Permission> updatePermission(@RequestBody Permission body);


    /************  角色+权限 关联关系 *************/

    /**
     * 角色+权限 插入新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "user/rolePermission/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<RolePermission> addRolePermission(@RequestBody RolePermission body);

    /**
     * 角色+权限 通过ID 获取指定数据
     *
     * @param id 角色+权限表ID
     * @return
     */
    @RequestMapping(value = "user/rolePermission/getList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<RolePermission> getRolePermission(@RequestBody String id);

    /**
     * 角色+权限 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "user/rolePermission/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<RolePermission> updateRolePermission(@RequestBody RolePermission body);


}
