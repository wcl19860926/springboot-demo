package com.study.export.excel.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

public final class Utf8Utils {

    private static final Pattern UTFPTRN = Pattern.compile("_x([0-9A-F]{4})_");

    private Utf8Utils() {

    }

    /**
     * 过虑掉非unicode字符
     *
     * @param text
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
        byte[] bytes = text.getBytes("UTF-8");
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b > 0) {
                buffer.put(bytes[i++]);
                continue;
            }
            b += 256;
            if ((b ^ 0xC0) >> 4 == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            } else if ((b ^ 0xE0) >> 4 == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            } else if ((b ^ 0xF0) >> 4 == 0) {
                i += 4;
            }
        }
        buffer.flip();
        return new String(buffer.array(), "utf-8");
    }


    /**
     * 全角转半角
     *
     * @param obj 需转换的数据
     * @return 转成半角后的数据
     * @author linjie
     * @data 2017-04-24
     */
    public static String toSBCS(Object obj) {
        String str = (String) obj;
        if (str == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, n = str.length(); i < n; i++) {
            int c = str.charAt(i);
            if ((c >= 'Ａ') && (c <= 'Ｚ')) {
                c = (c + 'A') - 'Ａ';
            } else if ((c >= '０') && (c <= '９')) {
                c = (c + '0') - '０';
            } else if ((c >= 'ａ') && (c <= 'ｚ')) {
                c = (c + 'a') - 'ａ';
            } else if (c == '（') {
                c = (c + '(') - '（';
            } else if (c == '）') {
                c = (c + ')') - '）';
            }
            sb.append((char) c);
        }
        return sb.toString();
    }


}
