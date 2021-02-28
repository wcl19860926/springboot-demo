package com.study.user.security.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Service
public class KickoutSessionControlFilter extends AccessControlFilter {
    @Autowired
    private DefaultSessionManager onlineSessionManager;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public KickoutSessionControlFilter() {
    }

    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (!this.checkConcurrenceLogin((HttpServletRequest)request)) {
            try {
                Subject subject = SecurityUtils.getSubject();
                this.logger.info(String.format("user [%s] with sessionId [%s] is kicked out", subject.getPrincipal(), subject.getSession().getId()));
                subject.logout();
            } catch (Exception var4) {
                this.logger.error("exception occurred when kick out user", var4);
            }

            this.saveRequest(request);
            ((HttpServletResponse)response).sendError(401, "You are kicked out");
            response.flushBuffer();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkConcurrenceLogin(HttpServletRequest request) {
        return true;
    }
}
