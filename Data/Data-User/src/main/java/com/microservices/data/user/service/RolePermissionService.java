package com.microservices.data.user.service;

import com.microservices.common.feignclient.data.user.result.RolePermission;
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
public class RolePermissionService {

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(RolePermissionService.class);


    /**
     * 角色+权限  新增
     *
     * @param body
     * @return
     */
    public ResponseModel<RolePermission> insert(RolePermission body) {
        ResponseModel<RolePermission> responseModel = new ResponseModel<>();

        // todo 根据 父节点ID + 兄弟节点数量 + 1，确定ID


//        int next = 0;
//        ResponseArrayModel<RolePermission> hasRolePermissions = selectAllList();
//        if (hasRolePermissions.isSuccess()) {
//            next = hasRolePermissions.getData().size() + 1;
//        } else {
//            next = 1;
//        }
//
//        body.id = String.valueOf(next);
//        body.createTime = new Date();
//
//        int count = sqlSessionTemplate.insert("com.microservices.data.user.RolePermissionMapper.insert", body);
//        if (count > 0) {
//            responseModel.setData(body);
//            responseModel.setSuccess(true);
//        } else {
//            responseModel.setMessage("表数据插入失败：" + body);
//        }

        return responseModel;
    }

    /**
     * 角色+权限 通过角色ID
     *
     * @param role_id 角色ID
     * @return
     */
    public ResponseArrayModel<RolePermission> selectList(String role_id) {
        ResponseArrayModel<RolePermission> responseModel = new ResponseArrayModel<>();

        Map<String, Object> map = new HashMap<>();
        map.put("role_id", role_id);

        List<RolePermission> entities = sqlSessionTemplate.selectList("com.microservices.data.user.RolePermissionMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("该角色+权限不存在：" + role_id);
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
    public ResponseModel<RolePermission> update(RolePermission body) {
        ResponseModel<RolePermission> responseModel = new ResponseModel<>();

        if (StringUtil.isEmpty(body.id)) {
            responseModel.setMessage("更新ID不能为空");
            return responseModel;
        }

        body.updateTime = new Date();

        int count = sqlSessionTemplate.update("com.microservices.data.user.RolePermissionMapper.update", body);
        if (count == 0) {
            responseModel.setMessage("更新角色+权限失败");
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(body);
        }

        return responseModel;
    }
}
