package com.microservices.business.justbehere.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microservices.business.justbehere.utils.Service2JSONObject;
import com.microservices.common.constants.Constants;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
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
@RequestMapping(value = "/service")
public class ServiceController {

    @Autowired
    JBH_Mysql_Client jbh_mysql_client;


    private final Logger logger = LoggerFactory.getLogger(ServiceController.class);


    /************ 服务分类 **************/



    /**
     * 获取服务分类详情
     *
     * @param code 服务分类编号
     * @return  ServiceClassify + Link ServiceClassify
     */
    @RequestMapping(value = "/getServiceClassify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getServiceClassify(@RequestBody String code) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();

        // TODO: 2020/6/19 需要性能优化
        ResponseModel<ServiceClassify> serviceClassifyResponseModel = jbh_mysql_client.selectServiceClassify(code);
        if (serviceClassifyResponseModel.isSuccess()) {
            responseModel.setSuccess(true);

            ServiceClassify serviceClassify = serviceClassifyResponseModel.getData();

            JSONObject serviceClassifyObject = new JSONObject();
            Service2JSONObject.serviceClassify2Json(serviceClassify, serviceClassifyObject);

            // 服务分类下 所有服务
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("classify", serviceClassify.code);
            jsonObject.put("start", 0);
            jsonObject.put("number", 30);
            ResponseArrayModel<Service> serviceResponseArrayModel = jbh_mysql_client.selectServiceList(jsonObject);
            if (serviceResponseArrayModel.isSuccess()) {
                JSONArray jsonArrayServers = new JSONArray();
                for (Service service : serviceResponseArrayModel.getData()) {
                    JSONObject object = new JSONObject();
                    Service2JSONObject.service2Json(service, object);
                    jsonArrayServers.add(object);
                }
                serviceClassifyObject.put("servers", jsonArrayServers);
            }


            //关联 服务分类

            JSONObject linkClassifyObject = new JSONObject();

            ResponseModel<ServiceClassify> linkServiceClassify = jbh_mysql_client.selectServiceClassify(serviceClassify.link);
            if (linkServiceClassify.isSuccess()) {
                Service2JSONObject.serviceClassify2Json(linkServiceClassify.getData(), linkClassifyObject);

                JSONObject object = new JSONObject();
                object.put("classify", linkServiceClassify.getData().code);
                object.put("start", 0);
                object.put("number", 3);
                ResponseArrayModel<Service> linkServiceResponseArrayModel = jbh_mysql_client.selectServiceList(object);
                if (linkServiceResponseArrayModel.isSuccess()) {
                    JSONArray jsonArrayServers = new JSONArray();
                    for (Service service : linkServiceResponseArrayModel.getData()) {
                        JSONObject object1 = new JSONObject();
                        Service2JSONObject.service2Json(service, object1);
                        jsonArrayServers.add(object1);
                    }
                    linkClassifyObject.put("servers", jsonArrayServers);
                }
            }


            JSONObject row = new JSONObject();
            row.put("serviceClassify", serviceClassifyObject);
            row.put("linkServiceClassify", linkClassifyObject);
            responseModel.setData(row);
        }

        return responseModel;
    }

    /**
     * 获取服务分类详情
     *
     * @param code 服务分类编号
     * @return  Service + ServiceDetail
     */
    @RequestMapping(value = "/getService", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> getServiceList(@RequestBody String code) {
        ResponseModel<JSONObject> responseModel = new ResponseModel<>();

        // TODO: 2020/6/19 需要性能优化
        ResponseModel<Service> serviceResponseModel = jbh_mysql_client.selectService(code);
        if (serviceResponseModel.isSuccess()) {
            responseModel.setSuccess(true);

            JSONObject jsonObject = new JSONObject();
            Service2JSONObject.service2Json(serviceResponseModel.getData(), jsonObject);

            JSONObject jsonObject1 = new JSONObject();
            ResponseModel<ServiceDetail> serviceDetailResponseModel = jbh_mysql_client.selectServiceDetail(serviceResponseModel.getData().code);
            if (serviceDetailResponseModel.isSuccess()) {
                Service2JSONObject.serverDetail2Json(serviceDetailResponseModel.getData(), jsonObject1, jbh_mysql_client);
            }


            JSONObject response = new JSONObject();
            response.put("service", jsonObject);
            response.put("serviceDetail", jsonObject1);
            responseModel.setData(response);
        }

        return responseModel;
    }


}
