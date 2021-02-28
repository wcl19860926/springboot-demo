package com.study.user.service.sys.impl;

import com.study.common.core.mybaties.mapper.BaseMapper;
import com.study.common.core.mybaties.service.impl.BaseServiceImpl;
import com.study.user.entity.sys.SysRole;
import com.study.user.mapper.sys.SysRoleMapper;
import com.study.user.service.sys.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务实现类
 *
 * @author
 * @date Thu Jun 18 15:05:00 CST 2020
 */
@Service(value = "ssyRoleService")
public class SsyRoleServiceImpl extends BaseServiceImpl<SysRole, String> implements SysRoleService {


    @Autowired
    private SysRoleMapper sysRoleMapper;


    @Override
    public BaseMapper<SysRole, String> getMapper() {
        return sysRoleMapper;
    }


    /**
     * 根据角色Id批量查询角色
     *
     * @param roleIds
     * @return List<SysRole>
     */
    @Override
    public List<SysRole> queryByRoleIds(List<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return  new ArrayList<>();
        }
        Map<String ,Object> paramsMap = new HashMap<>();
        paramsMap.put("roleIds" , roleIds);
        return selectList(paramsMap);
    }
}
