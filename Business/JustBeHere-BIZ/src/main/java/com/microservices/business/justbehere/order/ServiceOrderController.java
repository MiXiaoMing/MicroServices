package com.microservices.business.justbehere.order;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.constants.Constants;
import com.microservices.common.feignclient.business.body.CreateServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.feignclient.data.justbehere.body.GoodsOrderBody;
import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.GoodsOrder;
import com.microservices.common.feignclient.data.justbehere.result.ServiceOrder;
import com.microservices.common.feignclient.data.justbehere.result.ServicePrice;
import com.microservices.common.feignclient.data.order.DataOrderClient;
import com.microservices.common.feignclient.data.order.body.OrderBody;
import com.microservices.common.feignclient.data.order.result.Order;
import com.microservices.common.feignclient.data.user.DataUserClient;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseJsonModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/order")
public class ServiceOrderController {

    @Autowired
    JBH_Mysql_Client jbh_mysql_client;

    @Autowired
    DataOrderClient orderClient;

    @Autowired
    DataUserClient userClient;


    private final Logger logger = LoggerFactory.getLogger(ServiceOrderController.class);


    /************ 服务订单 **************/

    /**
     * 添加 新服务订单
     *
     * @param body
     * @return Order + ServiceOrder
     */
    @RequestMapping(value = "/addServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> addServiceOrder(@RequestBody CreateServiceOrderBody body) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        // TODO: 2020/6/23 这里需要重新计算 金额
        // TODO: 2020/7/3 需要 使用 redis 去库存

        OrderBody orderBody = new OrderBody();
        orderBody.userID = body.userID;
        orderBody.deliveryAddressID = body.deliveryAddressID;
        ResponseModel<Order> orderResponseModel = orderClient.addOrder(orderBody);

        if (orderResponseModel.isSuccess()) {
            responseModel.setSuccess(true);

            Order order = orderResponseModel.getData();
            responseModel.getData().put("order", order);

            ServiceOrderBody serviceOrderBody = new ServiceOrderBody();
            serviceOrderBody.id = order.id;
            serviceOrderBody.serviceName = body.serviceName;
            serviceOrderBody.serviceCode = body.serviceCode;
            serviceOrderBody.serviceItems = body.serviceItems;
            serviceOrderBody.serviceTime = body.serviceTime;
            serviceOrderBody.remind = body.remind;
            serviceOrderBody.totalPrice = body.totalPrice;
            serviceOrderBody.discountPrice = body.discountPrice;
            serviceOrderBody.payPrice = body.payPrice;
            ResponseModel<String> addResponse = jbh_mysql_client.insertServiceOrder(serviceOrderBody);

            if (!addResponse.isSuccess()) {
                responseModel.getData().put("serviceOrder", addResponse.getData());
            }
        }

        return responseModel;
    }

    /**
     * 更新 服务订单 状态
     *
     * @param body 通过id, status, content
     * @return Order
     */
    @RequestMapping(value = "/updateServiceOrderStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Order> updateServiceOrder(@RequestBody JSONObject body) {
        return orderClient.updateOrder(body);
    }

    /**
     * 获取服务订单详情
     *
     * @param id  订单ID
     * @return Order + ServiceOrder + Array<ServicePrice> + DeliveryAddress
     */
    @RequestMapping(value = "/getServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getServiceOrder(@RequestBody String id) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        ResponseModel<Order> orderResponseModel = orderClient.getOrder(id);
        if (orderResponseModel.isSuccess()) {
            responseModel.setSuccess(true);

            ResponseModel<ServiceOrder> serviceOrderResponseModel = jbh_mysql_client.selectServiceOrder(id);
            if (serviceOrderResponseModel.isSuccess()) {
                responseModel.setData(joinServiceDetail(orderResponseModel.getData(), serviceOrderResponseModel.getData()));
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

        ResponseArrayModel<Order> orderResponseArrayModel = orderClient.getAllOrder(userID);
        if (orderResponseArrayModel.isSuccess()) {
            responseModel.setSuccess(true);

            joinServiceOrderList(orderResponseArrayModel.getData(), responseModel.getData());
        }

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

        ResponseArrayModel<Order> orderResponseArrayModel = orderClient.getAllUnDoneOrder(userID);
        if (orderResponseArrayModel.isSuccess()) {
            responseModel.setSuccess(true);

            joinServiceOrderList(orderResponseArrayModel.getData(), responseModel.getData());
        }

        return responseModel;
    }

    private void joinServiceOrderList(List<Order> orders, List<JSONObject> responseArray) {
        List<String> ids = new ArrayList<>();
        Map<String, Order> orderMap = new HashMap<>();
        for (int i = 0; i < orders.size(); ++i) {
            Order order = orders.get(i);
            ids.add(order.id);
            orderMap.put(order.id, orders.get(i));
        }

        ResponseArrayModel<ServiceOrder> serviceOrderResponseArrayModel = jbh_mysql_client.selectServiceOrderList(ids);
        if (serviceOrderResponseArrayModel.isSuccess()) {
            for (int i = 0; i < serviceOrderResponseArrayModel.getData().size(); ++i) {
                ServiceOrder serviceOrder = serviceOrderResponseArrayModel.getData().get(i);
                JSONObject jsonObject = joinServiceDetail(orderMap.get(serviceOrder.id), serviceOrder);
                responseArray.add(jsonObject);
            }
        }
    }

    /**
     * 拼接数据
     */
    private JSONObject joinServiceDetail(Order order, ServiceOrder serviceOrder) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("order", order);
        jsonObject.put("serviceOrder", serviceOrder);

        if (serviceOrder != null) {
            // 存入 ServicePrice
            ResponseArrayModel<ServicePrice> servicePrice = jbh_mysql_client.selectServicePriceList(serviceOrder.serviceCode);
            if (servicePrice.isSuccess()) {
                jsonObject.put("prices", servicePrice.getData());
            }

            // 存入 DeliveryAddress
            ResponseModel<UserDeliveryAddress> addressResponseModel = userClient.getDeliveryAddress(order.deliveryAddressID);
            if (addressResponseModel.isSuccess()) {
                jsonObject.put("deliveryAddress", addressResponseModel.getData());
            }
        }

        return jsonObject;
    }
}
