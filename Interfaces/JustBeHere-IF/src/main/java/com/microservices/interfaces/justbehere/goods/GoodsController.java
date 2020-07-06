package com.microservices.interfaces.justbehere.goods;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.Goods;
import com.microservices.common.feignclient.data.justbehere.result.GoodsClassify;
import com.microservices.common.feignclient.data.justbehere.result.ServiceOrder;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseJsonModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/goods")
public class GoodsController {

    @Autowired
    JBH_BIZ_Client jbh_biz_client;



    /*************  商品 分类 ************/

    /**
     * 获取 所有商品分类
     */
    @RequestMapping(value = "/getAllGoodsClassify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getAllGoodsClassify() {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        ResponseArrayModel<GoodsClassify> goodsClassifyResponse = jbh_biz_client.getAllGoodsClassify();
        if (goodsClassifyResponse.isSuccess()) {
            for (int i = 0; i < goodsClassifyResponse.getData().size(); ++i) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", goodsClassifyResponse.getData().get(i).code);
                jsonObject.put("title", goodsClassifyResponse.getData().get(i).title);
                jsonObject.put("name", goodsClassifyResponse.getData().get(i).name);
                jsonObject.put("desc", goodsClassifyResponse.getData().get(i).desc);

                responseModel.getData().add(jsonObject);
            }

            responseModel.setSuccess(true);
        }

        return responseModel;
    }

    /**
     * 获取某一商品分类中商品，
     *
     * @param body  classify（商品分类），page（页数，从0开始），number（每页数量）
     * @return
     */
    @RequestMapping(value = "/getGoodsList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getGoodsList(@RequestBody JSONObject body) {

        int page = body.getInteger("page");
        if (page <= 0) {
            page = 0;
        }

        int number = body.getInteger("number");
        if (number <= 0) {
            number = 10;
        }

        body.put("start", page * number);

        return jbh_biz_client.getGoodsList(body);
    }

    /**
     * 获取某一商品详情
     *
     * @param code  商品编号
     * @return
     */
    @RequestMapping(value = "/getGoods", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getGoodsList(@RequestBody String code) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        if (StringUtil.isEmpty(code)) {
            responseModel.setMessage("商品编号为空");
            return responseModel;
        }

        return jbh_biz_client.getGoods(code);
    }
}
