package com.study.oauth2.server.exception.handler;


import com.study.oauth2.server.dto.common.ResultDto;
import com.study.oauth2.server.exception.BaseException;
import com.study.oauth2.server.exception.UserNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class CommonExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 未知的异常，统一处理
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultDto exceptionHandler(Exception e){
        BaseException ce = null;
        if(e instanceof BaseException){
            ce = (BaseException) e;
            logger.debug(e.getMessage());
        } else {
            ce = new BaseException(50000, e.getMessage());
            logger.debug(e.getMessage(), e);
        }
        return ResultDto.fail(ce);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BaseException.class)
    public ResultDto userNotExistExceptionHandle(UserNotExistException e) {
        BaseException ce = new BaseException(50001, e.getMessage());
        logger.debug(e.getMessage(), e);
        return ResultDto.fail(ce);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public ResultDto accessDeniedExceptionHandler(Exception e){
        BaseException ce = new BaseException(50002, e.getMessage());
        logger.debug(e.getMessage(), e);
        return ResultDto.fail(ce);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ResultDto insufficientAuthenticationExceptionHandler(AuthenticationException e){
        //如果捕获到认证异常，将异常继续向上抛给AuthorizationServer
        //AuthorizationServer捕获到异常后，将页面重定向到登录授权页面
        throw e;
    }

}
