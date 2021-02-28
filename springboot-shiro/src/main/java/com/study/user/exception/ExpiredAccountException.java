package com.study.user.exception;

import org.apache.shiro.authc.AccountException;

public class ExpiredAccountException extends AccountException {

    public ExpiredAccountException() {
        super();
    }

    public ExpiredAccountException(String message) {
        super(message);
    }

    public ExpiredAccountException(Throwable cause) {
        super(cause);
    }

    public ExpiredAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}