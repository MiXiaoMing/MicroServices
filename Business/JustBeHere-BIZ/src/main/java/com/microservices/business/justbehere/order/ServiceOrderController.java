package com.microservices.business.justbehere.order;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.constants.Constants;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.ServiceOrder;
import com.microservices.common.feignclient.data.justbehere.result.ServicePrice;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.feignclient.middleplatform.UserClient;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseJsonModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/order")
public class ServiceOrderController {

    @Autowired
    JBH_Mysql_Client serviceClient;

    @Autowired
    UserClient userClient;


    private final Logger logger = LoggerFactory.getLogger(ServiceOrderController.class);


    /************ 服务订单 **************/

    /**
     * 添加 新服务订单
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/addServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceOrder> addServiceOrder(@RequestBody ServiceOrderBody body) {
        ResponseModel<ServiceOrder> responseModel = new ResponseModel<>();

        // TODO: 2020/6/23 这里需要重新计算 金额

        ResponseModel<String> addResponse = serviceClient.insertServiceOrder(body);
        if (!addResponse.isSuccess()) {
            responseModel.setMessage(addResponse.getMessage());
            return responseModel;
        }

        return serviceClient.selectServiceOrder(addResponse.getData());
    }

    /**
     * 更新 服务订单 状态
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/updateServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceOrder> updateServiceOrder(@RequestBody ServiceOrderBody body) {
        return serviceClient.updateServiceOrder(body);
    }

    /**
     * 获取服务订单详情
     *
     * @param id  订单ID
     * @return ServiceOrder + Array<ServicePrice> + DeliveryAddress
     */
    @RequestMapping(value = "/getServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getServiceOrder(@RequestBody String id) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        ResponseModel<ServiceOrder> orderResponseModel = serviceClient.selectServiceOrder(id);
        if (orderResponseModel.isSuccess()) {
            responseModel.setSuccess(true);

            ServiceOrder serviceOrder = orderResponseModel.getData();

            // 存入 ServiceOrder
            responseModel.getData().put("serviceOrder", serviceOrder);

            if (serviceOrder != null) {
                // 存入 ServicePrice
                ResponseArrayModel<ServicePrice> servicePrice = serviceClient.selectServicePriceList(serviceOrder.serviceCode);
                if (servicePrice.isSuccess()) {
                    responseModel.getData().put("prices", servicePrice.getData());
                }

                // 存入 DeliveryAddress
                ResponseModel<UserDeliveryAddress> addressResponseModel = userClient.getDeliveryAddress(serviceOrder.deliveryAddressID);
                if (addressResponseModel.isSuccess()) {
                    responseModel.getData().put("deliveryAddress", addressResponseModel.getData());
                }
            }
        }

        return responseModel;
    }

    /**
     * 获取所有服务订单
     *
     * @param userID
     * @return ServiceOrder + DeliveryAddress
     */
    @RequestMapping(value = "/getAllServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllServiceOrder(@RequestBody String userID) {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        if (StringUtil.isEmpty(userID)) {
            responseModel.setMessage("用户ID用空");
            return responseModel;
        }

        getServerOrderList(userID, "", responseModel.getData());

        responseModel.setSuccess(true);
        return responseModel;
    }


    /**
     * 获取所有服务订单 未结束
     *
     * @param userID
     * @return ServiceOrder + DeliveryAddress
     */
    @RequestMapping(value = "/getAllUnDoneServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllUnDoneServiceOrder(@RequestBody String userID) {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        if (StringUtil.isEmpty(userID)) {
            responseModel.setMessage("用户ID用空");
            return responseModel;
        }

        getServerOrderList(userID, Constants.order_status_unpay, responseModel.getData());
        getServerOrderList(userID, Constants.order_status_undone, responseModel.getData());
        getServerOrderList(userID, Constants.order_status_ing, responseModel.getData());

        responseModel.setSuccess(true);
        return responseModel;
    }

    private void getServerOrderList(String userID, String status, List<JSONObject> responseArray) {
        JSONObject jsonObject = new JSONObject();
        if (!StringUtil.isEmpty(userID)) {
            jsonObject.put("userID", userID);
        }

        if (!StringUtil.isEmpty(status)) {
            jsonObject.put("status", status);
        }

        ResponseArrayModel<ServiceOrder> orderResponseArrayModel = serviceClient.selectServiceOrderList(jsonObject);
        if (orderResponseArrayModel.isSuccess()) {
            for (int i = 0; i < orderResponseArrayModel.getData().size(); ++i) {
                JSONObject response = new JSONObject();

                ServiceOrder serviceOrder = orderResponseArrayModel.getData().get(i);

                // TODO: 2020/6/20 这里要做成 定时任务
                if (serviceOrder.status.equals(Constants.order_status_unpay)) {
                    if (System.currentTimeMillis() - serviceOrder.createTime.getTime() >= 15 * 60 * 1000) {
                        serviceOrder.status = "05";
                        serviceOrder.content = "客户原因（15分钟超时未支付，系统自动取消）";

                        ServiceOrderBody body = new ServiceOrderBody();
                        body.id = serviceOrder.id;
                        body.status = "05";
                        body.content = "客户原因（15分钟超时未支付，系统自动取消）";

                        serviceClient.updateServiceOrder(body);
                    } else {
                        serviceOrder.remainTime = 15 * 60 * 1000 - (System.currentTimeMillis() - serviceOrder.createTime.getTime());
                    }
                }

                response.put("serviceOrder", serviceOrder);

                ResponseModel<UserDeliveryAddress> addressResponse = userClient.getDeliveryAddress(serviceOrder.deliveryAddressID);
                if (addressResponse.isSuccess()) {
                    response.put("deliveryAddress", addressResponse.getData());
                }

                responseArray.add(response);
            }
        }
    }

}
