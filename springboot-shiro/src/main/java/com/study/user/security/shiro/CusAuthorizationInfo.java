package com.study.user.security.shiro;

import lombok.Data;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

import java.util.List;

@Data
public class CusAuthorizationInfo extends SimpleAuthorizationInfo {
    protected List<String> menus;
}
