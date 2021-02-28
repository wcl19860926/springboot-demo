package com.study.user.security.shiro.util;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.Sha384Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;

public final class HashUtils {

    private HashUtils() {

    }

    /**
     * 将密码Md5
     *
     * @param plainPassword  密码明文
     * @param salt           盐值
     * @param hashIterations hash次数
     * @return
     */
    public static String md5Hash(String plainPassword, String salt, int hashIterations) {
        return new Md5Hash(plainPassword, salt, hashIterations).toHex();
    }

    /**
     * @param plainPassword
     * @param salt
     * @param hashIterations
     * @return
     */
    public static String sHA256Hash(String plainPassword, String salt, int hashIterations) {
        return new Sha256Hash(plainPassword, salt, hashIterations).toHex();
    }

    /**
     * @param plainPassword
     * @param salt
     * @param hashIterations
     * @return
     */
    public static String sHA384Hash(String plainPassword, String salt, int hashIterations) {
        return new Sha384Hash(plainPassword, salt, hashIterations).toHex();
    }

    /**
     * @param plainPassword
     * @param salt
     * @param hashIterations
     * @return
     */
    public static String sHA512Hash(String plainPassword, String salt, int hashIterations) {
        return new Sha512Hash(plainPassword, salt, hashIterations).toHex();
    }


    public static  void  main(String[] args){


        System.out.println(md5Hash("123" ,"123" ,16));
    }
}
