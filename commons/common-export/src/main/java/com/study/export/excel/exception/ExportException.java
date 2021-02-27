package com.study.export.excel.exception;


public class ExportException extends Exception {


    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }


    public ExportException(Throwable cause) {
        super(cause);
    }


    public ExportException(String message) {
        super(message);
    }
}
