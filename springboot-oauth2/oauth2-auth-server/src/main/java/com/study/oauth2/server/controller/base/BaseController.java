package com.study.oauth2.server.controller.base;

import com.study.oauth2.server.dto.common.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    public BaseController() {
    }

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    protected ResultDto successResult() {
        return this.getResultDto(ResultDto.ofEmpty());
    }


    protected ResultDto getResultDto(Object object) {
        return ResultDto.sucess(object);
    }


}
