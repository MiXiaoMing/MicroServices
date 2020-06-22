package com.microservices.business.justbehere.setting;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/setting/mainPage")
public class Setting_MainPageController {

    @Autowired
    JBH_Mysql_Client jbh_mysql_client;


    private final Logger logger = LoggerFactory.getLogger(Setting_MainPageController.class);


    /************ 主页配置项 **************/

    /**
     * 配置项 列表获取  五大项
     *
     * @return
     */
    @RequestMapping(value = "/getFive", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getFive() {
        ResponseArrayModel<JSONObject> responseArrayModel = new ResponseArrayModel<>();

        getData(Constants.setting_main_page_five, responseArrayModel);
        return responseArrayModel;
    }

    /**
     * 配置项 列表获取  十小项
     *
     * @return
     */
    @RequestMapping(value = "/getTen", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getTen() {
        ResponseArrayModel<JSONObject> responseArrayModel = new ResponseArrayModel<>();

        getData(Constants.setting_main_page_ten, responseArrayModel);
        return responseArrayModel;
    }

    /**
     * 配置项 列表获取  推荐
     *
     * @return
     */
    @RequestMapping(value = "/getRecommend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getRecommend() {
        ResponseArrayModel<JSONObject> responseArrayModel = new ResponseArrayModel<>();

        getData(Constants.setting_main_page_recommend, responseArrayModel);
        return responseArrayModel;
    }

    /**
     * 配置项 列表获取  推荐服务分类
     *
     * @return
     */
    @RequestMapping(value = "/getRecommendServiceClassify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getRecommendServiceClassify() {
        ResponseArrayModel<JSONObject> responseArrayModel = new ResponseArrayModel<>();

        ResponseArrayModel<Setting_MainPage> settingResponse = jbh_mysql_client.selectMainPageSettingList(Constants.setting_main_page_recommend_service_classify);
        if (settingResponse.isSuccess()) {
            responseArrayModel.setSuccess(true);

            for (int i = 0; i < settingResponse.getData().size(); ++i) {
                Setting_MainPage setting = settingResponse.getData().get(i);

                String classify = setting.classify;

                JSONObject object = new JSONObject();
                object.put("classify", classify);

                JSONObject serviceClassifyObject = new JSONObject();
                if (classify.equals(Constants.data_service_classify)) {
                    ResponseModel<ServiceClassify> serviceClassifyResponse = jbh_mysql_client.selectServiceClassify(setting.code);
                    if (serviceClassifyResponse.isSuccess()) {
                        Service2JSONObject.serviceClassify2Json(serviceClassifyResponse.getData(), serviceClassifyObject);
                        object.put("serviceClassify", serviceClassifyObject);

                        JSONObject serviceInput = new JSONObject();
                        serviceInput.put("classify", serviceClassifyResponse.getData().code);
                        serviceInput.put("start", 0);
                        serviceInput.put("number", 3);
                        ResponseArrayModel<Service> serviceResponseArrayModel = jbh_mysql_client.selectServiceList(serviceInput);
                        if (serviceResponseArrayModel.isSuccess()) {
                            JSONArray jsonArrayServers = new JSONArray();
                            for (Service service : serviceResponseArrayModel.getData()) {
                                JSONObject serviceObject = new JSONObject();
                                Service2JSONObject.service2Json(service, serviceObject);
                                jsonArrayServers.add(serviceObject);
                            }
                            serviceClassifyObject.put("servers", jsonArrayServers);
                        }
                    }
                } else {
                    logger.error("错误数据：需要查找原因:" + setting.code);
                }

                responseArrayModel.getData().add(object);
            }
        }

        return responseArrayModel;
    }





    private void getData(String type, ResponseArrayModel<JSONObject> responseArrayModel) {
        ResponseArrayModel<Setting_MainPage> settingResponse = jbh_mysql_client.selectMainPageSettingList(type);
        if (settingResponse.isSuccess()) {
            responseArrayModel.setSuccess(true);

            for (int i = 0; i < settingResponse.getData().size(); ++i) {
                Setting_MainPage setting = settingResponse.getData().get(i);

                String classify = setting.classify;

                JSONObject object = new JSONObject();
                object.put("classify", classify);

                JSONObject objectObj = new JSONObject();
                if (classify.equals(Constants.data_service_classify)) {
                    ResponseModel<ServiceClassify> serviceClassifyResponse = jbh_mysql_client.selectServiceClassify(setting.code);
                    if (serviceClassifyResponse.isSuccess()) {
                        Service2JSONObject.serviceClassify2Json(serviceClassifyResponse.getData(), objectObj);
                        object.put("serviceClassify", objectObj);
                    }
                } else if (classify.equals(Constants.data_service)) {
                    ResponseModel<Service> service = jbh_mysql_client.selectService(setting.code);
                    if (service.isSuccess()) {
                        Service2JSONObject.service2Json(service.getData(), objectObj);
                        object.put("service", objectObj);
                    }
                } else if (classify.equals(Constants.data_goods_classify)) {
                    ResponseModel<GoodsClassify> goodsClassify = jbh_mysql_client.selectGoodsClassify(setting.code);
                    if (goodsClassify.isSuccess()) {
                        goodsClassify2Json(goodsClassify.getData(), objectObj);
                        object.put("goodsClassify", objectObj);
                    }
                } else if (classify.equals(Constants.data_goods)) {
                    ResponseModel<Goods> goodsDetail = jbh_mysql_client.selectGoods(setting.code);
                    if (goodsDetail.isSuccess()) {
                        goodsDetail2Json(goodsDetail.getData(), objectObj);
                        object.put("goods", objectObj);
                    }
                } else {
                    logger.error("错误数据：需要查找原因:" + setting.code);
                }

                responseArrayModel.getData().add(object);
            }
        }
    }






    private void goodsClassify2Json(GoodsClassify classify, JSONObject object) {
        if (classify != null) {
            object.put("code", classify.code);
            object.put("title", classify.title);
            object.put("name", classify.name);
            object.put("desc", classify.desc);
            object.put("icon", classify.icon);
        }
    }

    private void goodsDetail2Json(Goods goodsDetail, JSONObject object) {
        if (goodsDetail != null) {
            object.put("code", goodsDetail.code);
            object.put("title", goodsDetail.title);
            object.put("icon", goodsDetail.icon);
        }
    }
}
