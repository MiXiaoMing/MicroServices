package com.microservices.business.justbehere.pay;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.constants.ThirdPartyConstants;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.response.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@RestController
@RequestMapping(value = "/pay/balance")
public class BalancePay {

    // TODO: 2020/6/20 三方支付 需要做成 中台服务


    @Autowired
    JBH_Mysql_Client jbh_mysql_client;

    private final Logger logger = LoggerFactory.getLogger(BalancePay.class);

    @RequestMapping(value = "/balancePay", method = RequestMethod.POST)
    public ResponseModel<JSONObject> balancePay(HttpServletRequest request, String orderID, String orderType) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();

//        String userID = sessionUserHolder.getUserId(request);
//        float balance = userService.getUserBalance(userID);
//
//        if (orderType.equals(Constants.order_type_server)) {
//            ServerOrder serverOrder = orderService.getServerOrderDetail(userID, orderID);
//            if (balance >= serverOrder.payPrice) {
//                float curBalance = balance - serverOrder.payPrice;
//                orderService.updateServerOrderStatus("", orderID, "02", "余额支付金额：" + serverOrder.payPrice);
//                userService.updateBalance(userID, curBalance);
//                JSONObject row = new JSONObject();
//                row.put("balance", curBalance);
//                responseModel.setRows(row);
//                responseModel.setIsSuccess("true");
//            } else {
//                responseModel.setMsgCode("余额不足，请充值");
//                JSONObject row = new JSONObject();
//                row.put("balance", balance);
//                responseModel.setRows(row);
//            }
//        } else {
//            GoodsOrder goodsOrder = orderService.getGoodsOrderDetail(userID, orderID);
//            if (balance >= goodsOrder.price) {
//                float curBalance = balance - goodsOrder.price;
//                orderService.updateGoodsOrderStatus("", orderID, "02", "余额支付金额：" + goodsOrder.price);
//                userService.updateBalance(userID, curBalance);
//                JSONObject row = new JSONObject();
//                row.put("balance", curBalance);
//                responseModel.setRows(row);
//                responseModel.setIsSuccess("true");
//            } else {
//                responseModel.setMsgCode("余额不足，请充值");
//                JSONObject row = new JSONObject();
//                row.put("balance", balance);
//                responseModel.setRows(row);
//            }
//        }

        return responseModel;
    }

}
