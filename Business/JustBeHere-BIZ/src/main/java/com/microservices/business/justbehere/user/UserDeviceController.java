package com.microservices.business.justbehere.user;

import com.microservices.common.feignclient.data.user.body.UserDeviceBody;
import com.microservices.common.feignclient.data.user.result.UserDevice;
import com.microservices.common.feignclient.middleplatform.UserClient;
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
public class UserDeviceController {

    @Autowired
    UserClient mpUserClient;


    private final Logger logger = LoggerFactory.getLogger(UserDeviceController.class);


    /************ 设备信息 **************/

    /**
     * 添加 新设备信息
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/addDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> addDevice(@RequestBody UserDeviceBody body) {
        ResponseModel<UserDevice> responseModel = new ResponseModel<>();

        ResponseModel<String> addResponse = mpUserClient.addDevice(body);
        if (!addResponse.isSuccess()) {
            responseModel.setMessage(addResponse.getMessage());
            return responseModel;
        }

        return mpUserClient.getDevice(addResponse.getData());
    }

    /**
     * 更新 设备信息
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/updateDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> updateDevice(@RequestBody UserDeviceBody body) {
        return mpUserClient.updateDevice(body);
    }

    /**
     * 获取所有设备信息
     *
     * @param userID
     * @return
     */
    @RequestMapping(value = "/getAllDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDevice> getAllDevice(@RequestBody String userID) {
        ResponseArrayModel<UserDevice> responseModel = new ResponseArrayModel<>();

        if (StringUtil.isEmpty(userID)) {
            responseModel.setMessage("用户ID用空");
            return responseModel;
        }

        UserDeviceBody body = new UserDeviceBody();
        body.userID = userID;

        return mpUserClient.getDeviceList(body);
    }

}
