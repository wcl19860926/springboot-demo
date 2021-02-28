package com.study.user.controller.sys;


import com.study.common.base.dto.ResultDto;
import com.study.common.base.error.codes.ErrorCodes;
import com.study.common.core.web.controller.BaseController;
import com.study.common.util.StringUtils;
import com.study.user.dto.sys.LoginDto;
import com.study.user.dto.sys.TokenDto;
import com.study.user.service.sys.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Api(value = "系统登录管理", tags = "系统用户登录，登出管理")
@RestController
@RequestMapping("/api/sysUser/auth")
public class SysUserController extends BaseController {


    private static Logger logger = LoggerFactory.getLogger(SysUserController.class);


    @Autowired
    private SysUserService sysUserService;


    @ApiOperation(value = "用户登录", notes = "用户登录")
    @ApiImplicitParam(paramType = "body", name = "loginDto", value = "用户登录参数", required = true, dataType = "LoginDto")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultDto login(@RequestBody @Validated LoginDto loginDto, HttpServletRequest request) throws Exception {
        String userName = loginDto.getUsername();
        loginDto.setUsername(StringUtils.trimWhitespace(userName));
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        //记录当前用户到session，方便后续使用
        SecurityUtils.getSubject().getSession().setAttribute("username", username);
        //获取当前的Subject
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //登录过程
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            logger.info("对用户[" + username + "]进行登录验证..验证开始");
            subject.login(token);
            logger.info("对用户[" + username + "]进行登录验证..验证通过");
        } catch (UnknownAccountException uae) {
            logger.warn("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
            return ResultDto.fail(ErrorCodes.ACCOUNT_USERNAME_OR_PASSWORD_ERROR);
        } catch (IncorrectCredentialsException ice) {
            logger.warn("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
            return ResultDto.fail(ErrorCodes.ACCOUNT_USERNAME_OR_PASSWORD_ERROR);
        } catch (LockedAccountException lae) {
            return ResultDto.fail(ErrorCodes.ACCOUNT_LOCKED);
        } catch (ExcessiveAttemptsException eae) {
            logger.warn("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
            return ResultDto.fail(ErrorCodes.ACCOUNT_PASSWORD_ERROR_TOO_MANY_TIMES);
        } catch (AuthenticationException ae) {
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            logger.warn("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下" + ae.toString());
            return ResultDto.fail(ErrorCodes.ACCOUNT_USERNAME_OR_PASSWORD_ERROR);
        }
        //登录成功
        TokenDto tokenDto = new TokenDto();
        tokenDto.setCreated(new Date());
        tokenDto.setToken((String) SecurityUtils.getSubject().getSession().getId());
        tokenDto.setUsername(username);
        if (subject.isAuthenticated()) {
            logger.info("用户[" + username + "]登录认证通过;当前用户的sessionId：" + tokenDto.getToken());
        }
        return ResultDto.success(tokenDto);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResultDto logout(HttpServletRequest request) throws Exception {
        // 仅退出已登录用户
        if (SecurityUtils.getSubject().isAuthenticated()) {
            String username = (String) SecurityUtils.getSubject().getPrincipal();
            //进行用户的退出，给出提示信息
            SecurityUtils.getSubject().logout();
            return ResultDto.success("您已成功登出！");
        }
        return ResultDto.success();
    }


    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public ResultDto whoami() {
        return ResultDto.success(SecurityUtils.getSubject().getSession().getId());
    }


}
