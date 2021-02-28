package com.study.common.base.error.codes;

import lombok.Data;

@Data
public class ErrorCode {
    private Integer code;
    private String msgKey;

    public ErrorCode(Integer code, String msgKey) {
        this.code = code;
        this.msgKey = msgKey;
    }
}
