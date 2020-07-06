package com.microservices.interfaces.justbehere.setting;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.constants.Constants;
import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.feignclient.data.justbehere.result.Carousel;
import com.microservices.common.response.ResponseArrayModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/carousel")
public class CarouselController {

    @Autowired
    JBH_BIZ_Client jbh_biz_client;


    private final Logger logger = LoggerFactory.getLogger(CarouselController.class);


    /************ 轮播图 **************/

    /**
     * 轮播图 首页轮播图
     *
     * @return
     */
    @RequestMapping(value = "/getMainPageAllCarousel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Carousel> getMainPageAllCarousel() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("carouselCode", Constants.carousel_main_page);
        return jbh_biz_client.getAllCarousel(jsonObject);
    }

    /**
     * 轮播图 商品详情 轮播图
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/getGoodsCarousel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Carousel> getGoodsCarousel(@RequestBody String code) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("carouselCode", Constants.carousel_goods);
        jsonObject.put("code", code);
        return jbh_biz_client.getAllCarousel(jsonObject);
    }

}
