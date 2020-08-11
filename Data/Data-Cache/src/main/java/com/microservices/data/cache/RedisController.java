package com.microservices.data.cache;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.cache.body.TokenBody;
import com.microservices.common.utils.ValidateUtil;
import com.microservices.common.feignclient.data.cache.body.ExtendBody;
import com.microservices.common.feignclient.data.cache.result.ExtendResult;
import com.microservices.common.feignclient.data.cache.body.SmsCodeBody;
import com.microservices.data.cache.entity.UserRedis;
import com.microservices.data.cache.redis.RedisService;
import com.microservices.common.response.ResponseJsonModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/redis")
public class RedisController {

    @Autowired
    RedisService redisService;

    private static final String sms_code_pre = "sms_code_";
    private static final String token_pre = "token_";
    private static final String user_pre = "user_";


    private final Logger logger = LoggerFactory.getLogger(RedisController.class);


    /************  短信 验证码   ************/

    @RequestMapping(value = "/saveSmsCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> saveSmsCode(@RequestBody SmsCodeBody body) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        if (!ValidateUtil.isCellphone(body.phoneNumber)) {
            responseModel.setMessage("请输入正确手机号");
            return responseModel;
        }

        if (StringUtil.isEmpty(body.smsCode)) {
            responseModel.setMessage("验证码不能为空");
            return responseModel;
        }

        ExtendBody extendBody = new ExtendBody();
        extendBody.key = sms_code_pre + body.phoneNumber;
        extendBody.value = body.smsCode;
        extendBody.seconds = 5 * 60;

        return setExtend(extendBody);
    }

    @RequestMapping(value = "/getSmsCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> getSmsCode(@RequestBody String phoneNumber) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        if (!ValidateUtil.isCellphone(phoneNumber)) {
            responseModel.setMessage("请输入正确手机号");
            return responseModel;
        }

        ResponseModel<ExtendResult> smsCodeReponse = getExtend(sms_code_pre + phoneNumber);
        if (smsCodeReponse.isSuccess()) {
            responseModel.setSuccess(true);
            responseModel.setData(smsCodeReponse.getData().value);
        } else {
            responseModel.setMessage(smsCodeReponse.getErrCode() + " -- " + smsCodeReponse.getMessage());
        }

        return responseModel;
    }


    /************  userID   ************/

    @RequestMapping(value = "/saveToken", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> saveToken(@RequestBody TokenBody body) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        if (StringUtil.isEmpty(body.userID)) {
            responseModel.setMessage("用户ID为空");
            return responseModel;
        }

        if (StringUtil.isEmpty(body.token)) {
            responseModel.setMessage("token为空");
            return responseModel;
        }

        UserRedis userRedis = new UserRedis();

        ResponseModel<String> oldDataResult = get(user_pre + body.userID);
        if (oldDataResult.isSuccess()) {
            userRedis = JSONObject.parseObject(oldDataResult.getData(), UserRedis.class);
        }

        // 更新token值
        userRedis.token = body.token;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", user_pre + body.userID);
        jsonObject.put("value", userRedis.toString());
        ResponseModel<JSONObject> userResult = set(jsonObject);
        if (!userResult.isSuccess()) {
            return userResult;
        }

        ExtendBody tokenExtendBody = new ExtendBody();
        tokenExtendBody.key = token_pre + body.token;
        tokenExtendBody.value = body.userID;
        tokenExtendBody.seconds = 60 * 60;

        return setExtend(tokenExtendBody);
    }

    /**
     * 通过userID 获取 token
     *
     * @param userID
     * @return
     */
    @RequestMapping(value = "/getToken", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> getToken(@RequestBody String userID) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        if (!StringUtil.isEmpty(userID)) {
            responseModel.setMessage("用户ID为空");
            return responseModel;
        }

        ResponseModel<String> userResponse = get(user_pre + userID);
        if (userResponse.isSuccess()) {
            responseModel.setSuccess(true);
            UserRedis userRedis = JSONObject.parseObject(userResponse.getData(), UserRedis.class);
            responseModel.setData(userRedis.token);
        } else {
            responseModel.setMessage(userResponse.getErrCode() + " -- " + userResponse.getMessage());
        }

        return responseModel;
    }

    /**
     * 通过 token 获取用户ID
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/getUserID", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> getUserID(@RequestBody String token) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(token)) {
            responseModel.setMessage("token为空");
            return responseModel;
        }

        ResponseModel<ExtendResult> tokenResponse = getExtend(token_pre + token);
        if (tokenResponse.isSuccess()) {
            responseModel.setSuccess(true);
            responseModel.setData(tokenResponse.getData().value);

            // 更新下过期时间
            {
                ExtendBody tokenExtendBody = new ExtendBody();
                tokenExtendBody.key = token_pre + token;
                tokenExtendBody.value = tokenResponse.getData().value;
                tokenExtendBody.seconds = 60 * 60;

                setExtend(tokenExtendBody);
            }
        } else {
            responseModel.setMessage(tokenResponse.getErrCode() + " -- " + tokenResponse.getMessage());
        }

        return responseModel;
    }


    /************  extend : key-value   ************/

    /**
     * 通用
     *
     * @param body 通过 key-value
     * @return
     */
    @RequestMapping(value = "/set", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> set(@RequestBody JSONObject body) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        if (StringUtil.isEmpty(body.getString("key"))) {
            responseModel.setMessage("缓存数据，key为空");
            return responseModel;
        }

        String result = redisService.set(body.getString("key"), body.getString("value"));
        if (!StringUtil.isEmpty(result) && result.endsWith("OK")) {
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("缓存失败");
        }

        return responseModel;
    }

    /**
     * 获取value值
     *
     * @param key 通过key值
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> get(@RequestBody String key) {
        ResponseModel<String> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(key)) {
            logger.error(key + " - 为空");
            return responseModel;
        }

        String value = redisService.get(key);
        if (value == null) {
            responseModel.setMessage(key + " - 不存在");
            responseModel.setErrCode("-2");
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(value);
        }

        return responseModel;
    }

    /**
     * 设置扩展key-value，添加ttl
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/setExtend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<JSONObject> setExtend(@RequestBody ExtendBody body) {
        ResponseModel<JSONObject> responseModel = new ResponseJsonModel();

        if (StringUtil.isEmpty(body.key)) {
            responseModel.setMessage("缓存数据，key为空");
            return responseModel;
        }

        String result = redisService.setex(body.key, body.value, body.seconds);
        if (result != null) {
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("缓存失败");
        }

        return responseModel;
    }

    /**
     * 通过key值获取扩展内容
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "/getExtend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ExtendResult> getExtend(@RequestBody String key) {
        ResponseModel<ExtendResult> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(key)) {
            logger.error(key + " - 为空");
            return responseModel;
        }

        long ttl = redisService.ttl(key);
        if (ttl == -2) {
            responseModel.setMessage(key + " - 不存在");
            responseModel.setErrCode("-2");
        } else if (ttl == -1) {
            responseModel.setMessage(key + " - 已过期");
            responseModel.setErrCode("-1");
        } else {
            responseModel.setSuccess(true);

            ExtendResult result = new ExtendResult();
            result.key = key;
            result.value = redisService.get(key);
            result.ttl = ttl;

            responseModel.setData(result);
        }

        return responseModel;
    }

}
