package com.study.user.security.shiro.config;

import com.study.user.security.shiro.ShiroSessionManager;
import com.study.user.security.shiro.cache.ShiroRedisCacheManager;
import com.study.user.security.shiro.filter.CusFormAuthenticationFilter;
import com.study.user.security.shiro.filter.CusPermissionsAuthorizationFilter;
import com.study.user.security.shiro.filter.KickoutSessionControlFilter;
import com.study.user.security.shiro.matcher.RetryLimitHashedCredentialsMatcher;
import com.study.user.security.shiro.realm.UserServiceRealm;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.AbstractSessionManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * AtLeastOneSuccessfulStrategy ：如果一个（或更多）Realm 验证成功，则整体的尝试被认
 * 为是成功的。如果没有一个验证成功，则整体尝试失败。
 * FirstSuccessfulStrategy 只有第一个成功地验证的Realm 返回的信息将被使用。所有进一步
 * 的Realm 将被忽略。如果没有一个验证成功，则整体尝试失败
 * AllSucessfulStrategy 为了整体的尝试成功，所有配置的Realm 必须验证成功。如果没有一
 * 个验证成功，则整体尝试失败。
 * ModularRealmAuthenticator 默认的是AtLeastOneSuccessfulStrategy
 */
@Configuration
public class ShiroConfig {


    private static final String cookName = "DCS_JSESSIONID";
    private static final String cacheName = "shiro_SessionCache";


    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);


    protected String setCookName() {
        return cookName;
    }

    protected String setCacheName() {
        return cacheName;
    }


    /**
     * Shiro的Web过滤器Factory 命名:shiroFilter<br />
     *
     * @param securityManager
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        /*定义shiro过滤器,例如实现自定义的FormAuthenticationFilter，需要继承FormAuthenticationFilter
         */
        /*定义shiro过滤链  Map结构
         * Map中key(xml中是指value值)的第一个'/'代表的路径是相对于HttpServletRequest.getContextPath()的值来的
         * anon：它对应的过滤器里面是空的,什么都没做,这里.do和.jsp后面的*表示参数,比方说login.jsp?main这种
         * authc：该过滤器下的页面必须验证后才能访问,它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // <!-- 过滤链定义，从上向下顺序执行
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
       /* filterChainDefinitionMap.put("/api/sysUser/auth/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/index/**", "anon");
        filterChainDefinitionMap.put("/error/**", "anon");
        filterChainDefinitionMap.put("/api/v1/**", "authc,kickout");*/
        filterChainDefinitionMap.put("/**", "anon");
        return createShiroFilterFactoryBean(securityManager, filterChainDefinitionMap);
    }

    protected ShiroFilterFactoryBean createShiroFilterFactoryBean(SecurityManager securityManager, Map<String, String> filterChainDefinitionMap) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.getFilters().put("authc", new CusFormAuthenticationFilter());
        shiroFilterFactoryBean.getFilters().put("roles", new CusFormAuthenticationFilter());
        shiroFilterFactoryBean.getFilters().put("perms", new CusPermissionsAuthorizationFilter());
        shiroFilterFactoryBean.getFilters().put("kickout", createKickedOutSessionControlFilter());
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    @Bean
    public FilterRegistrationBean myFilterRegistration() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST, new DispatcherType[]{DispatcherType.ASYNC});
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns(new String[]{"/*"});
        return filterRegistration;
    }

    @Bean
    public KickoutSessionControlFilter createKickedOutSessionControlFilter() {
        return new KickoutSessionControlFilter();
    }


    @Bean
    protected SecurityManager securityManager(SessionManager sessionManager, @Qualifier("shiroCacheManager") CacheManager cacheManager, ShiroProperties shiroProperties) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(getModularRealmAuthenticator());
        securityManager.setAuthorizer(authorizer());
        List<Realm> reams = new ArrayList<>();
        reams.add(authorizingRealms(shiroProperties));
        securityManager.setRealms(reams);
        if (cacheManager != null) {
            securityManager.setCacheManager(cacheManager);
        }
        if (sessionManager != null) {
            securityManager.setSessionManager(sessionManager);
        }
        return securityManager;
    }

    @Bean
    public DefaultSessionManager sessionManager(SessionDAO sessionDAO) {
        ShiroSessionManager manager = new ShiroSessionManager();
        manager.setSessionDAO(sessionDAO);
        long timeout = getGlobalSessionOut();
        if (timeout > 0) {
            manager.setGlobalSessionTimeout(timeout);
            manager.setSessionValidationInterval(timeout);
        }
        manager.setSessionIdCookieEnabled(true);
        SimpleCookie cookie = new SimpleCookie(setCookName());
        manager.setSessionIdCookie(cookie);
        return manager;
    }

    @Bean("shiroCacheManager")
    public CacheManager cacheManager() {
        return new ShiroRedisCacheManager();
    }


    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName(setCacheName());
        return sessionDAO;
    }

    /**
     * 多realm验证,一个通过即可
     */
    @Bean("authenticator")
    protected Authenticator getModularRealmAuthenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }

    protected Long getGlobalSessionOut() {
        return AbstractSessionManager.DEFAULT_GLOBAL_SESSION_TIMEOUT * 4;
    }

    /**
     * shiro生命周期
     */
    @Bean("lifecycleBeanPostProcessor")
    protected LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean("advisorAutoProxyCreator")
    protected DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
     */
    @Bean("authorizationAttributeSourceAdvisor")
    @DependsOn({"securityManager"})
    protected AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 凭证匹配器
     * 密码的匹配是CredentitalsMatcher#doCredentialsMatch方法中进行的
     *
     * @return
     */
    @Bean("hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher(ShiroProperties shiroProperties) {
        RetryLimitHashedCredentialsMatcher hashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(cacheManager());
        hashedCredentialsMatcher.setHashAlgorithmName(shiroProperties.getHashAlgorithm());
        hashedCredentialsMatcher.setHashIterations(shiroProperties.getHashTimes());
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    @Bean
    protected Authorizer authorizer() {
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        return authorizer;
    }

    /**
     * Shiro Realm
     */
    @Bean
    public Realm authorizingRealms(ShiroProperties shiroProperties) {
        UserServiceRealm userRealm = new UserServiceRealm();
        //告诉realm,使用credentialsMatcher加密算法类来验证密文
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher(shiroProperties));
        userRealm.setCachingEnabled(true);
        return userRealm;
    }


}
