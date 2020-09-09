package com.microservices.gateway.zuul.shiro;

import com.alibaba.fastjson.JSON;
import com.microservices.common.feignclient.data.user.DataUserClient;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.apache.shiro.mgt.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.lang.annotation.Documented;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro 配置中心
 */
@Configuration
public class ShiroConfig {

    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    /**
     * ShiroFilterFactoryBean配置
     * 先走 filter ，然后 filter 如果检测到请求头存在 token，则用 token 去 login，走 Realm 去验证
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 添加自己的过滤器并且取名为jwtFilter
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwtFilter", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        // 设置无权限时跳转的 url;
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized/无权限");

        // Shiro路径权限配置
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 排除路径
//        filterChainDefinitionMap.put("jbh/login/**", "anon");
//        filterChainDefinitionMap.put("/uaa/logout", "anon");


        filterChainDefinitionMap.put("/jbh/user/**", "roles[admin]");
//        filterChainDefinitionMap.put("/jbh/user/**", "perms[sys:user:info]");
        // 如果启用shiro，则设置最后一个设置为JWTFilter，否则全部路径放行
        filterChainDefinitionMap.put("/**", "jwtFilter");

        logger.debug("filterChainMap:{}", JSON.toJSONString(filterChainDefinitionMap));
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 安全管理器配置
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager(DataUserClient dataUserClient) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        {
            JwtRealm jwtRealm = new JwtRealm(dataUserClient);
            jwtRealm.setCachingEnabled(false);

            CredentialsMatcher credentialsMatcher = new JwtCredentialsMatcher();
            jwtRealm.setCredentialsMatcher(credentialsMatcher);

            securityManager.setRealm(jwtRealm);
        }

        {
            /*
             * 关闭shiro自带的session，详情见文档
             * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
             */
            DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();

            DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
            sessionStorageEvaluator.setSessionStorageEnabled(false);
            defaultSubjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
            securityManager.setSubjectDAO(defaultSubjectDAO);
        }

        return securityManager;
    }

    /**
     * 添加注解支持
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 开启aop注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
