package com.study.common.core.mybaties.service.holder;




import com.study.common.core.id.service.impl.SnowflakeIdGeneratorService;

import java.net.InetAddress;
import java.util.Random;


public class SnowflakeIdGeneratorHolder {


    static class SnowflakeIdGeneratorServiceHolder {

        private static final SnowflakeIdGeneratorService instance = new SnowflakeIdGeneratorService(getServerId(), (new Random()).nextInt(32));


        private static int getServerId() {
            try {
                byte[] bytes = InetAddress.getLocalHost().getAddress();
                byte  b = bytes[3];
                int num = b & 31 & 255;
                return num;
            } catch (Exception var3) {
                return (new Random()).nextInt(32);
            }
        }

    }


    public static SnowflakeIdGeneratorService getInstance() {
        //调用内部类的静态字段，此时内部类初始化
        return SnowflakeIdGeneratorServiceHolder.instance;
    }
}
