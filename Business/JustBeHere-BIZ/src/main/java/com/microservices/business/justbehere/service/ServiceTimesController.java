package com.microservices.business.justbehere.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.JBH_Mysql_Client;
import com.microservices.common.feignclient.data.justbehere.result.ServiceOrder;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.DateUtil;
import io.micrometer.core.instrument.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

@RestController
@RequestMapping(value = "/service")
public class ServiceTimesController {

    @Autowired
    JBH_Mysql_Client jbh_mysql_client;


    private final Logger logger = LoggerFactory.getLogger(ServiceTimesController.class);


    /************ 服务 时间  **************/


    public static final ArrayList<KeyValue> times = new ArrayList() {{
        add(new KeyValue("8:00", 8));
        add(new KeyValue("9:00", 9));
        add(new KeyValue("10:00", 10));
        add(new KeyValue("11:00", 11));
        add(new KeyValue("12:00", 12));
        add(new KeyValue("13:00", 13));
        add(new KeyValue("14:00", 14));
        add(new KeyValue("15:00", 15));
        add(new KeyValue("16:00", 16));
        add(new KeyValue("17:00", 17));
        add(new KeyValue("18:00", 18));
    }};

    public static class KeyValue {
        public String key;
        public int value;

        public KeyValue(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }


    /**
     * 获取服务时间
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getTimeList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> getTimeList() {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 1; i < 7; ++i) {
            JSONObject object = new JSONObject();
            Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * i);
            long zeroTime = date.getTime() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
            object.put("dayDesc", dateFormat.format(date));
            if (i == 1) {
                object.put("dayWeek", "明天");
            } else {
                object.put("dayWeek", DateUtil.getWeekOfDate(date));
            }

            object.put("dayTime", zeroTime);
            responseModel.getData().add(object);

            JSONArray timeJsonArray = new JSONArray();
            for (int j = 0; j < times.size(); ++j) {
                JSONObject timeObject = new JSONObject();
                timeObject.put("timeDesc", times.get(j).key);
                timeObject.put("hourTime", times.get(j).value * 60 * 60 * 1000);

                long time = zeroTime + times.get(j).value * 60 * 60 * 1000;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("serviceTime", new Timestamp(time));
                ResponseArrayModel<ServiceOrder> serviceOrderResponseArrayModel = jbh_mysql_client.selectServiceOrderListByTime(jsonObject);
                if (serviceOrderResponseArrayModel.isSuccess()) {
                    int count = serviceOrderResponseArrayModel.getData().size();

                    timeObject.put("count", count);
                    if (count > 1) {
                        timeObject.put("isTimeActive", false);
                    } else {
                        timeObject.put("isTimeActive", true);
                    }
                } else {
                    timeObject.put("isTimeActive", true);
                }

                timeJsonArray.add(timeObject);
            }
            object.put("hourTimes", timeJsonArray);
        }

        responseModel.setSuccess(true);
        return responseModel;
    }



}
