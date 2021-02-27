package com.study.common.error.code;

import lombok.Data;

@Data
public class MessageCode {
    protected int code;
    protected String messageKey;

    /**
     * @param code
     * @param messageKey
     */
    public MessageCode(Integer code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

}
