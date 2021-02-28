package com.study.oauth2.server.security.handle;

import com.alibaba.fastjson.JSONObject;
import com.study.oauth2.server.dto.common.ResultDto;
import com.study.oauth2.server.util.ResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class CusAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        ResultDto result = ResultDto.fail(-1);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);
        ResponseUtils.writeStringToResponse(response, jsonObject);
    }
}
