package com.study.oauth2.server.config.oauth2;

import com.study.oauth2.server.service.security.MyUserAuthenticationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;


/**
 * 一、授权认证服务的作用--
 * 1. 获取第三方应用发送的授权码（code）以及第三方应用标识
 * 2. 根据授权码及标识进行校验
 * 3. 校验通过，发送令牌（Access Token）
 * <p>
 * 简单用上一节的例子，第一步就是点击微信第三方登陆的url，请求参数带有client_id（第三方用户的id（可理解为账号））client_secret（第三方应用和授权服务器之间的安全凭证(可理解为密码）。除此还会带有redirect_uri中的回调链接，微信服务会生成相关用户凭证，并在其回调链接上附带code
 * 第二步中，授权服务器（微信），首先会校验第三方服务器（比如简书平台）的真实可靠信接着会根据授权码（code）进行校验客户是否已认证
 * 第三步，通过第二步授权码code认证通过后，生成token，通过回调地址返回（MD5类型，uuid类型，jwt类型等）
 * <p>
 * <p>
 * 二、令牌的生成和管理
 * 创建AccessToken，并保存，以备后续请求访问都可以认证成功并获取到资源
 * AccessToken还有一个潜在功能，就是使用jwt生成token时候，可以用来加载一些信息，把一些相关权限等包含在AccessToken中
 * <p>
 * 创建方法：
 * 1. 可实现AuthorizationServerTokenServices 接口提供了对AccessToken的相关操作创建、刷新、获取
 * 2. spring就默认为我们提供了一个默认的DefaultTokenServices，提供一些基础的操作token
 * 保存方法,创建AccessToken完之后，除了发放给第三方，肯定还得保存起来：
 * 1. inMemoryTokenStore：这个是OAuth2默认采用的实现方式。在单服务上可以体现出很好特效（即并发量不大，并且它在失败的时候不会进行备份），大多项目都可以采用此方法。毕竟存在内存，而不是磁盘中，调试简易。
 * 2. JdbcTokenStore：这个是基于JDBC的实现，令牌（Access Token）会保存到数据库。这个方式，可以在多个服务之间实现令牌共享。
 * 3. JwtTokenStore：jwt全称 JSON Web Token。这个实现方式不用管如何进行存储（内存或磁盘），因为它可以把相关信息数据编码存放在令牌里。JwtTokenStore 不会保存任何数据，但是它在转换令牌值以及授权信息方面与 DefaultTokenServices 所扮演的角色是一样的。但有两个缺点：
 * 撤销一个已经授权的令牌会很困难，因此只适用于处理一个生命周期较短的以及撤销刷新令牌。
 * 令牌占用空间大，如果加入太多用户凭证信息，会存在传输冗余
 * <p>
 * 三、 端点接入
 * 授权认证是使用AuthorizationEndpoint这个端点来进行控制，一般使用AuthorizationServerEndpointsConfigurer 来进行配置。
 * 1. 端点（endpoints）的相关属性配置：
 * authenticationManager：认证管理器。若我们上面的Grant Type设置为password，则需设置一个AuthenticationManager对象
 * userDetailsService：若是我们实现了UserDetailsService,来管理用户信息，那么得设我们的userDetailsService对象
 * authorizationCodeServices：授权码服务。若我们上面的Grant Type设置为authorization_code，那么得设一个AuthorizationCodeServices对象
 * tokenStore：这个就是我们上面说到，把我们想要是实现的Access Token类型设置
 * accessTokenConverter：Access Token的编码器。也就是JwtAccessTokenConverter
 * tokenEnhancer:token的拓展。当使用jwt时候，可以实现TokenEnhancer来进行jwt对包含信息的拓展
 * tokenGranter：当默认的Grant Type已经不够我们业务逻辑，实现TokenGranter 接口，授权将会由我们控制，并且忽略Grant Type的几个属性。
 * 2. 端点（endpoints）的授权url：
 * 要授权认证，肯定得由url请求，才可以传输。因此OAuth2提供了配置授权端点的URL。
 * AuthorizationServerEndpointsConfigurer ，还是这个配置对象进行配置，其中由一个pathMapping()方法进行配置授权端点URL路径
 * 默认实现
 * /oauth/authorize：授权端点
 * /oauth/token：令牌端点
 * /oauth/confirm_access：用户确认授权提交端点
 * /oauth/error：授权服务错误信息端点
 * /oauth/check_token：用于资源服务访问的令牌解析端点
 * /oauth/token_key：提供公有密匙的端点，如果使用JWT令牌的话
 * <p>
 * AuthorizationServerEndpointsConfigurer
 * <p>
 * authorizationCodeServices ：配置验证码服务。
 * implicitGrantService ：配置管理implict 验证的状态。
 * token Grant曰：配置Token Granter 。
 * JwtTokenStore ： 采用JWT 形式，这种形式没有做任何的存储，因为JWT 本身包含了
 * 用户验证的所有信息，不需要存储。采用这种形式， 需要引入spring-jwt 的依赖。
 * AuthorizationServerSecurityConfigurer
 * 如果资源服务和授权服务是在同一个服务中，用默认的配置即可，不需要做其他任何的配
 * 置。但是如果资源服务和授权服务不在同一个服务中，则需要做一些额外配置。如果采用
 * RemoteTokenServices （远程Token 校验），资源服务器的每次请求所携带的Token 都需要从授
 * 权服务做校验。这时需要配置“／oauth/check token ”校验节点的校验策略。
 */
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    /**
     * 自定义授权页面
     */
    @Autowired
    private AuthorizationEndpoint authorizationEndpoint;

    @PostConstruct
    public void init() {
        authorizationEndpoint.setUserApprovalPage("forward:/oauth/approval_page");//自定义授权页面
        authorizationEndpoint.setErrorPage("forward:/oauth/error_page");//自定义授权错误页面
    }

    /**
     * 允许表单验证，浏览器直接发送post请求即可获取tocken
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess(
                "isAuthenticated()");
        oauthServer.allowFormAuthenticationForClients();

    }

    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // oauth_client_details
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // oauth_approvals
        endpoints.approvalStore(approvalStore());
        // oauth_code
        endpoints.authorizationCodeServices(authorizationCodeServices());
        // oauth_access_token & oauth_refresh_token
        endpoints.tokenStore(tokenStore());
        // 支持password grant type
        endpoints.authenticationManager(authenticationManager);
        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(new MyUserAuthenticationConverter());
        endpoints.accessTokenConverter(defaultAccessTokenConverter);


    }

}
