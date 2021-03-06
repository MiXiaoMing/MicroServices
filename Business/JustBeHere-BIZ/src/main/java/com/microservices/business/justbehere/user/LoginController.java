package com.microservices.business.justbehere.user;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.constants.Constants;
import com.microservices.common.constants.RedisConstants;
import com.microservices.common.constants.entity.UserRedis;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.cache.body.ExtendBody;
import com.microservices.common.feignclient.data.cache.body.SmsCodeBody;
import com.microservices.common.feignclient.data.cache.result.ExtendResult;
import com.microservices.common.feignclient.data.user.DataUserClient;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.generator.SnowflakeIdService;
import com.microservices.common.generator.UUIDUtil;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.microservices.common.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    DataUserClient dataUserClient;

    @Autowired
    DataCacheClient dataCacheClient;

    @Autowired
    private SnowflakeIdService snowflakeIdService;


    private final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @RequestMapping(value = "/phoneNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> phoneNumber(@RequestBody SmsCodeBody body) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        if (!ValidateUtil.isCellphone(body.phoneNumber)) {
            responseModel.setMessage("请输入正确手机号");
            return responseModel;
        }

        if (StringUtil.isEmpty(body.smsCode)) {
            responseModel.setMessage("验证码不能为空");
            return responseModel;
        }

        // 判断验证码
        ResponseModel<ExtendResult> smsCodeResponse = dataCacheClient.getExtend(RedisConstants.sms_code_pre_login + body.phoneNumber);
        if (!smsCodeResponse.isSuccess()) {
            logger.error("获取验证码失败：" + smsCodeResponse.getErrCode() + " - " + smsCodeResponse.getMessage());
            responseModel.setMessage(smsCodeResponse.getErrCode() + " - " + smsCodeResponse.getMessage());
            return responseModel;
        }

        if (!body.smsCode.equals(smsCodeResponse.getData().value)) {
            logger.error("验证码错误：" + body.smsCode + " - " + smsCodeResponse.getData());
            responseModel.setMessage("验证码错误，请重新输入");
            return responseModel;
        }

        // 判断用户数据
        String userID = "";

        // TODO: 2020/6/23 这里需要区分 用户类型
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phoneNumber", body.phoneNumber);
        jsonObject.put("type", Constants.user_type_customer);

        ResponseModel<UserBase> userResultResponseModel = dataUserClient.getUser(jsonObject);
        if (!userResultResponseModel.isSuccess()) {
            UserBase userBase = new UserBase();
            userBase.phoneNumber = body.phoneNumber;

            ResponseModel<UserBase> createUserResponse = dataUserClient.addUser(userBase);
            if (!createUserResponse.isSuccess()) {
                responseModel.setMessage("用户创建失败：" + createUserResponse.getErrCode() + " - " + createUserResponse.getMessage());
                return responseModel;
            } else {
                userID = createUserResponse.getData().id;
            }
        } else {
            userID = userResultResponseModel.getData().id;
        }

        // 创建token
        String token = UUIDUtil.getUuid();

        // 缓存token数据
        UserRedis userRedis = new UserRedis();

        ResponseModel<String> oldDataResult = dataCacheClient.get(RedisConstants.user_pre + userID);
        if (oldDataResult.isSuccess()) {
            userRedis = JSONObject.parseObject(oldDataResult.getData(), UserRedis.class);
        }

        // 更新token值
        userRedis.token = token;

        JSONObject keyValue = new JSONObject();
        keyValue.put("key", RedisConstants.user_pre + userID);
        keyValue.put("value", userRedis.toString());
        ResponseModel<JSONObject> userResult = dataCacheClient.set(keyValue);
        if (userResult.isSuccess()) {
            // 更新token
            ExtendBody tokenExtendBody = new ExtendBody();
            tokenExtendBody.key = RedisConstants.token_pre + token;
            tokenExtendBody.value = userID;
            tokenExtendBody.seconds = RedisConstants.token_expired;

            dataCacheClient.setExtend(tokenExtendBody);

            responseModel.setData(token);
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("缓存token数据失败");
        }

        return responseModel;
    }

}
