package com.microservices.middleplatform.sms;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.microservices.common.feignclient.data.DataCacheClient;
import com.microservices.common.response.ResponseJsonModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.RandomUtil;
import com.microservices.common.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sms")
public class SmsController {

    @Autowired
    DataCacheClient dataCacheClient;


    @Value("${mp.sms.aliyuncs.regionid}")
    private String regionid;

    @Value("${mp.sms.aliyuncs.accesskeyid}")
    private String accesskeyid;

    @Value("${mp.sms.aliyuncs.secret}")
    private String secret;


    private static final String sms_code_pre = "sms_code_";

    private final Logger logger = LoggerFactory.getLogger(SmsController.class);


    /************  手机号 验证码   ************/

    // 发送验证码
    @RequestMapping(value = "/sendVerificationCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> sendVerificationCode(@RequestBody JSONObject params) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        String phoneNumber = params.getString("phoneNumber");
        if (!ValidateUtil.isCellphone(phoneNumber)) {
            responseModel.setMessage("请输入正确手机号");
            return responseModel;
        }

        try {
            String smsCode = RandomUtil.smsCode();
            logger.debug("短信验证码：" + phoneNumber + " -- " + smsCode);

            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            // 创建DefaultAcsClient实例并初始化
            // TODO: 2020/5/25 需要把数据提到配置文件中
            DefaultProfile profile = DefaultProfile.getProfile(regionid, accesskeyid, secret);
            DefaultProfile.addEndpoint("cn-zhangjiakou", "cn-zhangjiakou", "Dysmsapi", "dysmsapi.aliyuncs.com");
            IAcsClient client = new DefaultAcsClient(profile);
            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(phoneNumber);
            request.setSignName("倾心");
            request.setTemplateCode("SMS_159620377");
            request.setTemplateParam("{\"code\":\"" + smsCode + "\"}");

//            SendSmsResponse resp = client.getAcsResponse(request);
//            logger.debug("短信验证信息：" + resp.getCode() + "   " + resp.getMessage() + "   " + resp.getRequestId() + "   " + resp.getBizId());

            // 缓存数据
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", sms_code_pre + phoneNumber);
            jsonObject.put("value", smsCode);
            jsonObject.put("seconds", 5 * 60);
            ResponseModel<JSONObject> redisResponse = dataCacheClient.redisSetExtend(jsonObject);
            if (redisResponse.isSuccess()) {
                responseModel.setSuccess(true);
                responseModel.setMessage(smsCode);
            } else {
                responseModel.setMessage("验证码发送成功，缓存失败");
            }
        } catch (Exception e) {
            logger.error("手机验证码发送异常：", e);
            responseModel.setMessage("验证码发送失败：" + e.toString());
        }

        return responseModel;
    }

}
