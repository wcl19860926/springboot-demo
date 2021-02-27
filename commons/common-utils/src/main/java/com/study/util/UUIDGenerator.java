package com.study.util;

import java.net.InetAddress;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author xiquee.com. <br>
 * @date 2018-11-09 10:16:00
 */
public class UUIDGenerator {

    /**
     * @return
     */
    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private static SnowflakeIdWorker worker = null;

    /**
     * 单例锁
     */
    private static final Object WORKER_LOCK = new Object();

    private static SnowflakeIdWorker getSnowflakeIdWorker() {
        if (worker == null) {
            synchronized (WORKER_LOCK) {
                if (worker == null) {
                    //serverId和workerId的范围 [0,32)
                    int serverId = getServerId();
                    int workerId = new Random().nextInt(32);
                    worker = new SnowflakeIdWorker(workerId, serverId);
                }
            }
        }
        return worker;
    }

    private static int getServerId() {
        try {
            //服务器集群中IP地址一般是连续的，所以取IP地址最后5位，基本可以保证serverId不会重复
            byte[] bytes = InetAddress.getLocalHost().getAddress();
            byte byta = bytes[3];
            int num = ((byta & 0x1F) & 0xFF); //取IP地址的最后5位bit
            return num;
        } catch (Exception ex) {
            //获取不到IP，取随机码
            return new Random().nextInt(32);
        }
    }

    public static String getOrderedUUID() {
        return String.valueOf(getLongUUID());
    }

    private static long getLongUUID() {
        return getSnowflakeIdWorker().nextId();
    }

    /**
     * 获取短随机串，可能重复
     * @param length --长度
     * @return
     */
    public static String getRandomShortString(int length) {
        String uuid = getRandomUUID();
        if (length > 0 && length < 32) {
            return uuid.substring(0,length);
        }
        return uuid;
    }

    /**
     *
     * @return
     */
    public static String getRandomShortString() {
        return getRandomShortString(6);
    }
}
