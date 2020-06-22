package com.microservices.business.justbehere.pay;

import com.microservices.common.constants.Constants;
import com.microservices.common.constants.ThirdPartyConstants;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping(value = "/pay/thirdParty")
public class ThirdPartyPay {

    // TODO: 2020/6/20 三方支付 需要做成 中台服务


    @Autowired
    JBH_Mysql_Client jbh_mysql_client;

    private final Logger logger = LoggerFactory.getLogger(ThirdPartyPay.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/wxpay/wxPayNotify/server")
    @ResponseBody
    public void wxPayNotifyServer(HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.debug("商品 - 微信成功支付信息=============start");

        // 读取参数
        InputStream inputStream = request.getInputStream();
        StringBuffer sb = new StringBuffer();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        // 解析xml成map
        Map<String, String> m = new HashMap<String, String>();
        m = WxPayXMLUtil.doXMLParse(sb.toString());
        // 过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);

            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        // 账号信息
        // logger.error("@@@@@@@@@@@@@@微信回调读取配置@@@@@@@@@@@@@" + ConfigUtil.description());
        String key = ThirdPartyConstants.wx_api_key; // key
        logger.error("@@@@@@@@@@@@@@微信回调读取配置api_key@@@@@@@@@@@@@【" + key + "】");
        String resXml = "";
        // 判断签名是否正确
        if (PayCommonUtil.isTenpaySign("UTF-8", packageParams, key)) {
            logger.debug("微信支付成功回调");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // String resXml = "";
            if ("SUCCESS".equals(packageParams.get("result_code"))) {
                // 这里是支付成功
                String orderNo = (String) packageParams.get("out_trade_no");
                String total_fee = (String) packageParams.get("total_fee");
                // 3.商户自行增加处理流程, 例如：更新订单状态-支付成功 、 等操作
                // 获取订单业务编号

                logger.error("微信订单号【" + orderNo + "】付款成功");
                logger.error("微信订单号付款金额【" + total_fee + "】分");

                // TODO: 2020/6/20 这里不应该注释掉
//                jbh_mysql_client.updateServiceOrder("WX", orderNo, "02", "微信支付金额：" + total_fee);

                // 这里 根据实际业务场景 做相应的操作
                // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            } else {
                logger.error("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
            // ------------------------------
            // 处理业务完毕
            // ------------------------------
            // BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            // out.write(resXml.getBytes());
            // out.flush();
            // out.close();
        } else {
            logger.error("通知签名验证失败");

            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[签名验证失败]]></return_msg>" + "</xml> ";

        }
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/wxpay/wxPayNotify/goods")
    @ResponseBody
    public void wxPayNotifyGoods(HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.debug("商品 - 微信成功支付信息=============start");

        // 读取参数
        InputStream inputStream = request.getInputStream();
        StringBuffer sb = new StringBuffer();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        // 解析xml成map
        Map<String, String> m = new HashMap<String, String>();
        m = WxPayXMLUtil.doXMLParse(sb.toString());
        // 过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);

            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        // 账号信息
        String key = ThirdPartyConstants.wx_api_key; // key
        logger.error("@@@@@@@@@@@@@@微信回调读取配置api_key@@@@@@@@@@@@@【" + key + "】");
        String resXml = "";
        // 判断签名是否正确
        if (PayCommonUtil.isTenpaySign("UTF-8", packageParams, key)) {
            logger.debug("微信支付成功回调");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // String resXml = "";
            if ("SUCCESS".equals(packageParams.get("result_code"))) {
                // 这里是支付成功
                String orderNo = (String) packageParams.get("out_trade_no");
                String total_fee = (String) packageParams.get("total_fee");
                // 3.商户自行增加处理流程, 例如：更新订单状态-支付成功 、 等操作
                // 获取订单业务编号

                logger.error("微信订单号【" + orderNo + "】付款成功");
                logger.error("微信订单号付款金额【" + total_fee + "】分");

                // TODO: 2020/6/20 这里不应该 注释掉
//                jbh_mysql_client.updateGoodsOrderStatus("WX", orderNo, "02", "微信支付金额：" + (Float.valueOf(total_fee) / 100F));

                // 这里 根据实际业务场景 做相应的操作
                // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            } else {
                logger.error("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            logger.error("通知签名验证失败");

            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[签名验证失败]]></return_msg>" + "</xml> ";

        }
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

}
