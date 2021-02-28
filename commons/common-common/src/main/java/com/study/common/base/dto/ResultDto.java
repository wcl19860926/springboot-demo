package com.study.common.base.dto;


import com.study.common.base.error.codes.ErrorCode;
import com.study.common.exception.BaseException;
import com.study.common.i18n.I18nMessageHelper;

/**
 * api接口返回的数据格式
 *
 * @param <T>
 */
public class ResultDto<T> {
    private Boolean isSuccess = Boolean.TRUE;
    private Integer code;
    private String  msgKey;
    private T data;
    private String message;


    private ResultDto() {
        this.code = 0;

    }

    private ResultDto(T data) {
        this.code = 0;
        this.data = data;

    }


    private ResultDto(ErrorCode messageCode) {
        this.code = messageCode.getCode();
        this.msgKey  = messageCode.getMsgKey();
        this.isSuccess = Boolean.FALSE;
        this.message=I18nMessageHelper.getI18nMessage(this.msgKey );
    }

    private ResultDto(ErrorCode messageCode, String... args) {
        this.code = messageCode.getCode();
        this.msgKey  = messageCode.getMsgKey();
        this.message=  I18nMessageHelper.getI18nMessage( this.msgKey, args);
        this.isSuccess = Boolean.FALSE;
    }

    private ResultDto(BaseException e) {
        this.code = e.getErrorCode();
        this.isSuccess = Boolean.FALSE;
        this.message =e.getMessage();
        this.msgKey  = e.getMsgKey();
    }


    public static <T> ResultDto<T> success(T data) {
        return new ResultDto<T>(data);
    }

    public static <T> ResultDto<T> success() {
        return new ResultDto<T>();
    }


    public static ResultDto fail(ErrorCode messageCode) {
        return new ResultDto<>(messageCode);
    }

    public static ResultDto fail(ErrorCode messageCode, String... args) {
        return new ResultDto(messageCode, args);
    }

    public static <T> ResultDto<T> fail(BaseException ex) {
        return new ResultDto(ex);
    }


    public Integer getCode() {
        return code;
    }

    public T getData() {
        return data;
    }


    public String getMessage() {
        return  this.message;
    }



}
