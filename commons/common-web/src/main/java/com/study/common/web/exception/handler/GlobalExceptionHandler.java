package com.study.common.web.exception.handler;

import com.study.common.base.dto.ResultDto;
import com.study.common.base.error.codes.ErrorCode;
import com.study.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 统一异常处理器
 *
 * @author LYD
 * @date 2017/7/3
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 自定义异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({BaseException.class})
    public static ResultDto systemException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultDto resultBody = resolveException(ex, request.getRequestURI());
        return resultBody;
    }

    /**
     * 其他异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({Exception.class})
    public static ResultDto exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultDto resultBody = resolveException(ex, request.getRequestURI());
        return resultBody;
    }

    /**
     * 静态解析异常。可以直接调用
     *
     * @param e
     * @return
     */
    public static ResultDto resolveException(Exception e, String path) {
        String message = e.getMessage();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return null;
    }


}
