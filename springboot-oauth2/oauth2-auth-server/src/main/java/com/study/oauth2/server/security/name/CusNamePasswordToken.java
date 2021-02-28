package com.study.oauth2.server.security.name;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CusNamePasswordToken extends AbstractAuthenticationToken {

    /**
     * 在 UsernamePasswordAuthenticationToken 中该字段代表登录的用户名，
     * 在这里就代表登录的手机号码
     */
    private final Object principal;
    private Object credentials;


    /**
     * 构建一个没有鉴权的 SmsCodeAuthenticationToken
     */
    public CusNamePasswordToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    /**
     * 构建拥有鉴权的 SmsCodeAuthenticationToken
     */
    public CusNamePasswordToken(Object principal, Object credentials , Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    // ~ Methods
    // ========================================================================================================

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }
}
