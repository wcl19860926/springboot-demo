package com.study.oauth2.server.service.biz.service;

import com.study.oauth2.server.service.base.service.BaseService;
import com.study.oauth2.server.entity.Resource;

import java.util.List;

public interface ResourceService extends BaseService<Resource, Long> {

    List<Resource> findByUserId(Long userId);

    List<Resource> findByRoleId(Long roleId);

}
