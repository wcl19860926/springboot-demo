package com.study.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.springframework.util.StringUtils {
    public static final String NUMBER_REGEX = "-?[0-9]*";

    public StringUtils() {
    }

    public static String replaceSqlPattern(String str) {
        if (str != null && str.length() != 0) {
            str = str.replaceAll("\\\\", "\\\\\\\\");
            str = str.replaceAll("_", "\\\\_");
            str = str.replaceAll("%", "\\\\%");
            return str.trim();
        } else {
            return str;
        }
    }

    public static boolean isNumber(String str) {
        if (str != null && str.length() != 0) {
            Pattern pattern = Pattern.compile("-?[0-9]*");
            Matcher isNum = pattern.matcher(str);
            return isNum.matches();
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String toCommaString(Collection<String> strs) {
        return collectionToCommaDelimitedString(strs);
    }

    public static boolean checkFieldValid(String regex, String fieldName) {
        if (null == fieldName) {
            return false;
        } else if (null == regex) {
            return true;
        } else {
            Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(fieldName);
            return m.matches();
        }
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


    public static String join(String separator, String... args) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(args).forEach((s) -> {
            stringBuilder.append(s);
            stringBuilder.append(separator);
        });
        return stringBuilder.toString();
    }

    public static List<String> join(String separator, String arg) {
        List<String> list = new ArrayList();
        Arrays.stream(arg.split(separator)).forEach((s) -> {
            list.add(s);
        });
        return list;
    }

    public static String[] split(String arg, String separator) {
        return arg != null && separator != null ? arg.split(separator) : new String[0];
    }

    public static List<String> splitAsList(String arg, String separator) {
        return Arrays.asList(split(arg, separator));
    }

    public static String getBracket(String key, String value) {
        return String.format("%s(%s)", key, value);
    }
}
