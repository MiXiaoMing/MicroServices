package com.microservices.gateway.zuul.shiro;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.user.DataUserClient;
import com.microservices.common.feignclient.data.user.result.Permission;
import com.microservices.common.feignclient.data.user.result.Role;
import com.microservices.common.feignclient.data.user.result.RolePermission;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Shiro 授权认证
 */
public class JwtRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(JwtRealm.class);

    private DataUserClient dataUserClient;

    public JwtRealm(DataUserClient dataUserClient) {
        this.dataUserClient = dataUserClient;
    }

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权认证,设置角色/权限信息
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.debug("权限认证");
        String token = (String) principalCollection.getPrimaryPrincipal();
        String userID = JwtUtil.getUserID(token);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        ResponseModel<UserBase> responseModel = dataUserClient.getUser(userID);
        if (responseModel.isSuccess()) {
            String role_id = responseModel.getData().role_id;
            {
                // 设置角色
                ResponseModel<Role> roleResponse = dataUserClient.getRole(role_id);
                if (roleResponse.isSuccess()) {
                    Set<String> roleSet = new HashSet<>();
                    roleSet.add(roleResponse.getData().code);
                    info.setRoles(roleSet);
                }
            }

            // 获取角色所对应的权限内容
            ResponseArrayModel<RolePermission> rolePermissionResponse = dataUserClient.getRolePermission(role_id);
            if (rolePermissionResponse.isSuccess()) {
                List<String> permissionIDs = new ArrayList<>();
                for (int i = 0; i< rolePermissionResponse.getData().size(); ++i) {
                    RolePermission rolePermission = rolePermissionResponse.getData().get(i);
                    permissionIDs.add(rolePermission.permission_id);
                }
                if (permissionIDs.size() > 0) {
                    // 获取权限内容
                    ResponseArrayModel<Permission> permissionResponse = dataUserClient.getPermissionList(permissionIDs);
                    Set<String> permissionSet = new HashSet<>();
                    for (int i = 0; i< permissionResponse.getData().size(); ++i) {
                        Permission permission = permissionResponse.getData().get(i);
                        permissionSet.add(permission.code);
                    }

                    info.setStringPermissions(permissionSet);
                }
            }
        }

        logger.error("设置用户角色+权限：" + JSONObject.toJSONString(info));
        return info;
    }

    /**
     * 登录认证
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.debug("身份认证方法");
        // 校验token
        String token = (String) authenticationToken.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUserID(token);
        if (username == null || !JwtUtil.verifyToken(token, "")) {
            throw new AuthenticationException("token认证失败！");
        }

        // TODO: 2020/7/23 feign 始终为null，后续查明原因
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("number", username);
//        ResponseModel<UserCollection> responseModel = dataUserClient.info(jsonObject);
//        if (!responseModel.success) {
//            throw new AuthenticationException("该用户不存在！");
//        }

        return new SimpleAuthenticationInfo(token, token, "JwtRealm");
    }
}
