package com.study.user.service.sys.impl;

import com.study.common.core.mybaties.mapper.BaseMapper;
import com.study.common.core.mybaties.service.impl.BaseServiceImpl;
import com.study.user.entity.sys.SysPermissionRole;
import com.study.user.mapper.sys.SysPermissionRoleMapper;
import com.study.user.service.sys.SysPermissionRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务实现类
 *
 * @author
 * @date Thu Jun 18 15:05:00 CST 2020
 */
@Service(value = "sysPermissionRoleService")
public class SysPermissionRoleServiceImpl extends BaseServiceImpl<SysPermissionRole, String> implements SysPermissionRoleService {


    @Autowired
    private SysPermissionRoleMapper sysPermissionRoleMapper;


    @Override
    public BaseMapper<SysPermissionRole, String> getMapper() {
        return sysPermissionRoleMapper;
    }


    /**
     * 通过角色Id查找角色权限关联对象
     *
     * @param roleId
     * @return
     */
    @Override
    public List<SysPermissionRole> findByRoleId(String roleId) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("roleId", roleId);
        return selectList(paramsMap);
    }

    /**
     * 通过角色Id批量查询角色权限关联对象
     *
     * @param roleIds
     * @return
     */

    @Override
    public List<SysPermissionRole> findByRoleIds(List<String> roleIds) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("roleIds", roleIds);
        return selectList(paramsMap);
    }
}
