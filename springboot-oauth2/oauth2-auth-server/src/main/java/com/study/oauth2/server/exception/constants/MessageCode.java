package com.study.oauth2.server.exception.constants;

import lombok.Data;

@Data
public class MessageCode {
    private int code;
    private String msgKey;

    public MessageCode(int code, String msgKey) {
        this.code = code;
        this.msgKey = msgKey;
    }


}
