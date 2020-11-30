package com.microservices.gateway.zuul.filter;

import com.microservices.common.constants.RedisConstants;
import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.feignclient.data.user.DataUserClient;
import com.microservices.common.feignclient.data.user.result.UserBase;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import com.microservices.gateway.zuul.exception.AuthenticationException;
import com.microservices.gateway.zuul.exception.AuthorizationException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * token值验证
 */
@Component
@ConfigurationProperties(prefix = "permission")
public class AuthFilter extends ZuulFilter {

    @Autowired
    DataCacheClient dataCacheClient;

    @Autowired
    DataUserClient dataUserClient;


    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    /**
     * 过滤器的类型。可选值有：
     * pre - 前置过滤
     * route - 路由后过滤
     * error - 异常过滤
     * post - 远程服务调用后过滤
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 同种类的过滤器的执行顺序。
     * 按照返回值的自然升序执行。
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 返回boolean类型。代表当前filter是否生效。
     * 默认值为false。
     * 返回true代表开启filter。
     */
    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        // 这里通过token值判断是否逻辑处理
        String token = request.getHeader("token");
        if (token == null) {
            return false;
        }
        return true;
    }

    /**
     * run方法就是过滤器的具体逻辑。
     * return 可以返回任意的对象，当前实现忽略。（spring-cloud-zuul官方解释）
     * 直接返回null即可。
     */
    @Override
    public Object run() throws ZuulException {
        // 通过zuul，获取请求上下文
        RequestContext rc = RequestContext.getCurrentContext();
        HttpServletRequest request = rc.getRequest();

        logger.info("请求地址：method={},url={}", request.getMethod(), request.getRequestURL().toString());

        try {
            // 第一步：判断token值有效性
            String token = request.getHeader("token");
            ResponseModel<String> tokenResponse = dataCacheClient.get(RedisConstants.token_pre + token);
            if (tokenResponse.isSuccess()) {
                logger.debug("添加header->userID：" + tokenResponse.getData());
                rc.addZuulRequestHeader("userID", tokenResponse.getData());
            } else {
                throw new AuthenticationException("无效token");
            }

            if (!enable) {
                return null;
            }

            // 第二步：判断权限
            String uri = request.getRequestURI();
            String permission = getPermission(uri);
            if (!StringUtil.isEmpty(permission)) {
                ResponseModel<UserBase> userResponse = dataUserClient.getUser(tokenResponse.getData());
                if (userResponse.isSuccess()) {
                    String roleID = userResponse.getData().role_id;
                    ResponseModel<String> permissionResponse = dataUserClient.getListByRole(roleID);
                    if (permissionResponse.isSuccess()) {
                        if (!permissionResponse.getData().contains(permission)) {
                            throw new AuthorizationException("无相应权限");
                        }
                    } else {
                        throw new AuthorizationException("无相应权限");
                    }
                } else {
                    throw new AuthorizationException("无相应权限");
                }
            }
        } catch (AuthenticationException ex) {
            rc.setSendZuulResponse(false);
            rc.setResponseStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
            rc.setResponseBody("token不存在");
            rc.getResponse().setContentType("text/html;charset=UTF-8");
        } catch (AuthorizationException ex) {
            rc.setSendZuulResponse(false);
            rc.setResponseStatusCode(HttpServletResponse.SC_FORBIDDEN);
            rc.getResponse().setContentType("text/html;charset=UTF-8");
            rc.setResponseBody("无相应权限");
        } catch (Exception ex) {
            rc.setSendZuulResponse(false);
            rc.setResponseStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            rc.setResponseBody("未知错误：" + ex.toString());
            rc.getResponse().setContentType("text/html;charset=UTF-8");
        }

        return null;
    }


    /********  配置数据  ***********/

    // 是否启用
    private boolean enable;

    private List<Permission> list;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<Permission> getList() {
        return list;
    }

    public void setList(List<Permission> list) {
        this.list = list;
    }

    public static class Permission {
        private String perm;
        private String urls;

        public String getPerm() {
            return perm;
        }

        public void setPerm(String perm) {
            this.perm = perm;
        }

        public String getUrls() {
            return urls;
        }

        public void setUrls(String urls) {
            this.urls = urls;
        }
    }

    private String getPermission(String uri) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).urls.contains(uri)) {
                return list.get(i).perm;
            }
        }
        return "";
    }
}
