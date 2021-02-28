package com.study.oauth2.server.service.security;

import com.study.oauth2.server.entity.Resource;
import com.study.oauth2.server.entity.Role;
import com.study.oauth2.server.entity.User;
import com.study.oauth2.server.service.biz.service.ResourceService;
import com.study.oauth2.server.service.biz.service.RoleService;
import com.study.oauth2.server.service.biz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {



    @Autowired
    public UserService userService;

    @Autowired
    public RoleService roleService;

    @Autowired
    public ResourceService resourceService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        Set<GrantedAuthority> authorities = new HashSet<>();
        List<Role> roles = roleService.findByUserId(user.getId());
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getCode()));
        }
        List<Resource> resources = resourceService.findByUserId(user.getId());
        for (Resource resource : resources) {
            authorities.add(new SimpleGrantedAuthority(resource.getCode()));
        }
        user.setAuthorities(new ArrayList<>(authorities));
        return user;
    }

}
