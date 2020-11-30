package com.microservices.interfaces.justbehere.others;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.business.JBH_BIZ_Client;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.justbehere.body.GoodsOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.Feedback;
import com.microservices.common.feignclient.data.justbehere.result.GoodsClassify;
import com.microservices.common.feignclient.data.justbehere.result.GoodsOrder;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseJsonModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/others")
public class FeedbackController {

    @Autowired
    JBH_BIZ_Client jbh_biz_client;

    @Autowired
    DataCacheClient dataCacheClient;


    /*************  用户反馈 ************/

    /**
     * 添加 新用户反馈
     */
    @RequestMapping(value = "/addFeedback", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> addGoodsOrder(@RequestHeader("userID") String userID, @RequestBody String content) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        Feedback body = new Feedback();
        body.userID = userID;
        body.content = content;

        return jbh_biz_client.addFeedback(body);
    }

    /**
     * 获取用户反馈列表
     *
     * @param body page（页数，从0开始），number（每页数量）
     * @return
     */
    @RequestMapping(value = "/getGoodsList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Feedback> getGoodsList(@RequestBody JSONObject body) {

        int page = body.getInteger("page");
        if (page <= 0) {
            page = 0;
        }

        int number = body.getInteger("number");
        if (number <= 0) {
            number = 10;
        }

        body.put("start", page * number);

        return jbh_biz_client.getAllFeedback(body);
    }
}
