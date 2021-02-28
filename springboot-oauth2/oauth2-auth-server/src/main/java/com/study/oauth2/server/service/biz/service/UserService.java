package com.study.oauth2.server.service.biz.service;

import com.study.oauth2.server.service.base.service.BaseService;
import com.study.oauth2.server.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService extends BaseService<User, Long> {

    User findByUsername(String username);

    List<User> findByName(String name);

    User findByTel(String tel);

    void addUserRole(Long userId, Set<Long> roleIds);

    void updateUserRole(Long userId, Set<Long> roleIds);

}
