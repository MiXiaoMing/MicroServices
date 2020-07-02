package com.microservices.interfaces.justbehere.cart;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.result.Cart;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseJsonModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    JBH_BIZ_Client jbh_biz_client;

    @Autowired
    DataCacheClient dataCacheClient;


    /*************  购物车  ************/

    /**
     * 添加 新购物车
     */
    @RequestMapping(value = "/addToCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> addToCart(@RequestHeader("userID") String userID, @RequestBody CartBody body) {

        body.userID = userID;
        return jbh_biz_client.addToCart(body);
    }

    /**
     * 更新 购物车
     */
    @RequestMapping(value = "/updateToCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> updateToCart(@RequestBody CartBody body) {
        ResponseModel<Cart> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(body.id)) {
            responseModel.setMessage("购物车ID为空");
            return responseModel;
        }

        if (body.number <= 0) {
            responseModel.setMessage("购物数量不能必须大于0");
            return responseModel;
        }

        return jbh_biz_client.updateToCart(body);
    }

    /**
     * 删除 购物车
     */
    @RequestMapping(value = "/deleteFromCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> deleteFromCart(@RequestBody String id) {
        ResponseModel<Cart> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(id)) {
            responseModel.setMessage("购物车ID为空");
            return responseModel;
        }

        return jbh_biz_client.deleteFromCart(id);
    }

    /**
     * 获取用户 所有购物车
     */
    @RequestMapping(value = "/getAllFromCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllFromCart(@RequestHeader("userID") String userID) {
        return jbh_biz_client.getAllFromCart(userID);
    }

    /**
     * 获取用户 所有购物车
     */
    @RequestMapping(value = "/getAllCountFromCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getAllCountFromCart(@RequestHeader("userID") String userID) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();

        ResponseArrayModel<JSONObject> allFromCart = jbh_biz_client.getAllFromCart(userID);

        if (allFromCart.isSuccess()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("count", allFromCart.getData().size());
            responseModel.setData(jsonObject);

            responseModel.setSuccess(true);
        }

        return responseModel;
    }
}
