package com.microservices.business.justbehere.goods;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.feignclient.data.justbehere.result.GoodsClassify;
import com.microservices.common.feignclient.data.justbehere.result.GoodsCollection;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/goods")
public class GoodsController {

    @Autowired
    JBH_Mysql_Client jbh_mysql_client;


    private final Logger logger = LoggerFactory.getLogger(GoodsController.class);


    /************ 商品 **************/

    /**
     * 获取所有商品分类
     *
     * @return
     */
    @RequestMapping(value = "/getAllGoodsClassify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<GoodsClassify> getAllGoodsClassify() {
        JSONObject jsonObject = new JSONObject();

        return jbh_mysql_client.selectGoodsClassifyList(jsonObject);
    }

    /**
     * 获取某类商品
     *
     * @param body classify（商品分类），start（开始位置），number（数量）
     * @return
     */
    @RequestMapping(value = "/getGoodsList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<GoodsCollection> getGoodsList(@RequestBody JSONObject body) {
        return jbh_mysql_client.selectGoodsList(body);
    }

    /**
     * 获取商品详情
     *
     * @param code 商品编号
     * @return
     */
    @RequestMapping(value = "/getGoods", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsCollection> getGoods(@RequestBody String code) {
        return jbh_mysql_client.selectGoods(code);
    }
}
