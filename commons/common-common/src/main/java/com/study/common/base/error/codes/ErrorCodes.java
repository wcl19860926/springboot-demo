package com.study.common.base.error.codes;

public class ErrorCodes {



    public static final ErrorCode SYS_ERROR_401 = new ErrorCode(401, "auth.session.invalid");
    public static final ErrorCode SYS_ERROR_403 = new ErrorCode(403, "auth.no.permission");
    public static final ErrorCode SYS_ERROR_404 = new ErrorCode(404, "system.resource.not.found");
    public static final ErrorCode SYS_ERROR_500 = new ErrorCode(500, "system.exception.error");
    public static final ErrorCode SYS_ERROR_BUSY = new ErrorCode(510, "system.exception.busy");
    /**
     * 操作异常
     */
    public static final ErrorCode OPERATE_SUCCESS = new ErrorCode(200, "system.operation.success");
    public static final ErrorCode OPERATE_FAILED = new ErrorCode(300, "system.operation.fail");
    public static final ErrorCode OPERATE_TIMEOUT = new ErrorCode(408, "system.operation.time.out");

    /**
     * 文件异常10201-10300
     */
    public static final ErrorCode FILE_TYPE_NOT_SUPPORT = new ErrorCode(10201, "sys.file.upload.type.not.support");
    public static final ErrorCode FILE_UPLOAD_FAILED = new ErrorCode(10202, "sys.file.upload.fail");
    public static final ErrorCode FILE_EXCEED_MAX_SIZE = new ErrorCode(10203, "sys.file.upload.file.size.error");

    /**
     * 账户异常10301-10400
     */
    public static final ErrorCode ACCOUNT_LOCKED = new ErrorCode(10301, "sys.user.account.locked");
    public static final ErrorCode ACCOUNT_EXIST = new ErrorCode(10302, "sys.user.account.exist");
    public static final ErrorCode ACCOUNT_USERNAME_OR_PASSWORD_ERROR = new ErrorCode(10304, "sys.user.account.login.error");
    public static final ErrorCode ACCOUNT_PASSWORD_ERROR_TOO_MANY_TIMES = new ErrorCode(10305, "sys.user.account.login.error.too.times");
    public static final ErrorCode ACCOUNT_OLD_PASSWORD_ERROR = new ErrorCode(10306, "sys.user.account.source.password.error");
    public static final ErrorCode ACCOUNT_TWO_PASSWORD_NOT_EQUAL = new ErrorCode(10307, "sys.user.account.password.error.no.equal");
    public static final ErrorCode ACCOUNT_PASSWORD_NOT_CHANGE = new ErrorCode(10308, "sys.user.account.password.has.not.changed");


    public static final ErrorCode ARGS_NOT_LEGAL = new ErrorCode(10003, "request.parameter.invalid");
}
