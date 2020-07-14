package com.microservices.business.justbehere.user;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.constants.Constants;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.cache.body.SmsCodeBody;
import com.microservices.common.feignclient.data.cache.body.TokenBody;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.feignclient.data.user.DataUserClient;
import com.microservices.common.feignclient.middleplatform.NotifyClient;
import com.microservices.common.generator.SnowflakeIdService;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.JwtUtil;
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
    DataUserClient mpUserClient;

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
        ResponseModel<String> smsCodeResponse = dataCacheClient.getSmsCode(body.phoneNumber);
        if (!smsCodeResponse.isSuccess()) {
            logger.error("获取验证码失败：" + smsCodeResponse.getErrCode() + " - " + smsCodeResponse.getMessage());
            responseModel.setMessage(smsCodeResponse.getErrCode() + " - " + smsCodeResponse.getMessage());
            return responseModel;
        }

        if (!body.smsCode.equals(smsCodeResponse.getData())) {
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

        ResponseModel<UserBase> userResultResponseModel = mpUserClient.getUser(jsonObject);
        if (!userResultResponseModel.isSuccess()) {
            UserBase userBase = new UserBase();
            userBase.phoneNumber = body.phoneNumber;

            ResponseModel<UserBase> createUserResponse = mpUserClient.addUser(userBase);
            if (!createUserResponse.isSuccess()) {
                responseModel.setMessage("用户创建失败：" + createUserResponse.getErrCode() + " - " + createUserResponse.getMessage());
                return responseModel;
            } else {
                userID = createUserResponse.getData().id;
            }
        } else {
            userID = userResultResponseModel.getData().id;
        }

        // 返回token
        responseModel.setSuccess(true);

        // 使用jwt创建token
        String token = JwtUtil.generateToken(userID, "", null);
        responseModel.setData(token);

        // todo 这里需要缓存refresh token值
        // 缓存token数据
        {
            TokenBody tokenBody = new TokenBody();
            tokenBody.userID = userID;
            tokenBody.token = token;
            dataCacheClient.saveToken(tokenBody);
        }


        return responseModel;
    }

}
