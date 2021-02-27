package com.study.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtils <br>
 *
 * @author @author xiquee
 * @date 2018-11-09 10:16:00
 */

public class StringUtils extends org.springframework.util.StringUtils {


    public static final String NUMBER_REGEX = "-?[0-9]*";

    /**
     * 在QueryParam 前无需要使用 替换查询字段的通配符
     */
    public static String replaceSqlPattern(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        str = str.replaceAll("\\\\", "\\\\\\\\");
        str = str.replaceAll("_", "\\\\_");
        str = str.replaceAll("%", "\\\\%");
        return str.trim();
    }

    public static boolean isNumber(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile(NUMBER_REGEX);
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    public static String toCommaString(Collection<String> strs) {
        return collectionToCommaDelimitedString(strs);
    }

    public static boolean checkFieldValid(String regex, String fieldName) {
        if (null == fieldName) {
            return false;
        }
        if (null == regex) {
            return true;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(fieldName);
        if (!m.matches()) {
            return false;
        }
        return true;
    }


    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static String join(Iterable<?> iterable, String separator) {
        return org.apache.commons.lang3.StringUtils.join(iterable, separator);
    }

    public static String join(String separator, String... args) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(args).forEach(s -> {
            stringBuilder.append(s);
            stringBuilder.append(separator);
        });
        return stringBuilder.toString();
    }

    public static List<String> join(String separator, String arg) {
        List<String> list = new ArrayList<>();
        Arrays.stream(arg.split(separator)).forEach(s -> {
            list.add(s);
        });
        return list;
    }

    public static String[] split(String arg, String separator) {
        if (arg == null || separator == null) {
            return new String[0];
        }
        return arg.split(separator);
    }

    public static List<String> splitAsList(String arg, String separator) {
        return Arrays.asList(split(arg,separator));
    }

    /**
     * 返回字符串形式key(value)
     *
     * @param key
     * @param value
     * @return
     */
    public static String getBracket(String key, String value) {
        return String.format("%s(%s)", key, value);
    }
}
