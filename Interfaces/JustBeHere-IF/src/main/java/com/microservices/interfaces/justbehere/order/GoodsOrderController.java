package com.microservices.interfaces.justbehere.order;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.justbehere.body.GoodsOrderBody;
import com.microservices.common.feignclient.data.justbehere.body.GoodsOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.GoodsOrder;
import com.microservices.common.feignclient.data.justbehere.result.GoodsOrder;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/order")
public class GoodsOrderController {

    @Autowired
    JBH_BIZ_Client jbh_biz_client;

    @Autowired
    DataCacheClient dataCacheClient;


    /*************  商品 订单  ************/

    /**
     * 添加 新商品订单
     */
    @RequestMapping(value = "/addGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsOrder> addGoodsOrder(@RequestHeader("userID") String userID, @RequestBody GoodsOrderBody body) {

        body.userID = userID;

        return jbh_biz_client.addGoodsOrder(body);
    }

    /**
     * 更新 商品订单
     */
    @RequestMapping(value = "/updateGoodsOrderStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsOrder> updateGoodsOrderStatus(@RequestBody GoodsOrderBody body) {
        ResponseModel<GoodsOrder> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(body.id)) {
            responseModel.setMessage("订单ID为空");
            return responseModel;
        }

        if (StringUtil.isEmpty(body.status)) {
            responseModel.setMessage("订单状态为空");
            return responseModel;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", body.id);
        jsonObject.put("status", body.status);

        return jbh_biz_client.updateGoodsOrder(jsonObject);
    }

    /**
     * 获取 商品订单详情
     *
     * @param id  商品订单ID
     * @return
     */
    @RequestMapping(value = "/getGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getGoodsOrder(@RequestBody String id) {
        return jbh_biz_client.getGoodsOrder(id);
    }

    /**
     * 获取用户 所有商品订单
     */
    @RequestMapping(value = "/getAllGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllGoodsOrder(@RequestHeader("userID") String userID) {

        return jbh_biz_client.getAllGoodsOrder(userID);
    }

    /**
     * 获取用户 所有未完成商品订单
     */
    @RequestMapping(value = "/getAllUndoneGoodsOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllUndoenGoodsOrder(@RequestHeader("userID") String userID) {

        return jbh_biz_client.getAllUnDoneGoodsOrder(userID);
    }
}
