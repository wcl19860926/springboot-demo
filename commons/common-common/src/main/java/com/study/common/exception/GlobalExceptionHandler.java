package com.study.common.exception;

import com.study.common.base.dto.ResultDto;
import com.study.common.base.error.codes.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


/**
 * 子类应继承此， 并且此类不应该标记
 * @RestControllerAdvice ，
 * 否则，spring会注则两个ExceptionHandlerExceptionResolver
 * 在使用时，找到一个就使用
 */
@Slf4j

public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(value = BaseException.class)
    public ResultDto bizExceptionHandler(BaseException bizException) {
        log.error("业务异常:{}", bizException.getErrorMsg());
        return ResultDto.fail(bizException);

    }

    /**
     * 文件异常
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ResultDto maxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        log.error("文件异常:{}", exception.getMessage());
        return ResultDto.fail(ErrorCodes.FILE_EXCEED_MAX_SIZE, "");
    }

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResultDto missingServletRequestParameterException(MissingServletRequestParameterException exception) {
        log.error("业务异常:{}", exception.getMessage());
        return ResultDto.fail(ErrorCodes.ARGS_NOT_LEGAL, exception.getParameterName());
    }



    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    public ResultDto exceptionHandler(Exception exception) {
        log.error("系统异常:{}", exception.getMessage(), exception);
        return ResultDto.fail(ErrorCodes.SYS_ERROR_500);
    }
}
