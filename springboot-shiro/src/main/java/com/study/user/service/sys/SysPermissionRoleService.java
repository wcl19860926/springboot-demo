package com.study.user.service.sys;

import com.study.common.core.mybaties.service.BaseService;
import com.study.user.entity.sys.SysPermissionRole;

import java.util.List;

/**
 * 服务类
 *
 * @author
 * @date Thu Jun 18 15:05:00 CST 2020
 */

public interface SysPermissionRoleService extends BaseService<SysPermissionRole, String> {


    /**
     * 通过角色Id查找角色权限关联对象
     *
     * @param roleId
     * @return
     */
    List<SysPermissionRole> findByRoleId(String roleId);


    /**
     * 通过角色Id批量查询角色权限关联对象
     *
     * @param roleIds
     * @return
     */
    List<SysPermissionRole> findByRoleIds(List<String> roleIds);

}

