package com.microservices.business.justbehere.cart;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.result.Cart;
import com.microservices.common.feignclient.data.justbehere.result.Cart;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    JBH_Mysql_Client serviceClient;


    private final Logger logger = LoggerFactory.getLogger(CartController.class);


    /************ 购物车 **************/

    /**
     * 添加 新购物车
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/addToCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> addToCart(@RequestBody CartBody body) {
        ResponseModel<Cart> responseModel = new ResponseModel<>();

        ResponseModel<String> addResponse = serviceClient.insertCart(body);
        if (!addResponse.isSuccess()) {
            responseModel.setMessage(addResponse.getMessage());
            return responseModel;
        }

        return serviceClient.selectCart(addResponse.getData());
    }

    /**
     * 更新 购物车 状态
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/updateToCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> updateToCart(@RequestBody CartBody body) {
        return serviceClient.updateCart(body);
    }

    /**
     * 删除 从购物车
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteFromCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> deleteFromCart(@RequestBody  String id) {
        return serviceClient.deleteFromCart(id);
    }

    /**
     * 获取所有购物车数据
     *
     * @param userID
     * @return
     */
    @RequestMapping(value = "/getAllFromCart", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Cart> getAllFromCart(@RequestBody String userID) {
        ResponseArrayModel<Cart> responseModel = new ResponseArrayModel<>();

        if (StringUtil.isEmpty(userID)) {
            responseModel.setMessage("用户ID用空");
            return responseModel;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", userID);

        return serviceClient.selectCartList(jsonObject);
    }

}
