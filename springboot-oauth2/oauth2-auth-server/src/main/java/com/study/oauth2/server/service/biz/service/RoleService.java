package com.study.oauth2.server.service.biz.service;



import com.study.oauth2.server.service.base.service.BaseService;
import com.study.oauth2.server.entity.Role;

import java.util.List;

public interface RoleService extends BaseService<Role, Long> {

    List<Role> findByUserId(Long userId);

    void addRoleResources(Long roleId, List<Long> resourcesIds);

    void removeRoleResources(Long roleId, List<Long> resourcesIds);

}
