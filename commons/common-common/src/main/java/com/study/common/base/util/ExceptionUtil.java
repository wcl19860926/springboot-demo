package com.study.common.base.util;


import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {


    /**
     * 获取指定长度的异常堆椎信息
     *
     * @param e
     * @param length
     * @return
     */
    public static String getExceptionMsg(Exception e, int length) {

        String errMsg = ExceptionUtil.getStackMsg(e);
        if (errMsg != null && errMsg.length() > length) {
            return errMsg.substring(0, length);
        }
        return errMsg;
    }


    /**
     * 获取堆堆信息，
     *
     * @param e
     * @return
     */
    public static String getStackMsg(Exception e) {
        if (e != null) {
            StringWriter outputStream = new StringWriter();
            PrintWriter writer = new PrintWriter(outputStream);
            e.printStackTrace(writer);
            writer.flush();
            writer.close();
            return outputStream.toString();
        }
        return null;

    }


}
