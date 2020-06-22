package com.microservices.common.feignclient.business;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.Carousel;
import com.microservices.common.feignclient.data.justbehere.result.Cart;
import com.microservices.common.feignclient.data.justbehere.result.GoodsClassify;
import com.microservices.common.feignclient.data.justbehere.result.ServiceOrder;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface JBH_BIZ_SettingClient {

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
}