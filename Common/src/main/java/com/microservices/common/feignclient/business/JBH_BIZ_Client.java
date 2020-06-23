package com.microservices.common.feignclient.business;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.cache.body.SmsCodeBody;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.body.GoodsOrderBody;
import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.*;
import com.microservices.common.feignclient.data.user.body.UserDeliveryAddressBody;
import com.microservices.common.feignclient.data.user.body.UserDeviceBody;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.feignclient.data.user.result.UserDeliveryAddress;
import com.microservices.common.feignclient.data.user.result.UserDevice;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ClientConstants.module_jbh_biz)
public interface JBH_BIZ_Client {


    /************  轮播图  *************/

    /**
     * 轮播图 列表获取
     *
     * @param body 通过carouselCode（轮播图位置编码）, code（对应编码）
     * @return
     */
    @RequestMapping(value = "carousel/getAllCarousel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Carousel> getAllCarousel(@RequestBody JSONObject body);





    /************  配置项 主页  *************/


    /**
     * 配置项 列表获取  五大项
     *
     * @return
     */
    @RequestMapping(value = "setting/mainPage/getFive", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getFive();

    /**
     * 配置项 列表获取  十小项
     *
     * @return
     */
    @RequestMapping(value = "setting/mainPage/getTen", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getTen();

    /**
     * 配置项 列表获取  推荐
     *
     * @return
     */
    @RequestMapping(value = "setting/mainPage/getRecommend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getRecommend();

    /**
     * 配置项 列表获取  推荐服务分类
     *
     * @return
     */
    @RequestMapping(value = "setting/mainPage/getRecommendServiceClassify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getRecommendServiceClassify();





    /************  用户 登录  *************/

    // 登录
    @RequestMapping(value = "login/phoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResponseModel<String> phoneNumber(@RequestBody SmsCodeBody body);




    /************  用户 信息  *************/

    /**
     * 获取所有个人信息
     *
     * @param userID
     * @return
     */
    @RequestMapping(value = "user/getUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserBase> getUser(@RequestBody String userID);


    /************  用户 收货地址  *************/

    /**
     * 添加 新设备信息
     * @param body
     * @return
     */
    @RequestMapping(value = "user/addDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> addDevice(@RequestBody UserDeviceBody body);

    /**
     * 更新 设备信息
     * @param body
     * @return
     */
    @RequestMapping(value = "user/updateDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDevice> updateDevice(@RequestBody UserDeviceBody body);

    /**
     * 获取所有设备信息
     * @param userID
     * @return
     */
    @RequestMapping(value = "user/getAllDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDevice> getAllDevice(@RequestBody String userID);





    /************  用户 收货地址  *************/

    /**
     * 添加新收货地址
     * @param body
     * @return
     */
    @RequestMapping(value = "user/addDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> addDeliveryAddress(@RequestBody UserDeliveryAddressBody body);

    /**
     * 更新 收货地址
     * @param body
     * @return
     */
    @RequestMapping(value = "user/updateDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> updateDeliveryAddress(@RequestBody UserDeliveryAddressBody body);

    /**
     * 删除 收货地址
     * @param id 表ID
     * @return
     */
    @RequestMapping(value = "user/deleteDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<UserDeliveryAddress> deleteDeliveryAddress(@RequestBody String id);

    /**
     * 收货地址 通过 用户ID 获取所有收货地址
     * @param userID
     * @return
     */
    @RequestMapping(value = "user/getAllDeliveryAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<UserDeliveryAddress> getAllDeliveryAddress(@RequestBody String userID);





    /************  商品  *************/

    /**
     * 获取所有商品分类
     *
     * @return
     */
    @RequestMapping(value = "goods/getAllGoodsClassify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<GoodsClassify> getAllGoodsClassify();

    /**
     * 获取某类商品
     *
     * @param body  classify（商品分类），start（开始位置），number（数量）
     * @return
     */
    @RequestMapping(value = "goods/getGoodsList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getGoodsList(@RequestBody JSONObject body);


    /**
     * 获取商品详情
     *
     * @param code 商品编号
     * @return
     */
    @RequestMapping(value = "/getGoods", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getGoodsList(@RequestBody String code);





    /************  服务  *************/

    /**
     * 获取服务分类详情
     *
     * @param code 服务分类编号
     * @return  ServiceClassify + Link ServiceClassify
     */
    @RequestMapping(value = "service/getServiceClassify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getServiceClassify(@RequestBody String code);

    /**
     * 获取服务分类详情
     *
     * @param code 服务分类编号
     * @return  Service + ServiceDetail
     */
    @RequestMapping(value = "service/getService", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getService(@RequestBody String code);






    /************  购物车  *************/

    /**
     * 添加 新购物车
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "cart/addToCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> addToCart(@RequestBody CartBody body);

    /**
     * 更新 购物车 状态
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "cart/updateToCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> updateToCart(@RequestBody CartBody body);

    /**
     * 删除 从购物车
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "cart/deleteFromCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> deleteFromCart(@RequestBody String id);

    /**
     * 获取所有购物车数据
     *
     * @param userID
     * @return
     */
    @RequestMapping(value = "cart/getAllFromCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Cart> getAllFromCart(@RequestBody String userID);




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