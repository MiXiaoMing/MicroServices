package com.microservices.common.feignclient.business;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.result.Cart;
import com.microservices.common.feignclient.data.justbehere.result.GoodsClassify;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface JBH_BIZ_GoodsClient {


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


}