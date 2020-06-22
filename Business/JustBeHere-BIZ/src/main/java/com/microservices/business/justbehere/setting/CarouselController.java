package com.microservices.business.justbehere.setting;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.result.Carousel;
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
@RequestMapping(value = "/carousel")
public class CarouselController {

    @Autowired
    JBH_Mysql_Client jbh_mysql_client;


    private final Logger logger = LoggerFactory.getLogger(CarouselController.class);


    /************ 轮播图 **************/

    /**
     * 轮播图 列表获取
     *
     * @param body 通过carouselCode（轮播图位置编码）, code（对应编码）
     * @return
     */
    @RequestMapping(value = "/getAllCarousel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Carousel> getAllFromCart(@RequestBody JSONObject body) {
        return jbh_mysql_client.selectCarouselList(body);
    }

}
