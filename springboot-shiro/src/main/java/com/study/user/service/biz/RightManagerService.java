package com.study.user.service.biz;


import com.study.user.entity.sys.SysPermission;
import com.study.user.entity.sys.SysRole;
import com.study.user.entity.sys.SysUser;

import java.util.List;

public interface RightManagerService {


    /**
     * 通过UserId 查询用户对应的角色
     * @param userId
     * @return
     */
    List<SysRole>  findRolesByUserId(String userId);


    /**
     * 通过角色Id查询角色对应的权限
     * @param roleId
     * @return
     */
    List<SysPermission>  findPermissionsByRoleId(String roleId);



    /**
     * 通过角色Ids批量查询角色对应的权限信息
     * @param roleIds
     * @return
     */
    List<SysPermission>  findPermissionsByRoleIds(List<String> roleIds);


    /**
     * 通过UserId 查询用户对应的权限信息
     * @param userId
     * @return
     */
    List<SysPermission>  findPermissionsByUserId(String userId);


    /**
     * 通userCode 查询用户信息
     * @param userCode
     * @return
     */
    SysUser    findSysUserByUserCode(String  userCode);
}
