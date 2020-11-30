package com.microservices.middleplatform.notify;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.profile.DefaultProfile;
import com.microservices.common.constants.RedisConstants;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.cache.body.ExtendBody;
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


    private final Logger logger = LoggerFactory.getLogger(SmsController.class);


    /************  手机号 验证码   ************/

    // 发送验证码
    @RequestMapping(value = "/sendVerificationCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> sendVerificationCode(@RequestBody String phoneNumber) {
        ResponseModel<String> responseModel = new ResponseModel<>();

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
            DefaultProfile.addEndpoint("", "", "", "");
            IAcsClient client = new DefaultAcsClient(profile);
            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(phoneNumber);
            request.setSignName("倾心");
            request.setTemplateCode("");
            request.setTemplateParam("{\"code\":\"" + smsCode + "\"}");

            // TODO: 2020/5/25 不能删除
//            SendSmsResponse resp = client.getAcsResponse(request);
//            logger.debug("短信验证信息：" + resp.getCode() + "   " + resp.getMessage() + "   " + resp.getRequestId() + "   " + resp.getBizId());

            // 缓存数据
            ExtendBody extendBody = new ExtendBody();
            extendBody.key = RedisConstants.sms_code_pre_login + phoneNumber;
            extendBody.value = smsCode;
            extendBody.seconds = RedisConstants.sms_code_login_expired;

            ResponseModel<JSONObject> redisResponse = dataCacheClient.setExtend(extendBody);
            if (redisResponse.isSuccess()) {
                responseModel.setSuccess(true);
                responseModel.setData(smsCode);
            } else {
                responseModel.setMessage("验证码发送成功，缓存失败");
            }
        } catch (Exception e) {
            responseModel.setMessage("验证码发送失败：" + e.toString());
        }

        return responseModel;
    }

}
