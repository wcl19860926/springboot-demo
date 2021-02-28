package com.study.common.core.id.service.impl;


import com.study.common.core.id.service.IdGeneratorService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * uuid型的ID生成器
 * @author zhangguosheng
 */
@Service("uuidGeneratorService")
public class UuidGeneratorService implements IdGeneratorService<String> {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }

}