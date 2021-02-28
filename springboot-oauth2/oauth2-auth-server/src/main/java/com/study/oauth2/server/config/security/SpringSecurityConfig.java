package com.study.oauth2.server.config.security;

import com.study.oauth2.server.service.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Bean
    protected DaoAuthenticationProvider provider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
        provider.setHideUserNotFoundExceptions(false);
        provider.setForcePrincipalAsString(true);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/logout", "/code/image", "/code/mobile").permitAll()
                //这个地址由AuthorizationServer使用，不需要登录拦截
                .antMatchers("/oauth/**").permitAll()
                //这个地址开放地址
                .antMatchers("/public/**").permitAll()
                .and().formLogin()
                .loginPage("/user/login").permitAll().loginProcessingUrl("/user/login/process").permitAll().defaultSuccessUrl("/index").and()
                //.successHandler(new MyAuthenticationSuccessHandler())//.defaultSuccessUrl("/index")
                .logout().logoutUrl("/user/logout").logoutSuccessUrl("/user/login").and().authorizeRequests()
                .anyRequest().authenticated().and()
                .csrf().disable();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/favicon.ico", "/asserts/**", "/webjars/**");
    }


    /**
     * 四、踢出用户
     * 下面来看下如何主动踢出一个用户。
     * <p>
     * 首先需要在容器中注入名为 SessionRegistry 的 Bean，这里我就简单的写在 WebSecurityConfig 中：
     *
     * @return
     */
    @Bean("sessionRegistry")
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}
