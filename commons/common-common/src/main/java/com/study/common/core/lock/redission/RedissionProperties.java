package com.study.common.core.lock.redission;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "redission.lock.server")
@Data
public class RedissionProperties {


    /**
     * redis主机地址，ip：port，有多个用半角逗号分隔
     */
    private String address;

    /**
     * 连接类型，支持standalone-单机节点，sentinel-哨兵，cluster-集群，masterslave-主从
     */
    private String type;

    /**
     * redis 连接密码
     */
    private String password;

    /**
     * 选取那个数据库
     */
    private int database;

    public RedissionProperties setPassword(String password) {
        this.password = password;
        return this;
    }

    public RedissionProperties setDatabase(int database) {
        this.database = database;
        return this;
    }

}
