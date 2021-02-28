package com.study.oauth2.server.service.biz.service.impl;


import com.study.oauth2.server.service.base.impl.BaseServiceImpl;
import com.study.oauth2.server.entity.Resource;
import com.study.oauth2.server.mapper.ResourceMapper;
import com.study.oauth2.server.mybaties.mapper.BaseMapper;
import com.study.oauth2.server.service.biz.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl extends BaseServiceImpl<Resource, Long> implements ResourceService {

    @Autowired
    ResourceMapper mapper;

    @Override
    public BaseMapper<Resource, Long> getMapper() {
        return mapper;
    }

    @Override
    public List<Resource> findByUserId(Long userId) {
        return mapper.findByUserId(userId);
    }

    @Override
    public List<Resource> findByRoleId(Long roleId) {
        return mapper.findByRoleId(roleId);
    }

}
