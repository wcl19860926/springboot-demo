package com.study.oauth2.server.service.biz.service.impl;

import com.study.oauth2.server.service.base.impl.BaseServiceImpl;
import com.study.oauth2.server.entity.User;
import com.study.oauth2.server.entity.UserRole;
import com.study.oauth2.server.mapper.RoleMapper;
import com.study.oauth2.server.mapper.UserMapper;
import com.study.oauth2.server.mybaties.mapper.BaseMapper;
import com.study.oauth2.server.service.biz.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    @Resource
    UserMapper mapper;

    @Resource
    RoleMapper roleMapper;

    @Override
    public BaseMapper<User, Long> getMapper() {
        return mapper;
    }

    @Override
    public User findByUsername(String username) {
        return mapper.findByUsername(username);
    }

    @Override
    public User findByTel(String tel) {
        return mapper.findByTel(tel);
    }

    @Override
    public List<User> findByName(String name) {
        if(name==null){
            name = "";
        }
        return mapper.findByName("%" + name + "%");
    }

    @Override
    public void addUserRole(Long userId, Set<Long> roleIds) {
        List<UserRole> userRoles = new ArrayList<>();
        for(Long roleId : roleIds){
            UserRole ur = new UserRole();
            ur.setId((Long) idGenerator.generate());
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            userRoles.add(ur);
        }
        roleMapper.insertUserRoles(userRoles);
    }

    @Override
    public void updateUserRole(Long userId, Set<Long> roleIds) {
        roleMapper.deleteUserRole(userId);
        this.addUserRole(userId, roleIds);
    }

}
