package com.study.export.excel.util;

/**
 * @author wcl19
 */
public final class XmlUtils {

    /**
     * 构选方法
     */
    private XmlUtils() {

    }

    /**
     * 转换特殊
     *
     * @param str
     * @return
     */
    public static String encoderXML(String str) {
        if (str == null) {
            return "";
        }
        //去除非utf8的字符
        String temp = str.replaceAll("[^\u0020-\u9FA5]", "");
        int len = temp.length();
        StringBuilder buffer = new StringBuilder();
        char c;
        for (int i = 0; i < len; i++) {
            c = temp.charAt(i);
            switch (c) {
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                default:
                    buffer.append(c);
                    break;
            }
        }
        return buffer.toString();
    }


}
