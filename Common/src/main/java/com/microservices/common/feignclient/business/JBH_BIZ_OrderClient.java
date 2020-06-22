package com.microservices.common.feignclient.business;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.cache.body.SmsCodeBody;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.body.GoodsOrderBody;
import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.*;
import com.microservices.common.feignclient.data.user.body.UserDeliveryAddressBody;
import com.microservices.common.feignclient.data.user.body.UserDeviceBody;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.feignclient.data.user.result.UserDevice;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface JBH_BIZ_OrderClient {


    /************  服务 订单  *************/

    /**
     * 添加 新服务订单
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "order/addServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceOrder> addServiceOrder(@RequestBody ServiceOrderBody body);

    /**
     * 更新 服务订单 状态
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "order/updateServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceOrder> updateServiceOrder(@RequestBody ServiceOrderBody body);

    /**
     * 获取所有服务订单
     *
     * @param userID
     * @return ServiceOrder + DeliveryAddress
     */
    @RequestMapping(value = "order/getAllServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllServiceOrder(@RequestBody String userID);

    /**
     * 获取所有服务订单 未结束
     *
     * @param userID
     * @return ServiceOrder + DeliveryAddress
     */
    @RequestMapping(value = "order/getAllUnDoneServiceOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllUnDoneServiceOrder(@RequestBody String userID);




    /************  商品 订单  *************/

    /**
     * 添加 新商品订单
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/addGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsOrder> addGoodsOrder(@RequestBody GoodsOrderBody body);


    /**
     * 更新 商品订单 状态
     *
     * @param body  通过id, status, content
     * @return
     */
    @RequestMapping(value = "/updateGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsOrder> updateGoodsOrder(@RequestBody JSONObject body);

    /**
     * 获取所有商品订单
     *
     * @param userID
     * @return GoodsOrder + Array<Goods>
     */
    @RequestMapping(value = "/getAllGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllGoodsOrder(@RequestBody String userID);

    /**
     * 获取所有商品订单 未结束
     *
     * @param userID
     * @return GoodsOrder + Array<Goods>
     */
    @RequestMapping(value = "/getAllUnDoneGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllUnDoneGoodsOrder(@RequestBody String userID);



}