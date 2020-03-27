package com.microservices.testdata.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microservices.support.ResponseJsonModel;
import com.microservices.support.ResponseModel;
import com.microservices.testdata.entity.*;
import com.microservices.testdata.service.*;
import com.microservices.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/td/client")
public class AppClientController {

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



    /************  流程控制   ************/

    // 开始记录
    @RequestMapping(value = "/monitor/beginRecord", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> startRecord(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String deviceModel = params.getString("phoneNum");
        String systemVersion = params.getString("phoneSystem");
        String appVersion = params.getString("version");
        String appID = params.getString("appBundleId");
        String platform = params.getString("platform");

        if (TextUtils.isEmpty(platform)) {
            responseModel.setMessage("平台属性为空");
            return responseModel;
        }

        Code code = codeService.selectPlatformCode(platform , "");
        if (code == null) {
            responseModel.setMessage("暂不支持该平台类型：" + platform);
            return responseModel;
        }

        AppCode appCode = appCodeService.selectAppCode("", appID, "", code.code);
        if (appCode == null) {
            responseModel.setMessage(platform + " 该平台下未找到对应应用：" + appID);
            return responseModel;
        }

        String recordID = schemeService.insertScheme(appCode.id, appVersion, deviceModel, systemVersion);
        if (!TextUtils.isEmpty(recordID)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("recordId", recordID);

            responseModel.setData(jsonObject);
            responseModel.setSuccess(true);
        }

        return responseModel;
    }

    // 结束记录
    @RequestMapping(value = "/monitor/endRecord", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> endRecord(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String recordID = params.getString("recordId");

        if (TextUtils.isEmpty(recordID)) {
            responseModel.setMessage("记录ID不能为空");
        } else {
            int count = schemeService.updateScheme(recordID);
            if (count <= 0) {
                responseModel.setMessage("数据更新失败");
            } else {
                responseModel.setSuccess(true);
            }
        }

        return responseModel;
    }

    /************  具体的监控指标   ************/

    /************  页面   ************/
    // 页面监控
    @RequestMapping(value = "/monitor/pageResponse", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> pageRecord(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String schemeID = params.getString("recordId");
        JSONArray contextArray = params.getJSONArray("context");

        if (TextUtils.isEmpty(schemeID)) {
            responseModel.setMessage("id 为空");
            return responseModel;
        }

        if (contextArray == null || contextArray.size() <= 0) {
            responseModel.setMessage("context 为空");
            return responseModel;
        }

        int count = pageService.insertPageBatch(schemeID, contextArray);
        if (count >= 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("count", count);

            responseModel.setData(jsonObject);
            responseModel.setSuccess(true);
        }

        return responseModel;
    }

    // 页面监控 查询
    @RequestMapping(value = "/monitor/pages", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> pages(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String schemeID = params.getString("recordId");

        if (TextUtils.isEmpty(schemeID)) {
            responseModel.setMessage("id 为空");
            return responseModel;
        }

        List<Page> pages = pageService.selectPages(schemeID);
        if (pages.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pages", pages);

            responseModel.setData(jsonObject);
            responseModel.setSuccess(true);
        }

        return responseModel;
    }

    /************  接口   ************/

    @RequestMapping(value = "/monitor/urlResponse", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> interfaceRecord(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String schemeID = params.getString("recordId");
        JSONArray contextArray = params.getJSONArray("context");

        if (TextUtils.isEmpty(schemeID)) {
            responseModel.setMessage("id 为空");
            return responseModel;
        }

        if (contextArray == null || contextArray.size() <= 0) {
            responseModel.setMessage("context 为空");
            return responseModel;
        }

        int count = interfaceService.insertInterfaceBatch(schemeID, contextArray);
        if (count >= 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("count", count);

            responseModel.setData(jsonObject);
            responseModel.setSuccess(true);
        }

        return responseModel;
    }

    @RequestMapping(value = "/monitor/interfaces", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> interfaces(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String schemeID = params.getString("recordId");

        if (TextUtils.isEmpty(schemeID)) {
            responseModel.setMessage("id 为空");
            return responseModel;
        }

        List<Interface> interfaces = interfaceService.selectInterfaces(schemeID);
        if (interfaces.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("interfaces", interfaces);

            responseModel.setData(jsonObject);
            responseModel.setSuccess(true);
        }

        return responseModel;
    }

    /************  性能数据   ************/

    @RequestMapping(value = "/monitor/cpuAndMemory", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> performanceRecord(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String schemeID = params.getString("recordId");
        JSONArray contextArray = params.getJSONArray("context");

        if (TextUtils.isEmpty(schemeID)) {
            responseModel.setMessage("id 为空");
            return responseModel;
        }

        if (contextArray == null || contextArray.size() <= 0) {
            responseModel.setMessage("context 为空");
            return responseModel;
        }

        int count = performanceService.insertPerformanceBatch(schemeID, contextArray);
        if (count >= 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("count", count);

            responseModel.setData(jsonObject);
            responseModel.setSuccess(true);
        }

        return responseModel;
    }

    @RequestMapping(value = "/monitor/performances", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> performances(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String schemeID = params.getString("recordId");

        if (TextUtils.isEmpty(schemeID)) {
            responseModel.setMessage("id 为空");
            return responseModel;
        }

        List<Performance> performances = performanceService.selectPerformances(schemeID);
        if (performances.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("performances", performances);

            responseModel.setData(jsonObject);
            responseModel.setSuccess(true);
        }

        return responseModel;
    }
}
