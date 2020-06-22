package com.microservices.business.justbehere.goods;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microservices.common.constants.Constants;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.*;
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

import java.util.List;

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
        ResponseArrayModel<GoodsClassify> responseModel = new ResponseArrayModel<>();

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
    public ResponseArrayModel<JSONObject> getGoodsList(@RequestBody JSONObject body) {
        ResponseArrayModel<JSONObject> responseArrayModel = new ResponseArrayModel<>();

        // TODO: 2020/6/19 需要性能优化
        ResponseArrayModel<Goods> goodsResponseArrayModel = jbh_mysql_client.selectGoodsList(body);
        if (goodsResponseArrayModel.isSuccess()) {
            responseArrayModel.setSuccess(true);

            for (int i = 0; i < goodsResponseArrayModel.getData().size(); ++i) {
                JSONObject jsonObject = new JSONObject();
                goodsDetail2Json(goodsResponseArrayModel.getData().get(i), jsonObject);
                goodsPrice2Json(goodsResponseArrayModel.getData().get(i).id, jsonObject);

                responseArrayModel.getData().add(jsonObject);
            }
        }

        return responseArrayModel;
    }

    /**
     * 获取商品详情
     *
     * @param code 商品编号
     * @return
     */
    @RequestMapping(value = "/getGoods", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getGoodsList(@RequestBody String code) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();

        // TODO: 2020/6/19 需要性能优化
        ResponseModel<Goods> goodsResponseModel = jbh_mysql_client.selectGoods(code);
        if (goodsResponseModel.isSuccess()) {
            responseModel.setSuccess(true);

            JSONObject jsonObject = new JSONObject();
            goodsDetail2Json(goodsResponseModel.getData(), jsonObject);
            goodsPrice2Json(goodsResponseModel.getData().id, jsonObject);
            goodsCarousel2Json(goodsResponseModel.getData().code, jsonObject);

            responseModel.setData(jsonObject);
        }

        return responseModel;
    }


    private void goodsDetail2Json(Goods entity, JSONObject object) {
        if (entity != null) {
            object.put("id", entity.id);
            object.put("code", entity.code);
            object.put("title", entity.title);
            object.put("icon", entity.icon);
            object.put("tag", entity.tag);
            object.put("desc", entity.desc);
            object.put("classify", entity.classify);
            object.put("content", entity.content);
            object.put("level", entity.level);
        }
    }

    private void goodsPrice2Json(String code, JSONObject object) {
        if (!StringUtil.isEmpty(code)) {
            ResponseModel<GoodsPrice> goodsPrice = jbh_mysql_client.selectGoodsPrice(code);
            if (goodsPrice.isSuccess()) {
                object.put("price", goodsPrice.getData().price);
                object.put("discountRatio", goodsPrice.getData().discount);
            }
        }
    }

    private void goodsCarousel2Json(String code, JSONObject object) {
        if (!StringUtil.isEmpty(code)) {
            JSONObject carouselJsonObject = new JSONObject();
            carouselJsonObject.put("carouselCode", Constants.carousel_goods);
            carouselJsonObject.put("code", code);

            ResponseArrayModel<Carousel> carouselResponseArrayModel = jbh_mysql_client.selectCarouselList(carouselJsonObject);
            if (carouselResponseArrayModel.isSuccess()) {
                JSONArray jsonArray = new JSONArray();

                for (Carousel carousel : carouselResponseArrayModel.getData()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("path", carousel.path);
                    jsonArray.add(jsonObject);
                }
                object.put("carousels", jsonArray);
            }
        }
    }
}
