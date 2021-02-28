package com.study.oauth2.server.security.name;

import com.study.oauth2.server.security.sms.SmsCodeAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 步骤：
 * <p>
 * 1、实现 AuthenticationProvider 接口，实现 authenticate() 和 supports() 方法。
 * 2、 supports() 方法决定了这个 Provider 要怎么被 AuthenticationManager 挑中，
 * 我这里通过 return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication)，处理所有 SmsCodeAuthenticationToken 及其子类或子接口。
 * 3、authenticate() 方法处理验证逻辑。
 * 3.1、首先将 authentication 强转为 SmsCodeAuthenticationToken。
 * 3.2、从中取出登录的 principal，也就是手机号。
 * 3.3、调用自己写的 checkSmsCode() 方法，进行验证码校验，如果不合法，抛出 AuthenticationException 异常。
 * 3.4、如果此时仍然没有异常，通过调用 loadUserByUsername(mobile) 读取出数据库中的用户信息。
 * 3.5、如果仍然能够成功读取，没有异常，这里验证就完成了。
 * 3.6、重新构造鉴权后的 SmsCodeAuthenticationToken，并返回给 SmsCodeAuthenticationFilter 。
 * 4、SmsCodeAuthenticationFilter 的父类在 doFilter() 方法中处理是否有异常，是否成功，根据处理结果跳转到登录成功/失败逻辑。
 */
public class CusNamePassAuthProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CusNamePasswordToken authenticationToken = (CusNamePasswordToken) authentication;

        String mobile = (String) authenticationToken.getPrincipal();

      //  checkSmsCode(mobile);

        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);

        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    private void checkSmsCode(String mobile) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String inputCode = request.getParameter("smsCode");

        Map<String, Object> smsCode = (Map<String, Object>) request.getSession().getAttribute("smsCode");
        if (smsCode == null) {
            throw new BadCredentialsException("未检测到申请验证码");
        }

        String applyMobile = (String) smsCode.get("mobile");
        int code = (int) smsCode.get("code");

        if (!applyMobile.equals(mobile)) {
            throw new BadCredentialsException("申请的手机号码与登录手机号码不一致");
        }
        if (code != Integer.parseInt(inputCode)) {
            throw new BadCredentialsException("验证码错误");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 判断 authentication 是不是 SmsCodeAuthenticationToken 的子类或子接口
        return CusNamePasswordToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
