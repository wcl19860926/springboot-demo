package com.study.common.exception;

import com.study.common.base.error.codes.ErrorCode;
import com.study.common.i18n.I18nMessageHelper;
import lombok.Data;

@Data
public class BaseException extends RuntimeException {

    private Integer errorCode;
    private String errorMsg;
    private String msgKey;

    public BaseException(String errorMsg) {
        super(errorMsg);
        this.errorCode = -1;
        this.errorMsg = errorMsg;
    }

    public BaseException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode.getCode();
        this.msgKey = errorCode.getMsgKey();
        this.errorMsg = I18nMessageHelper.getI18nMessage(this.msgKey);
    }

    public BaseException(ErrorCode errorCode, String... args) {
        super();
        this.errorCode = errorCode.getCode();
        this.msgKey = errorCode.getMsgKey();
        this.errorMsg = I18nMessageHelper.getI18nMessage(this.msgKey, args);
    }

    public BaseException(String errorMsg, Throwable e) {
        super(e);
        this.errorMsg = errorMsg;
    }

    public BaseException(Throwable e) {
        super(e);
    }

    @Override
    public String getMessage() {
        return this.errorMsg;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
