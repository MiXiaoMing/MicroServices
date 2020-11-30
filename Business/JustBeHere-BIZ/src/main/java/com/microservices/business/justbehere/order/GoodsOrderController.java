package com.microservices.business.justbehere.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.business.body.CreateGoodsOrderBody;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.feignclient.data.justbehere.body.GoodsOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.Goods;
import com.microservices.common.feignclient.data.justbehere.result.GoodsCollection;
import com.microservices.common.feignclient.data.justbehere.result.GoodsOrder;
import com.microservices.common.feignclient.data.order.DataOrderClient;
import com.microservices.common.feignclient.data.order.body.OrderBody;
import com.microservices.common.feignclient.data.order.result.Order;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.feignclient.data.user.DataUserClient;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseJsonModel;
import com.microservices.common.response.ResponseModel;
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
public class GoodsOrderController {

    @Autowired
    JBH_Mysql_Client jbh_mysql_client;

    @Autowired
    DataUserClient userClient;

    @Autowired
    DataOrderClient orderClient;


    private final Logger logger = LoggerFactory.getLogger(GoodsOrderController.class);


    /************ 商品订单 **************/

    /**
     * 添加 新商品订单
     *
     * @param body
     * @return Order + GoodsOrder
     */
    @RequestMapping(value = "/addGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> addGoodsOrder(@RequestBody CreateGoodsOrderBody body) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        // TODO: 2020/7/3 需要 减去库存
        // TODO: 2020/7/3 库存 要在 redis操作

        OrderBody orderBody = new OrderBody();
        orderBody.userID = body.userID;
        orderBody.deliveryAddressID = body.deliveryAddressID;
        ResponseModel<Order> orderResponseModel = orderClient.addOrder(orderBody);

        if (orderResponseModel.isSuccess()) {
            responseModel.setSuccess(true);

            Order order = orderResponseModel.getData();
            responseModel.getData().put("order", order);

            GoodsOrderBody goodsOrderBody = new GoodsOrderBody();
            goodsOrderBody.id = order.id;
            goodsOrderBody.goodsItems = body.goodsItems;
            goodsOrderBody.price = body.price;
            goodsOrderBody.remind = body.remind;
            ResponseModel<String> addResponse = jbh_mysql_client.insertGoodsOrder(goodsOrderBody);

            if (!addResponse.isSuccess()) {
                responseModel.getData().put("goodsOrder", addResponse.getData());
                return responseModel;
            }
        }

        return responseModel;
    }

    /**
     * 更新 商品订单 状态
     *
     * @param body 通过id, status, content
     * @return Order
     */
    @RequestMapping(value = "/updateGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Order> updateGoodsOrder(@RequestBody JSONObject body) {
        return orderClient.updateOrder(body);
    }

    /**
     * 获取所有商品订单
     *
     * @param userID
     * @return Order + GoodsOrder + Array<Goods> +  DeliveryAddress
     */
    @RequestMapping(value = "/getAllGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllGoodsOrder(@RequestBody String userID) {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        ResponseArrayModel<Order> orderResponseArrayModel = orderClient.getAllOrder(userID);
        if (orderResponseArrayModel.isSuccess()) {
            responseModel.setSuccess(true);

            joinGoodsOrderList(orderResponseArrayModel.getData(), responseModel.getData());
        }

        return responseModel;
    }

    /**
     * 获取所有商品订单 未结束
     *
     * @param userID
     * @return Order + GoodsOrder + Array<Goods> +  DeliveryAddress
     */
    @RequestMapping(value = "/getAllUnDoneGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllUnDoneGoodsOrder(@RequestBody String userID) {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        ResponseArrayModel<Order> orderResponseArrayModel = orderClient.getAllUnDoneOrder(userID);
        if (orderResponseArrayModel.isSuccess()) {
            responseModel.setSuccess(true);

            joinGoodsOrderList(orderResponseArrayModel.getData(), responseModel.getData());
        }

        return responseModel;
    }

    /**
     * 获取商品订单详情
     *
     * @param id 订单ID
     * @return Order + GoodsOrder + Array<Goods> + DeliveryAddress
     */
    @RequestMapping(value = "/getGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getGoodsOrder(@RequestBody String id) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        ResponseModel<Order> orderResponseModel = orderClient.getOrder(id);
        if (orderResponseModel.isSuccess()) {
            responseModel.setSuccess(true);

            ResponseModel<GoodsOrder> goodsOrderResponseModel = jbh_mysql_client.selectGoodsOrder(id);
            if (goodsOrderResponseModel.isSuccess()) {
                responseModel.setData(joinGoodsOrderDetail(orderResponseModel.getData(), goodsOrderResponseModel.getData()));
            }
        }

        return responseModel;
    }


    private void joinGoodsOrderList(List<Order> orders, List<JSONObject> responseArray) {
        List<String> ids = new ArrayList<>();
        Map<String, Order> orderMap = new HashMap<>();
        for (int i = 0; i < orders.size(); ++i) {
            Order order = orders.get(i);
            ids.add(order.id);
            orderMap.put(order.id, orders.get(i));
        }

        ResponseArrayModel<GoodsOrder> goodsOrderResponseArrayModel = jbh_mysql_client.selectGoodsOrderList(ids);
        if (goodsOrderResponseArrayModel.isSuccess()) {
            for (int i = 0; i < goodsOrderResponseArrayModel.getData().size(); ++i) {
                GoodsOrder goodsOrder = goodsOrderResponseArrayModel.getData().get(i);
                JSONObject jsonObject = joinGoodsOrderDetail(orderMap.get(goodsOrder.id), goodsOrder);
                responseArray.add(jsonObject);
            }
        }
    }


    /**
     * 拼装数据 - 商品订单详情
     */
    private JSONObject joinGoodsOrderDetail(Order order, GoodsOrder goodsOrder) {
        JSONObject response = new JSONObject();

        response.put("order", order);
        response.put("goodsOrder", goodsOrder);


        JSONArray jsonArray = new JSONArray();

        JSONArray jsonArrayItems = JSONArray.parseArray(goodsOrder.goodsItems);
        for (int j = 0; j < jsonArrayItems.size(); ++j) {
            JSONObject goodsObject = jsonArrayItems.getJSONObject(j);

            ResponseModel<GoodsCollection> goodsResponseModel = jbh_mysql_client.selectGoods(goodsObject.getString("code"));
            if (goodsResponseModel.isSuccess()) {
                goodsObject.put("icon", goodsResponseModel.getData().goods.icon);
                goodsObject.put("title", goodsResponseModel.getData().goods.title);
                jsonArray.add(goodsObject);
            }
        }
        response.put("items", jsonArray);

        // 存入 DeliveryAddress
        ResponseModel<UserDeliveryAddress> addressResponseModel = userClient.getDeliveryAddress(order.deliveryAddressID);
        if (addressResponseModel.isSuccess()) {
            response.put("deliveryAddress", addressResponseModel.getData());
        }

        return response;
    }

}
