package com.study.oauth2.server.controller;


import com.study.oauth2.server.controller.base.BaseController;
import com.study.oauth2.server.dto.common.ResultDto;
import com.study.oauth2.server.entity.User;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
@Api(tags = "认证服务--910001－系统用户管理")
public class SysUserController extends BaseController {


    @Autowired
    private SessionRegistry sessionRegistry;


    @GetMapping("/login")
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/user/login");
        HttpSession session = request.getSession();
        AuthenticationException exception = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (exception != null) {
            if (exception instanceof AccountExpiredException) {
                modelAndView.addObject("login_error_tip_key", "login.error.tip.key.account.expire");
            } else if (exception instanceof LockedException) {
                modelAndView.addObject("login_error_tip_key", "login.error.tip.key.account.locked");
            } else {
                modelAndView.addObject("login_error_tip_key", "login.error.tip.key.login.error");
            }
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
        return modelAndView;
    }


    @GetMapping("/kick")
    @ResponseBody
    public ResultDto removeUserSessionByUsername(@RequestParam String username) {
        int count = 0;

        // 获取session中所有的用户信息
        List<Object> users = sessionRegistry.getAllPrincipals();
        for (Object principal : users) {
            if (principal instanceof User) {
                String principalName = ((User) principal).getUsername();
                if (principalName.equals(username)) {
                    // 参数二：是否包含过期的Session
                    List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
                    if (null != sessionsInfo && sessionsInfo.size() > 0) {
                        for (SessionInformation sessionInformation : sessionsInfo) {
                            sessionInformation.expireNow();
                            count++;
                        }
                    }
                }
            }
        }
        return ResultDto.sucess(null);
    }


    @GetMapping("/me")
    @ResponseBody
    public ResultDto loginInfo() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return ResultDto.sucess(authentication);
    }


}
