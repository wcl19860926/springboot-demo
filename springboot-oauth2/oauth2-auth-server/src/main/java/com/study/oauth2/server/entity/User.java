package com.study.oauth2.server.entity;

import com.study.oauth2.server.enums.Gender;
import com.study.oauth2.server.mybaties.entity.BaseEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
public class User extends BaseEntity<Long>  implements UserDetails {

    List<GrantedAuthority> authorities ;

    private Long id;
    private String name;
    private String avatarUrl;
    private String username;
    private String password;
    private String tel;
    private Gender gender;
    private Date createTime;
    private Date lastLoginTime;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
