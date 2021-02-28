package com.study.user.security.shiro.filter;


import com.alibaba.fastjson.JSON;
import com.study.common.base.dto.ResultDto;
import com.study.common.base.error.codes.ErrorCodes;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author : zhaoxuan
 * @date : 2019/12/4
 */
@Slf4j
public class CusFormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {
    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        ResultDto resultBody = ResultDto.fail(ErrorCodes.SYS_ERROR_401);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(resultBody));
        response.flushBuffer();
    }
}
