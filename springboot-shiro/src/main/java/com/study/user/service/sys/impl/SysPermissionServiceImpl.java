package com.study.user.service.sys.impl;

import com.study.common.core.mybaties.mapper.BaseMapper;
import com.study.common.core.mybaties.service.impl.BaseServiceImpl;
import com.study.user.entity.sys.SysPermission;
import com.study.user.mapper.sys.SysPermissionMapper;
import com.study.user.service.sys.SysPermissionService;
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
@Service(value = "sysPermissionService")
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermission, String> implements SysPermissionService {


    @Autowired
    private SysPermissionMapper sysPermissionMapper;


    @Override
    public BaseMapper<SysPermission, String> getMapper() {
        return sysPermissionMapper;
    }

    /**
     * 根据权限Id批量查询权限信息
     *
     * @param permissionIds
     * @return List<SysRole>
     */
    @Override
    public List<SysPermission> queryByPermissionIds(List<String> permissionIds) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("permissionIds", permissionIds);
        return super.selectList(paramsMap);
    }
}
