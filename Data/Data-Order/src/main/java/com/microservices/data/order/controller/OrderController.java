package com.microservices.data.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.constants.Constants;
import com.microservices.common.feignclient.data.order.body.OrderBody;
import com.microservices.common.feignclient.data.order.result.Order;
import com.microservices.common.feignclient.data.user.DataUserClient;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.data.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    DataUserClient userClient;


    private final Logger logger = LoggerFactory.getLogger(Order.class);


    /************ 订单 **************/

    /**
     * 添加 新订单
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Order> add(@RequestBody OrderBody body) {
        ResponseModel<Order> responseModel = new ResponseModel<>();

        ResponseModel<String> addResponse = orderService.insert(body);
        if (!addResponse.isSuccess()) {
            responseModel.setMessage(addResponse.getMessage());
            return responseModel;
        }

        return orderService.select(addResponse.getData());
    }

    /**
     * 更新 订单 状态
     *
     * @param body 通过id, status, content
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Order> update(@RequestBody JSONObject body) {
        Order order = new Order();
        order.id = body.getString("id");
        order.status = body.getString("status");
        order.content = body.getString("content");
        order.updateTime = new Date();
        order.delflag = "U";

        return orderService.update(order);
    }

    /**
     * 获取所有订单
     *
     * @param userID
     * @return Order
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Order> getAllOrder(@RequestBody String userID) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", userID);

        return orderService.selectList(jsonObject);
    }

    /**
     * 获取所有订单 未结束
     *
     * @param userID
     * @return Order
     */
    @RequestMapping(value = "/getAllUnDone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Order> getAllUnDoneOrder(@RequestBody String userID) {
        ResponseArrayModel<Order> responseModel = new ResponseArrayModel<>();

        ArrayList<Order> orders = new ArrayList<>();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", userID);

        // TODO: 2020/7/3 需要优化性能
        {
            jsonObject.put("status", Constants.order_status_unpay);
            ResponseArrayModel<Order> orderResponseArrayModel = orderService.selectList(jsonObject);
            if (orderResponseArrayModel.isSuccess()) {
                orders.addAll(orderResponseArrayModel.getData());
            }
        }
        {
            jsonObject.put("status", Constants.order_status_undone);
            ResponseArrayModel<Order> orderResponseArrayModel = orderService.selectList(jsonObject);
            if (orderResponseArrayModel.isSuccess()) {
                orders.addAll(orderResponseArrayModel.getData());
            }
        }
        {
            jsonObject.put("status", Constants.order_status_ing);
            ResponseArrayModel<Order> orderResponseArrayModel = orderService.selectList(jsonObject);
            if (orderResponseArrayModel.isSuccess()) {
                orders.addAll(orderResponseArrayModel.getData());
            }
        }

        responseModel.setData(orders);
        responseModel.setSuccess(true);
        return responseModel;
    }

    /**
     * 获取订单详情
     *
     * @param id 订单ID
     * @return Order
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Order> getOrder(@RequestBody String id) {

        return orderService.select(id);
    }


    // TODO: 2020/6/20 这里不应该存在，换成定时器
//            if (Order.status.equals("01")) {
//                if (System.currentTimeMillis() - Order.createTime.getTime() >= 15 * 60 * 1000) {
//                    Order.status = "05";
//                    String content = "客户原因（15分钟超时未支付，系统自动取消）";
//                    orderService.updateServiceOrder("", orderID, "05", content);
//                } else {
//                    object.put("payTime", 15 * 60 * 1000 - (System.currentTimeMillis() - order.createTime.getTime()));
//                }
//            }
//
//            if (order.status.equals("05")) {
//                OrderStatus orderStatus = orderStatusService.getOrderStatus(orderID, "05");
//                object.put("content", orderStatus.content);
//                object.put("cancelTime", TimeUtils.dateFormat(orderStatus.createTime));
//            }

}
