package com.study.user.security.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 *  XTokenWebSessionManager <br>
 * @author xiquee.com <br>
 * @date 2018-11-09 10:16:00
 */
public class XTokenWebSessionManager extends DefaultWebSessionManager {

    public static final String HEADER_TOKEN = "x-token";

    /**
     * 获取session, 对于跨域请求，所有请求都是无状态的，无法从cookie中获取sessionId，
     * 因此对于跨域请求，要求在http header 中添加 token 头，这里优先从http头中读取token，
     * 如果存在token，则以token作为sessionId
     *
     * @param key --session key
     * @return
     * @throws SessionException
     */
    @Override
    public Session getSession(SessionKey key) throws SessionException {
        if (key instanceof DefaultSessionKey) {
            String headerToken = getTokenFromHeader(key);
            if (headerToken != null && headerToken.trim().length() > 0) {
                ((DefaultSessionKey) key).setSessionId(headerToken);
            }
        }
        return super.getSession(key);
    }

    private String getTokenFromHeader(SessionKey key) {
        HttpServletRequest request = (HttpServletRequest) WebUtils.getRequest(key);
        if (request != null) {
            String token = request.getHeader(HEADER_TOKEN);
            return token;
        }
        return null;
    }
}
