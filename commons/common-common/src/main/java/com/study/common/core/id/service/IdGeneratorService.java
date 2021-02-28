package com.study.common.core.id.service;

import java.io.Serializable;

/**
 * 主键生成器
 * @author zhangguosheng
 */
public interface IdGeneratorService<ID extends Serializable> {

    /**
     * 获取一个主键
     * @return
     */
    ID generate();

}