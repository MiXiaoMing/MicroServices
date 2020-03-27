package com.microservices.testdata.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.support.ResponseArrayModel;
import com.microservices.testdata.entity.*;
import com.microservices.testdata.service.*;
import com.microservices.utils.DateUtil;
import com.microservices.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/td/web")
public class WebController {

    @Autowired
    private CodeService codeService;
    @Autowired
    private AppCodeService appCodeService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private PageService pageService;
    @Autowired
    private InterfaceService interfaceService;
    @Autowired
    private PerformanceService performanceService;


    /************  统计数据   ************/

    // 记录列表
    @RequestMapping(value = "/monitor/recordList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> recordList(@RequestBody JSONObject params) {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        String appName = params.getString("appName");
        String appVersion = params.getString("appVersion");
        String platform = params.getString("platForm");

        String appID = null;

        if (!TextUtils.isEmpty(platform)) {
            Code code = codeService.selectPlatformCode(platform , "");
            if (code == null) {
                responseModel.setMessage("暂不支持该类型平台：" + platform);
                return responseModel;
            }

            if (!TextUtils.isEmpty(appName)) {
                AppCode appCode = appCodeService.selectAppCode("","", appName, code.code);
                if (appCode == null) {
                    responseModel.setMessage(platform + " 平台下未找到应用：" + appName);
                    return responseModel;
                }

                appID = appCode.appID;
            }
        }


        List<Scheme> schemes = schemeService.selectSchemes(appID, appVersion);
        if (schemes != null) {
            ArrayList<JSONObject> jsonObjects = new ArrayList<>();

            for (int i = 0; i < schemes.size(); ++i) {
                Scheme scheme = schemes.get(i);
                JSONObject object = new JSONObject();
                if (!TextUtils.isEmpty(scheme.appCode)) {
                    AppCode appCode = appCodeService.selectAppCode(scheme.appCode, "", "", "");
                    Code code = codeService.selectPlatformCode(null, appCode.platformCode);
                    object.put("appName", appCode.appName);
                    object.put("platForm", code.nick);
                }else {
                    object.put("appName", appName);
                    object.put("platForm", platform);
                }

                object.put("appVersion", scheme.appVersion);
                object.put("recordId", scheme.id);
                object.put("phoneNum", scheme.deviceModel);
                object.put("phoneSystem", scheme.systemVersion);
                object.put("recordStartTime", DateUtil.dealDateFormat(scheme.startTime));
                object.put("recordEndTime", DateUtil.dealDateFormat(scheme.endTime));
                if (scheme.flag.equalsIgnoreCase("A")) {
                    object.put("recordStatus", "测试中");
                } else if (scheme.flag.equalsIgnoreCase("U") && scheme.endTime != null) {
                    object.put("recordStatus", "已完成");
                }

                object.put("classConsumeTime", pageService.averageTime(scheme.id));
                object.put("urlConsumeTime", interfaceService.averageTime(scheme.id));
                object.put("cpuUse", performanceService.averageCpu(scheme.id));
                object.put("memoryUse", performanceService.averageMemory(scheme.id));

                jsonObjects.add(object);
            }

            responseModel.setData(jsonObjects);
            responseModel.setSuccess(true);
        }

        return responseModel;
    }


    /************  具体的监控指标   ************/

    // 页面监控 查询
    @RequestMapping(value = "/monitor/recordPageDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> pages(@RequestBody JSONObject params) {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        String schemeID = params.getString("recordID");

        if (TextUtils.isEmpty(schemeID)) {
            responseModel.setMessage("id 为空");
            return responseModel;
        }

        List<Page> pages = pageService.selectPages(schemeID);
        if (pages != null) {
            ArrayList<JSONObject> jsonObjects = new ArrayList<>();

            for (int i = 0; i < pages.size(); ++i) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", pages.get(i).pageName);
                jsonObject.put("duration", pages.get(i).responseTime);
                jsonObject.put("recordTime", pages.get(i).operateTime);

                jsonObjects.add(jsonObject);
            }

            responseModel.setData(jsonObjects);
            responseModel.setSuccess(true);
        }

        return responseModel;
    }

    /************  接口   ************/

    @RequestMapping(value = "/monitor/recordUrlDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> interfaces(@RequestBody JSONObject params) {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        String schemeID = params.getString("recordID");

        if (TextUtils.isEmpty(schemeID)) {
            responseModel.setMessage("id 为空");
            return responseModel;
        }

        List<Interface> interfaces = interfaceService.selectInterfaces(schemeID);
        if (interfaces != null) {
            ArrayList<JSONObject> jsonObjects = new ArrayList<>();
            for (int i = 0; i < interfaces.size(); ++i) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", interfaces.get(i).url);
                jsonObject.put("duration", interfaces.get(i).responseTime);
                jsonObject.put("recordTime", interfaces.get(i).operateTime);

                jsonObjects.add(jsonObject);
            }

            responseModel.setData(jsonObjects);
            responseModel.setSuccess(true);
        }

        return responseModel;
    }

    /************  性能数据   ************/

    @RequestMapping(value = "/monitor/recordCpuMemoryDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<JSONObject> performances(@RequestBody JSONObject params) {
        ResponseArrayModel<JSONObject> responseModel = new ResponseArrayModel<>();

        String schemeID = params.getString("recordID");
        String type = params.getString("type");

        if (TextUtils.isEmpty(schemeID)) {
            responseModel.setMessage("id 为空");
            return responseModel;
        }

        List<Performance> performances = performanceService.selectPerformances(schemeID);
        if (performances != null) {
            ArrayList<JSONObject> jsonObjects = new ArrayList<>();

            for (int i = 0; i < performances.size(); ++i) {
                JSONObject jsonObject = new JSONObject();
                if (type.equalsIgnoreCase("0")) {
                    jsonObject.put("content", performances.get(i).cpuRatio);
                } else if (type.equalsIgnoreCase("1")) {
                    jsonObject.put("content", performances.get(i).memorySize);
                }
                jsonObject.put("recordTime", performances.get(i).operateTime);

                jsonObjects.add(jsonObject);
            }

            responseModel.setData(jsonObjects);
            responseModel.setSuccess(true);
        }

        return responseModel;
    }
}
