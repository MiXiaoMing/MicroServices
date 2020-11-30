package com.microservices.business.justbehere.others;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.result.Cart;
import com.microservices.common.feignclient.data.justbehere.result.Feedback;
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
@RequestMapping(value = "/others")
public class FeedbackController {

    @Autowired
    JBH_Mysql_Client serviceClient;


    private final Logger logger = LoggerFactory.getLogger(FeedbackController.class);


    /************ 用户反馈 **************/

    /**
     * 添加 新用户反馈
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/addFeedback", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> addFeedback(@RequestBody Feedback body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        ResponseModel<String> addResponse = serviceClient.insertFeedback(body);
        if (!addResponse.isSuccess()) {
            responseModel.setMessage(addResponse.getMessage());
            return responseModel;
        }

        return addResponse;
    }

    /**
     * 获取所有用户反馈数据
     *
     * @param body 通过 start（开始），number（个数）
     * @return
     */
    @RequestMapping(value = "/getAllFeedback", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Feedback> getAllFeedback(@RequestBody JSONObject body) {
        return serviceClient.selectFeedbackList(body);
    }

}
