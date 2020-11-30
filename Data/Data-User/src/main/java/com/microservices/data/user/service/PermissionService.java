package com.microservices.data.user.service;

import com.microservices.common.feignclient.data.user.result.Permission;
import com.microservices.common.generator.SnowflakeIdService;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermissionService {

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(PermissionService.class);


    /**
     * 权限  新增
     *
     * @param body
     * @return
     */
    public ResponseModel<Permission> insert(Permission body) {
        ResponseModel<Permission> responseModel = new ResponseModel<>();

        // todo 根据 父节点ID + 兄弟节点数量 + 1，确定ID


//        int next = 0;
//        ResponseArrayModel<Permission> hasPermissions = selectAllList();
//        if (hasPermissions.isSuccess()) {
//            next = hasPermissions.getData().size() + 1;
//        } else {
//            next = 1;
//        }
//
//        body.id = String.valueOf(next);
//        body.createTime = new Date();
//
//        int count = sqlSessionTemplate.insert("com.microservices.data.user.PermissionMapper.insert", body);
//        if (count > 0) {
//            responseModel.setData(body);
//            responseModel.setSuccess(true);
//        } else {
//            responseModel.setMessage("表数据插入失败：" + body);
//        }

        return responseModel;
    }

    /**
     * 权限 通过表ID
     *
     * @param id 权限表ID
     * @return
     */
    public ResponseModel<Permission> select(String id) {
        ResponseModel<Permission> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        Permission user = sqlSessionTemplate.selectOne("com.microservices.data.user.PermissionMapper.select", map);
        if (user == null) {
            responseModel.setMessage("该权限不存在：" + id);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(user);
        }

        return responseModel;
    }

    /**
     * 权限 获取指定数据数组
     *
     * @param
     * @return
     */
    public ResponseArrayModel<Permission> selectList(List<String> ids) {
        ResponseArrayModel<Permission> responseModel = new ResponseArrayModel<>();

        List<Permission> entities = sqlSessionTemplate.selectList("com.microservices.data.user.PermissionMapper.selectList", ids);
        if (entities == null) {
            responseModel.setMessage("无权限数据存在");
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }

    /**
     * 权限 获取所有数据
     *
     * @param
     * @return
     */
    public ResponseArrayModel<Permission> selectAllList() {
        ResponseArrayModel<Permission> responseModel = new ResponseArrayModel<>();

        List<Permission> entities = sqlSessionTemplate.selectList("com.microservices.data.user.PermissionMapper.selectAllList");
        if (entities == null) {
            responseModel.setMessage("无权限数据存在");
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
    public ResponseModel<Permission> update(Permission body) {
        ResponseModel<Permission> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(body.id)) {
            responseModel.setMessage("更新ID不能为空");
            return responseModel;
        }

        body.updateTime = new Date();

        int count = sqlSessionTemplate.update("com.microservices.data.user.PermissionMapper.update", body);
        if (count == 0) {
            responseModel.setMessage("更新权限失败");
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(body);
        }

        return responseModel;
    }
}
