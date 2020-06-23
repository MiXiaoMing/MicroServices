package com.microservices.interfaces.justbehere.order;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.ServiceOrder;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/order")
public class ServiceOrderController {

    @Autowired
    JBH_BIZ_Client jbh_biz_client;

    @Autowired
    DataCacheClient dataCacheClient;


    /*************  服务 订单  ************/

    /**
     * 添加 新服务订单
     */
    @RequestMapping(value = "/addServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceOrder> addServiceOrder(@RequestHeader("token") String token, @RequestBody ServiceOrderBody body) {
        ResponseModel<ServiceOrder> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(token)) {
            responseModel.setErrCode("401");
            responseModel.setMessage("请登录后重试");
            return responseModel;
        }

        ResponseModel<String> tokenResponse = dataCacheClient.getUserID(token);
        if (!tokenResponse.isSuccess()) {
            responseModel.setMessage(tokenResponse.getMessage());
            return responseModel;
        }

        body.userID = tokenResponse.getData();

        return jbh_biz_client.addServiceOrder(body);
    }

    /**
     * 更新 服务订单
     */
    @RequestMapping(value = "/updateServiceOrderStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceOrder> updateServiceOrderStatus(@RequestHeader("token") String token,
                                                    @RequestBody ServiceOrderBody body) {
        ResponseModel<ServiceOrder> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(token)) {
            responseModel.setErrCode("401");
            responseModel.setMessage("请登录后重试");
            return responseModel;
        }

        ResponseModel<String> tokenResponse = dataCacheClient.getUserID(token);
        if (!tokenResponse.isSuccess()) {
            responseModel.setMessage(tokenResponse.getMessage());
            return responseModel;
        }

        if (StringUtil.isEmpty(body.id)) {
            responseModel.setMessage("订单ID为空");
            return responseModel;
        }

        if (StringUtil.isEmpty(body.status)) {
            responseModel.setMessage("订单状态为空");
            return responseModel;
        }

        return jbh_biz_client.updateServiceOrder(body);
    }

    /**
     * 获取用户 所有服务订单
     */
    @RequestMapping(value = "/getAllServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllServiceOrder(@RequestHeader("token") String token) {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        if (StringUtil.isEmpty(token)) {
            responseModel.setErrCode("401");
            responseModel.setMessage("请登录后重试");
            return responseModel;
        }

        ResponseModel<String> tokenResponse = dataCacheClient.getUserID(token);
        if (!tokenResponse.isSuccess()) {
            responseModel.setErrCode("401");
            responseModel.setMessage(tokenResponse.getMessage());
            return responseModel;
        }

        return jbh_biz_client.getAllServiceOrder(tokenResponse.getData());
    }
}
