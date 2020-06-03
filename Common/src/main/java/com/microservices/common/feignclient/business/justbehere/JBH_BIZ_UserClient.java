package com.microservices.common.feignclient.business.justbehere;

import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.cache.body.SmsCodeBody;
import com.microservices.common.feignclient.data.user.body.UserDeliveryAddressBody;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ClientConstants.module_jbh_biz_user)
public interface JBH_BIZ_UserClient {

    // 登录
    @RequestMapping(value = "login/phoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResponseModel<String> phoneNumber(@RequestBody SmsCodeBody body);


    /************  用户 收货地址  *************/

    /**
     * 添加新收货地址
     * @param body
     * @return
     */
    @RequestMapping(value = "user/addDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> addDeliveryAddress(@RequestBody UserDeliveryAddressBody body);

    /**
     * 更新 收货地址
     * @param body
     * @return
     */
    @RequestMapping(value = "user/updateDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> updateDeliveryAddress(@RequestBody UserDeliveryAddressBody body);

    /**
     * 删除 收货地址
     * @param id 表ID
     * @return
     */
    @RequestMapping(value = "/deleteDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> deleteDeliveryAddress(@RequestBody String id);

    /**
     * 收货地址 通过 用户ID 获取所有收货地址
     * @param userID
     * @return
     */
    @RequestMapping(value = "user/getAllDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDeliveryAddress> getAllDeliveryAddress(@RequestBody String userID);
}