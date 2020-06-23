package com.microservices.interfaces.justbehere.setting;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.constants.Constants;
import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.data.justbehere.result.Carousel;
import com.microservices.common.feignclient.data.justbehere.result.Setting_MainPage;
import com.microservices.common.response.ResponseArrayModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/setting")
public class Setting_MainPageController {

    @Autowired
    JBH_BIZ_Client jbh_biz_client;


    private final Logger logger = LoggerFactory.getLogger(Setting_MainPageController.class);


    /************ 配置项 **************/

    /**
     * 配置项 首页配置项 五大项
     *
     * @return
     */
    @RequestMapping(value = "/getMainPageFive")
    public ResponseArrayModel<JSONObject> getFive() {
        return jbh_biz_client.getFive();
    }


    /**
     * 配置项 首页配置项 十小项
     *
     * @return
     */
    @RequestMapping(value = "/getMainPageTen", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getTen() {
        return jbh_biz_client.getTen();
    }

    /**
     * 配置项 首页配置项 推荐
     *
     * @return
     */
    @RequestMapping(value = "/getMainPageRecommend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getRecommend() {
        return jbh_biz_client.getRecommend();
    }

    /**
     * 配置项 首页配置项 推荐服务分类
     *
     * @return
     */
    @RequestMapping(value = "/getMainPageRecommendServiceClassify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getRecommendServiceClassify() {
        return jbh_biz_client.getRecommendServiceClassify();
    }

}
