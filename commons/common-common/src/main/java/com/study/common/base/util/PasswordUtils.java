package com.study.common.base.util;

public class PasswordUtils {
    private static final String LOWERCASE = ".*[a-z]{1,}.*";
    private static final String UPPERCASE = ".*[A-Z]{1,}.*";
    private static final String CHAR = ".*[a-zA-Z]{1,}.*";
    private static final String NUMBER = ".*\\d{1,}.*";
    private static final String SPECIAL = ".*[~!@#$%^+,()&*/_\\\\.?]{1,}.*";
    private static final String COMPLEX = ".*((?=[\\x21-\\x7e]+)[^A-Za-z0-9]).*";


    public static boolean containNum(String password) {
        return password.matches(NUMBER);
    }

    public static boolean containSpecChar(String password) {
        return password.matches(SPECIAL);
    }

    public static boolean containUpCase(String password) {
        return password.matches(UPPERCASE);
    }

    public static boolean containChar(String password) {
        return password.matches(CHAR);
    }


    public static boolean containLowerCase(String password) {
        return password.matches(LOWERCASE);
    }

}
