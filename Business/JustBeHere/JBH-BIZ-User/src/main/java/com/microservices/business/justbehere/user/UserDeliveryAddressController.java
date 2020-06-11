package com.microservices.business.justbehere.user;

import com.microservices.common.feignclient.data.user.body.UserDeliveryAddressBody;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.feignclient.middleplatform.MPUserClient;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserDeliveryAddressController {

    @Autowired
    MPUserClient mpUserClient;


    private final Logger logger = LoggerFactory.getLogger(UserDeliveryAddressController.class);


    /************ 收货地址 **************/

    /**
     * 添加新收货地址
     * @param body
     * @return
     */
    @RequestMapping(value = "/addDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> getAllDeliveryAddress(@RequestBody UserDeliveryAddressBody body) {
        ResponseModel<UserDeliveryAddress> responseModel = new ResponseModel<>();

        ResponseModel<String> addResponse = mpUserClient.addDeliveryAddress(body);
        if (!addResponse.isSuccess()) {
            responseModel.setMessage(addResponse.getMessage());
            return responseModel;
        }

        return mpUserClient.getDeliveryAddress(addResponse.getData());
    }

    /**
     * 更新 收货地址
     * @param body
     * @return
     */
    @RequestMapping(value = "/updateDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> updateDeliveryAddress(@RequestBody UserDeliveryAddressBody body) {
        return mpUserClient.updateDeliveryAddress(body);
    }

    /**
     * 删除 收货地址
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> deleteDeliveryAddress(@RequestBody String id) {
        return mpUserClient.deleteDeliveryAddress(id);
    }

    /**
     * 获取所有收货地址
     * @param userID
     * @return
     */
    @RequestMapping(value = "/getAllDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDeliveryAddress> getAllDeliveryAddress(@RequestBody String userID) {
        ResponseArrayModel<UserDeliveryAddress> responseModel = new ResponseArrayModel<>();

        if (StringUtil.isEmpty(userID)) {
            responseModel.setMessage("用户ID用空");
            return responseModel;
        }

        return mpUserClient.getAllDeliveryAddress(userID);
    }

}
