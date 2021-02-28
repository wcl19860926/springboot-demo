package com.study.oauth2.server.security.handle;


import com.alibaba.fastjson.JSONObject;
import com.study.oauth2.server.dto.common.ResultDto;
import com.study.oauth2.server.util.ResponseUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CusAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        ResultDto result = ResultDto.sucess("登录成功");
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);
        ResponseUtils.writeStringToResponse(response, jsonObject);

    }


}
