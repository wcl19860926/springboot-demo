package com.study.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  具体算法参考org.apache.shiro.crypto.hash.Md5Hash
 * 这里是算法简化版
 * @author xiquee.com. <br>
 * @date 2018-11-09 10:16:00
 */
public class MD5HashUtil {

    /**
     * @param str
     * @param salt
     * @param hashIterations
     * @return
     */
    public static String getMD5Hash(String str, String salt, int hashIterations) {
        return getHash("MD5", str, salt, hashIterations);
    }

    /**
     * @param algorithm
     * @param str
     * @param salt
     * @param hashIterations
     * @return
     */
    public static String getHash(String algorithm, String str, String salt, int hashIterations) {
        byte[] saltBytes = null;
        if (salt != null) {
            saltBytes = toBytes(salt, "UTF-8");
        }
        byte[] sourceBytes = toBytes(str, "UTF-8");
        byte[] hashedBytes = hash(algorithm, sourceBytes, saltBytes, hashIterations);

        return HexUtil.encodeToString(hashedBytes).toUpperCase();
    }

    protected static byte[] hash(String algorithm, byte[] bytes, byte[] salt, int hashIterations) {
        MessageDigest digest = getDigest(algorithm);
        if (salt != null) {
            digest.reset();
            digest.update(salt);
        }

        byte[] hashed = digest.digest(bytes);
        int iterations = hashIterations - 1;

        for (int i = 0; i < iterations; ++i) {
            digest.reset();
            hashed = digest.digest(hashed);
        }

        return hashed;
    }

    private static MessageDigest getDigest(String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException var4) {
            String msg = "No native '" + algorithmName + "' MessageDigest instance available on the current JVM.";
            throw new IllegalArgumentException(msg, var4);
        }
    }

    private static byte[] toBytes(String source, String encoding) {
        try {
            return source.getBytes(encoding);
        } catch (UnsupportedEncodingException var4) {
            String msg = "Unable to convert source [" + source + "] to byte array using " + "encoding '" + encoding + "'";
            throw new RuntimeException(msg, var4);
        }
    }
}
