package com.microservices.business.justbehere.user;

import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.cache.body.SmsCodeBody;
import com.microservices.common.feignclient.data.cache.body.TokenBody;
import com.microservices.common.feignclient.data.user.body.CreateUserBody;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.feignclient.middleplatform.UserClient;
import com.microservices.common.generator.SnowflakeIdService;
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
    UserClient mpUserClient;

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

        ResponseModel<UserBase> userResultResponseModel = mpUserClient.getUserByPhoneNumber(body.phoneNumber);
        if (!userResultResponseModel.isSuccess()) {
            CreateUserBody createUserBody = new CreateUserBody();
            createUserBody.phoneNumber = body.phoneNumber;

            ResponseModel<String> createUserResponse = mpUserClient.createUser(createUserBody);
            if (!createUserResponse.isSuccess()) {
                responseModel.setMessage("用户创建失败：" + createUserResponse.getErrCode() + " - " + createUserResponse.getMessage());
                return responseModel;
            } else {
                userID = createUserResponse.getData();
            }
        } else {
            userID = userResultResponseModel.getData().id;
        }

        // 返回token
        responseModel.setSuccess(true);
        // TODO: 2020/5/28 token生成，需要切换为 正式的
        String token = snowflakeIdService.getId();
        responseModel.setData(token);

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
