package com.study.user.security.shiro.realm;

import com.study.common.util.EntityUtils;
import com.study.user.entity.sys.SysPermission;
import com.study.user.entity.sys.SysRole;
import com.study.user.entity.sys.SysUser;
import com.study.user.security.shiro.CusAuthenticationInfo;
import com.study.user.service.biz.RightManagerService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class UserServiceRealm extends AuthorizingRealm {


    @Autowired
    private RightManagerService rightManagerService;


    /**
     * 认证信息
     * 验证当前登录的Subject
     * LoginController.login()方法中执行Subject.login()时 执行此方法
     */
    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authonToken) throws AuthenticationException {
        //authonToken  为Subject.login(obj)传过来的对象，
        String userCode = (String) authonToken.getPrincipal();

        SysUser user = rightManagerService.findSysUserByUserCode(userCode);
        if (user == null) {
            throw new UnknownAccountException();
        }
        if (user.getIsDeleted()) {
            throw new DisabledAccountException();
        }
        if (user.getIsLocked()) {
            throw new LockedAccountException();
        }
        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        CusAuthenticationInfo authenticationInfo = new
                CusAuthenticationInfo(user.getId(), user.getUserCode(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
        return authenticationInfo;
    }


    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }


    /**
     * 权限信息
     *
     * @param
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUser sysUser = rightManagerService.findSysUserByUserCode((String) principalCollection.getPrimaryPrincipal());
        List<SysRole> roleList = rightManagerService.findRolesByUserId(sysUser.getId());
        List<SysPermission> sysPermissionList = rightManagerService.findPermissionsByUserId(sysUser.getId());
        authorizationInfo.addRoles(EntityUtils.applyProperty(roleList, SysRole::getCode));
        authorizationInfo.addStringPermissions(EntityUtils.applyProperty(sysPermissionList, SysPermission::getCode));
        return authorizationInfo;
    }


}