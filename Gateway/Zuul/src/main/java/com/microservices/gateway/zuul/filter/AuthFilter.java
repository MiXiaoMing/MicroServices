package com.microservices.gateway.zuul.filter;

import com.microservices.common.feignclient.data.cache.DataCacheClient;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.JwtUtil;
import com.microservices.common.utils.StringUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.tomcat.util.http.MimeHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * token值验证
 */
@Component
public class AuthFilter extends ZuulFilter {

    @Autowired
    DataCacheClient dataCacheClient;


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
        String url = request.getRequestURI();

        if (url.startsWith("/jbh")) {
            if (url.startsWith("/jbh/goods") || url.startsWith("/jbh/service") || url.startsWith("/jbh/carousel")
                    || url.startsWith("/jbh/setting") || url.startsWith("/jbh/login")) {
                return false;
            } else {
                return true;
            }
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
        String token = request.getHeader("token");

        if (StringUtil.isEmpty(token)) {
            //返回错误信息
            rc.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            rc.setResponseBody(HttpStatus.UNAUTHORIZED.getReasonPhrase());
            rc.setSendZuulResponse(false);
            return null;
        }

        // jwt验证token正确性
        boolean verify = JwtUtil.verifyToken(token, "");
        if (!verify) {
            //返回错误信息
            rc.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            rc.setResponseBody(HttpStatus.UNAUTHORIZED.getReasonPhrase());
            rc.setSendZuulResponse(false);
            return null;
        }

        // 添加header：userID
        rc.addZuulRequestHeader("userID", JwtUtil.getUserID(token));


        return null;
    }
}
