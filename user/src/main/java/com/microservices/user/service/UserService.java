package com.microservices.user.service;

import com.microservices.entities.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    //获取用户数据
    public User getUserInfo(String id) {
        if (StringUtils.isNotBlank(id)) {
            User user = new User();
            user.id = "11234";
            user.loginID = "1234";
            user.telephone = "1234";
            user.createTime = new Date();
            user.updateTime = new Date();
            return user;
        }
        return null;
    }
}
