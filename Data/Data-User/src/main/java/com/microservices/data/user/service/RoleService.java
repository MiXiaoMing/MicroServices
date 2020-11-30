package com.microservices.data.user.service;

import com.microservices.common.feignclient.data.user.result.Role;
import com.microservices.common.generator.SnowflakeIdService;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(RoleService.class);


    /**
     * 角色  新增
     *
     * @param body
     * @return
     */
    public ResponseModel<Role> insert(Role body) {
        ResponseModel<Role> responseModel = new ResponseModel<>();

        int next = 0;
        ResponseArrayModel<Role> hasRoles = selectAllList();
        if (hasRoles.isSuccess()) {
            next = hasRoles.getData().size() + 1;
        } else {
            next = 1;
        }

        body.id = String.valueOf(next);
        body.createTime = new Date();

        int count = sqlSessionTemplate.insert("com.microservices.data.user.RoleMapper.insert", body);
        if (count > 0) {
            responseModel.setData(body);
            responseModel.setSuccess(true);
        } else {
            responseModel.setMessage("表数据插入失败：" + body);
        }

        return responseModel;
    }

    /**
     * 角色 通过表ID
     *
     * @param id 角色表ID
     * @return
     */
    public ResponseModel<Role> select(String id) {
        ResponseModel<Role> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        Role user = sqlSessionTemplate.selectOne("com.microservices.data.user.RoleMapper.select", map);
        if (user == null) {
            responseModel.setMessage("该角色不存在：" + id);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(user);
        }

        return responseModel;
    }

    /**
     * 角色 获取所有数据
     *
     * @param
     * @return
     */
    public ResponseArrayModel<Role> selectAllList() {
        ResponseArrayModel<Role> responseModel = new ResponseArrayModel<>();

        List<Role> entities = sqlSessionTemplate.selectList("com.microservices.data.user.RoleMapper.selectAllList");
        if (entities == null) {
            responseModel.setMessage("无角色数据存在");
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }


    /**
     * 个人信息 更新
     *
     * @param body
     * @return
     */
    public ResponseModel<Role> update(Role body) {
        ResponseModel<Role> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(body.id)) {
            responseModel.setMessage("更新ID不能为空");
            return responseModel;
        }

        body.updateTime = new Date();

        int count = sqlSessionTemplate.update("com.microservices.data.user.RoleMapper.update", body);
        if (count == 0) {
            responseModel.setMessage("更新角色失败");
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(body);
        }

        return responseModel;
    }
}
