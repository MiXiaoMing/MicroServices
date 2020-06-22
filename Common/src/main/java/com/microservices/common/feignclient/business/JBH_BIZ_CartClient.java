package com.microservices.common.feignclient.business;

import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.result.Cart;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface JBH_BIZ_CartClient {


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
}