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
    public ResponseModel<Cart> addToCart(@RequestHeader("token") String token,
                                         @RequestBody CartBody body) {
        ResponseModel<Cart> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(token)) {
            responseModel.setErrCode("401");
            responseModel.setMessage("请登录后重试");
            return responseModel;
        }

        ResponseModel<String> tokenResponse = dataCacheClient.getUserID(token);
        if (!tokenResponse.isSuccess()) {
            responseModel.setMessage(tokenResponse.getMessage());
            return responseModel;
        }

        body.userID = tokenResponse.getData();

        return jbh_biz_client.addToCart(body);
    }

    /**
     * 更新 购物车
     */
    @RequestMapping(value = "/updateToCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> updateToCart(@RequestHeader("token") String token,
                                                                    @RequestBody CartBody body) {
        ResponseModel<Cart> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(token)) {
            responseModel.setErrCode("401");
            responseModel.setMessage("请登录后重试");
            return responseModel;
        }

        ResponseModel<String> tokenResponse = dataCacheClient.getUserID(token);
        if (!tokenResponse.isSuccess()) {
            responseModel.setMessage(tokenResponse.getMessage());
            return responseModel;
        }

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
    public ResponseModel<Cart> deleteFromCart(@RequestHeader("token") String token,
                                                                    @RequestBody String id) {
        ResponseModel<Cart> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(token)) {
            responseModel.setErrCode("401");
            responseModel.setMessage("请登录后重试");
            return responseModel;
        }

        ResponseModel<String> tokenResponse = dataCacheClient.getUserID(token);
        if (!tokenResponse.isSuccess()) {
            responseModel.setMessage(tokenResponse.getMessage());
            return responseModel;
        }

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
    public ResponseArrayModel<Cart> getAllFromCart(@RequestHeader("token") String token) {
        ResponseArrayModel<Cart> responseModel = new ResponseArrayModel<>();

        if (StringUtil.isEmpty(token)) {
            responseModel.setErrCode("401");
            responseModel.setMessage("请登录后重试");
            return responseModel;
        }

        ResponseModel<String> tokenResponse = dataCacheClient.getUserID(token);
        if (!tokenResponse.isSuccess()) {
            responseModel.setErrCode("401");
            responseModel.setMessage(tokenResponse.getMessage());
            return responseModel;
        }

        return jbh_biz_client.getAllFromCart(tokenResponse.getData());
    }

    /**
     * 获取用户 所有购物车
     */
    @RequestMapping(value = "/getAllCountFromCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getAllCountFromCart(@RequestHeader("token") String token) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(token)) {
            responseModel.setErrCode("401");
            responseModel.setMessage("请登录后重试");
            return responseModel;
        }

        ResponseModel<String> tokenResponse = dataCacheClient.getUserID(token);
        if (!tokenResponse.isSuccess()) {
            responseModel.setErrCode("401");
            responseModel.setMessage(tokenResponse.getMessage());
            return responseModel;
        }

        ResponseArrayModel<Cart> allFromCart = jbh_biz_client.getAllFromCart(tokenResponse.getData());

        if (allFromCart.isSuccess()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("count", allFromCart.getData().size());
            responseModel.setData(jsonObject);
        }

        return responseModel;
    }
}
