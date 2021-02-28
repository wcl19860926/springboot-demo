package com.study.user.service.sys;

import com.study.user.entity.sys.SysPermission;
import com.study.common.core.mybaties.service.BaseService;
import com.study.user.entity.sys.SysRole;

import java.util.List;

/**
 *  服务类
 *
 * @author
 * @date Thu Jun 18 15:05:00 CST 2020
 */

public interface SysPermissionService  extends   BaseService<SysPermission , String> {



    /**
     * 根据权限Id批量查询权限信息
     *
     * @param permissionIds
     * @return   List<SysRole>
     */
    List<SysPermission> queryByPermissionIds(List<String> permissionIds);

}

