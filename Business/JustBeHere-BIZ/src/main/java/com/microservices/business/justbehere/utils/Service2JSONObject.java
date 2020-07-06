package com.microservices.business.justbehere.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.feignclient.data.justbehere.result.Service;
import com.microservices.common.feignclient.data.justbehere.result.ServiceClassify;
import com.microservices.common.feignclient.data.justbehere.result.ServiceDetail;
import com.microservices.common.feignclient.data.justbehere.result.ServicePrice;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class Service2JSONObject {

    @Autowired
    JBH_Mysql_Client jbh_mysql_client;

    /**
     * 服务分类 转 JSON
     * @param serviceClassify
     * @param object
     */
    public static void serviceClassify2Json(ServiceClassify serviceClassify, JSONObject object) {
        if (serviceClassify != null) {
            object.put("code", serviceClassify.code);
            object.put("name", serviceClassify.name);
            object.put("desc", serviceClassify.desc);
            object.put("icon", serviceClassify.icon);
            object.put("image", serviceClassify.cover);
        }
    }


    /**
     *  服务 转 JSON
     *
     * @param service
     * @param object
     */
    public static void service2Json(Service service, JSONObject object) {
        if (service != null) {
            object.put("code", service.code);
            object.put("name", service.name);
            object.put("shortdesc", service.summary);
            object.put("desc", service.desc);
            object.put("icon", service.icon);
            object.put("bigicon", service.bigIcon);
            object.put("cover", service.cover);
            object.put("classify", service.classify);
            object.put("level", service.level);
        }
    }

    /**
     * 服务详情 转 JSON
     *
     * @param serverDetail
     * @param object
     */
    public static void serverDetail2Json(ServiceDetail serverDetail, JSONObject object, JBH_Mysql_Client client) {
        if (serverDetail != null) {
            object.put("note", serverDetail.note);
            object.put("effect", serverDetail.effect);
            object.put("introduce", serverDetail.introduce);
            object.put("refertime", serverDetail.refertime);
            object.put("scope", serverDetail.scope);
            object.put("tools", serverDetail.tools);
            object.put("assurance", serverDetail.assurance);
            object.put("flow", serverDetail.flow);

            // TODO: 2020/6/20 这种关联性 需要分离出来
            if (!StringUtil.isEmpty(serverDetail.others)) {
                List<String> others = Arrays.asList(serverDetail.others.split(","));
                JSONArray otherArray = new JSONArray();
                for (String code : others) {
                    ResponseModel<Service> service = client.selectService(code);
                    if (service.isSuccess()) {
                        JSONObject otherObject = new JSONObject();
                        otherObject.put("code", code);
                        otherObject.put("name", service.getData().name);
                        otherObject.put("desc", service.getData().desc);
                        otherObject.put("cover", service.getData().cover);
                        otherArray.add(otherObject);
                    }
                }
                object.put("others", otherArray);
            }
            ResponseArrayModel<ServicePrice> servicePrice = client.selectServicePriceList(serverDetail.code);
            if (servicePrice.isSuccess()) {
                JSONArray priceArray = new JSONArray();

                for (int i = 0; i < servicePrice.getData().size(); ++i) {
                    JSONObject priceObject = new JSONObject();
                    priceObject.put("id", servicePrice.getData().get(i).id);
                    priceObject.put("name", servicePrice.getData().get(i).name);
                    priceObject.put("price", servicePrice.getData().get(i).price);
                    priceObject.put("discount", servicePrice.getData().get(i).discount);
                    priceObject.put("unit", servicePrice.getData().get(i).unit);
                    priceObject.put("minimum", servicePrice.getData().get(i).minimum);

                    priceArray.add(priceObject);
                }

                object.put("prices", priceArray);
            }
        }
    }


}
