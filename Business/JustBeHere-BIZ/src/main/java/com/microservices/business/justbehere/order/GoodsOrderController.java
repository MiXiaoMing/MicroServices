package com.microservices.business.justbehere.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microservices.common.constants.Constants;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.feignclient.data.justbehere.body.GoodsOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.Goods;
import com.microservices.common.feignclient.data.justbehere.result.GoodsOrder;
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
public class GoodsOrderController {

    @Autowired
    JBH_Mysql_Client jbh_mysql_client;

    @Autowired
    UserClient userClient;


    private final Logger logger = LoggerFactory.getLogger(GoodsOrderController.class);


    /************ 商品订单 **************/

    /**
     * 添加 新商品订单
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/addGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsOrder> addGoodsOrder(@RequestBody GoodsOrderBody body) {
        ResponseModel<GoodsOrder> responseModel = new ResponseModel<>();

        ResponseModel<String> addResponse = jbh_mysql_client.insertGoodsOrder(body);
        if (!addResponse.isSuccess()) {
            responseModel.setMessage(addResponse.getMessage());
            return responseModel;
        }

        return jbh_mysql_client.selectGoodsOrder(addResponse.getData());
    }

    /**
     * 更新 商品订单 状态
     *
     * @param body  通过id, status, content
     * @return
     */
    @RequestMapping(value = "/updateGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsOrder> updateGoodsOrder(@RequestBody JSONObject body) {
        return jbh_mysql_client.updateGoodsOrder(body);
    }

    /**
     * 获取所有商品订单
     *
     * @param userID
     * @return GoodsOrder + Array<Goods> +  DeliveryAddress
     */
    @RequestMapping(value = "/getAllGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllGoodsOrder(@RequestBody String userID) {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        if (StringUtil.isEmpty(userID)) {
            responseModel.setMessage("用户ID用空");
            return responseModel;
        }

        getGoodsOrderList(userID, "", responseModel.getData());

        responseModel.setSuccess(true);
        return responseModel;
    }

    /**
     * 获取所有商品订单 未结束
     *
     * @param userID
     * @return GoodsOrder + Array<Goods> +  DeliveryAddress
     */
    @RequestMapping(value = "/getAllUnDoneGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllUnDoneGoodsOrder(@RequestBody String userID) {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        if (StringUtil.isEmpty(userID)) {
            responseModel.setMessage("用户ID用空");
            return responseModel;
        }

        getGoodsOrderList(userID, Constants.order_status_unpay, responseModel.getData());
        getGoodsOrderList(userID, Constants.order_status_undone, responseModel.getData());
        getGoodsOrderList(userID, Constants.order_status_ing, responseModel.getData());

        responseModel.setSuccess(true);
        return responseModel;
    }

    /**
     * 获取商品订单详情
     *
     * @param id  订单ID
     * @return GoodsOrder + Array<Goods> + DeliveryAddress
     */
    @RequestMapping(value = "/getGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getGoodsOrder(@RequestBody String id) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        ResponseModel<GoodsOrder> goodsOrderResponseModel = jbh_mysql_client.selectGoodsOrder(id);
        if (goodsOrderResponseModel.isSuccess()) {
            responseModel.setSuccess(true);

            GoodsOrder goodsOrder = goodsOrderResponseModel.getData();

            // 存入 GoodsOrder
            responseModel.getData().put("goodsOrder", goodsOrder);

            // 存入 Array<Goods>
            JSONArray itemArray = new JSONArray();
            JSONArray jsonArray = JSONArray.parseArray(goodsOrder.goodsItems);
            for (int i = 0; i < jsonArray.size(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ResponseModel<Goods> goodsResponseModel = jbh_mysql_client.selectGoods(jsonObject.getString("code"));
                if (goodsResponseModel.isSuccess()) {
                    jsonObject.put("icon", goodsResponseModel.getData().icon);
                    jsonObject.put("title", goodsResponseModel.getData().title);
                    itemArray.add(jsonObject);
                }
            }
            responseModel.getData().put("items", itemArray);

            // 存入 DeliveryAddress
            ResponseModel<UserDeliveryAddress> addressResponseModel = userClient.getDeliveryAddress(goodsOrder.deliveryAddressID);
            if (addressResponseModel.isSuccess()) {
                responseModel.getData().put("deliveryAddress", addressResponseModel.getData());
            }

            // TODO: 2020/6/20 这里不应该存在，换成定时器
//            if (goodsOrder.status.equals("01")) {
//                if (System.currentTimeMillis() - goodsOrder.createTime.getTime() >= 15 * 60 * 1000) {
//                    goodsOrder.status = "05";
//                    String content = "客户原因（15分钟超时未支付，系统自动取消）";
//                    jbh_mysql_client.updateServiceOrder("", orderID, "05", content);
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



        return responseModel;
    }





    /**
     * 拼装数据
     * @param userID
     * @param status
     * @param responseArray
     */

    private void getGoodsOrderList(String userID, String status, List<JSONObject> responseArray) {
        JSONObject jsonObject = new JSONObject();
        if (!StringUtil.isEmpty(userID)) {
            jsonObject.put("userID", userID);
        }

        if (!StringUtil.isEmpty(status)) {
            jsonObject.put("status", status);
        }

        ResponseArrayModel<GoodsOrder> orderResponseArrayModel = jbh_mysql_client.selectGoodsOrderList(jsonObject);
        if (orderResponseArrayModel.isSuccess()) {
            for (int i = 0; i < orderResponseArrayModel.getData().size(); ++i) {
                JSONObject response = new JSONObject();

                GoodsOrder goodsOrder = orderResponseArrayModel.getData().get(i);

                // TODO: 2020/6/20 这里要做成 定时任务
                if (goodsOrder.status.equals(Constants.order_status_unpay)) {
                    if (System.currentTimeMillis() - goodsOrder.createTime.getTime() >= 15 * 60 * 1000) {
                        goodsOrder.status = "05";
                        goodsOrder.content = "客户原因（15分钟超时未支付，系统自动取消）";

                        JSONObject body = new JSONObject();
                        body.put("id", goodsOrder.id);
                        body.put("status", Constants.order_status_cancel);
                        body.put("content", "客户原因（15分钟超时未支付，系统自动取消）");

                        jbh_mysql_client.updateGoodsOrder(body);
                    } else {
                        goodsOrder.remainTime = 15 * 60 * 1000 - (System.currentTimeMillis() - goodsOrder.createTime.getTime());
                    }
                }

                response.put("goodsOrder", goodsOrder);


                JSONArray jsonArray = new JSONArray();

                JSONArray jsonArrayItems = JSONArray.parseArray(goodsOrder.goodsItems);
                for (int j = 0; j < jsonArrayItems.size(); ++j) {
                    JSONObject goodsObject = jsonArrayItems.getJSONObject(j);

                    ResponseModel<Goods> goodsResponseModel = jbh_mysql_client.selectGoods(goodsObject.getString("code"));
                    if (goodsResponseModel.isSuccess()) {
                        goodsObject.put("icon", goodsResponseModel.getData().icon);
                        goodsObject.put("title", goodsResponseModel.getData().title);
                        jsonArray.add(goodsObject);
                    }
                }
                response.put("items", jsonArray);

                // 存入 DeliveryAddress
                ResponseModel<UserDeliveryAddress> addressResponseModel = userClient.getDeliveryAddress(goodsOrder.deliveryAddressID);
                if (addressResponseModel.isSuccess()) {
                    response.put("deliveryAddress", addressResponseModel.getData());
                }

                responseArray.add(response);
            }
        }
    }

}
