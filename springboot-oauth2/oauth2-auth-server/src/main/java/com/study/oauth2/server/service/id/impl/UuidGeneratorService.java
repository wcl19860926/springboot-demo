package com.study.oauth2.server.service.id.impl;

import com.study.oauth2.server.service.id.service.IdGeneratorService;
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