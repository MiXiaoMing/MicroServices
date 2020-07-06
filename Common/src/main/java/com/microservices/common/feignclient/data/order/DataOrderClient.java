package com.microservices.common.feignclient.data.order;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.order.body.OrderBody;
import com.microservices.common.feignclient.data.order.result.Order;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ClientConstants.module_data_order)
public interface DataOrderClient {


    /************  订单  *************/

    /**
     * 添加 新订单
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "order/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Order> addOrder(@RequestBody OrderBody body);

    /**
     * 更新 订单 状态
     *
     * @param body  通过id, status, content
     * @return
     */
    @RequestMapping(value = "order/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Order> updateOrder(@RequestBody JSONObject body);

    /**
     * 获取订单详情
     *
     * @param id  订单ID
     * @return Order
     */
    @RequestMapping(value = "order/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Order> getOrder(@RequestBody String id);

    /**
     * 获取所有订单
     *
     * @param userID
     * @return  Order
     */
    @RequestMapping(value = "order/getAll", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Order> getAllOrder(@RequestBody String userID);

    /**
     * 获取所有订单 未结束
     *
     * @param userID
     * @return Order
     */
    @RequestMapping(value = "order/getAllUnDone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Order> getAllUnDoneOrder(@RequestBody String userID);
}
