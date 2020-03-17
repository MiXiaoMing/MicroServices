package com.microservices.testdata.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.support.ResponseJsonModel;
import com.microservices.support.ResponseModel;
import com.microservices.testdata.service.SchemeService;
import com.microservices.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/td")
public class TestDataController {

    @Autowired
    private SchemeService schemeService;

    /************  流程控制   ************/

    // 开始记录
    @RequestMapping(value = "/monitor/beginRecord", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> startRecord(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String deviceModel = params.getString("phoneNum");
        String systemVersion = params.getString("phoneSystem");
        String appVersion = params.getString("version");
        String appID = params.getString("appBundleId");

        String recordID = schemeService.insertScheme(appID, appVersion, deviceModel, systemVersion);
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

        String recordID = params.getString("id");

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
}
