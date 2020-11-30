package com.microservices.interfaces.justbehere.order;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.business.body.CreateServiceOrderBody;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.ServiceOrder;
import com.microservices.common.feignclient.data.order.result.Order;
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
    public ResponseModel<JSONObject> addServiceOrder(@RequestHeader("userID") String userID, @RequestBody CreateServiceOrderBody body) {
        body.userID = userID;

        return jbh_biz_client.addServiceOrder(body);
    }

    /**
     * 更新 服务订单
     */
    @RequestMapping(value = "/updateServiceOrderStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Order> updateServiceOrderStatus(@RequestHeader("userID") String userID, @RequestBody JSONObject body) {

        return jbh_biz_client.updateServiceOrder(body);
    }

    /**
     * 获取 服务订单详情
     *
     * @param id 服务订单ID
     * @return
     */
    @RequestMapping(value = "/getServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getServiceOrder(@RequestBody String id) {
        return jbh_biz_client.getServiceOrder(id);
    }

    /**
     * 获取用户 所有服务订单
     */
    @RequestMapping(value = "/getAllServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllServiceOrder(@RequestHeader("userID") String userID) {

        return jbh_biz_client.getAllServiceOrder(userID);
    }

    /**
     * 获取用户 所有未完成服务订单
     */
    @RequestMapping(value = "/getAllUndoneServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllUndoenServiceOrder(@RequestHeader("userID") String userID) {

        return jbh_biz_client.getAllUnDoneServiceOrder(userID);
    }
}
