package com.study.user.service.sys.impl;

import com.study.common.core.mybaties.mapper.BaseMapper;
import com.study.common.core.mybaties.service.impl.BaseServiceImpl;
import com.study.user.entity.sys.SysRoleUser;
import com.study.user.mapper.sys.SysRoleUserMapper;
import com.study.user.service.sys.SysRoleUserService;
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
@Service(value = "sysRoleUserService")
public class SysRoleUserServiceImpl extends BaseServiceImpl<SysRoleUser, String> implements SysRoleUserService {


    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;


    @Override
    public BaseMapper<SysRoleUser, String> getMapper() {
        return sysRoleUserMapper;
    }

    /**
     * 能过用户Id查询对应用户角色关系
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysRoleUser> findByUserId(String userId) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        return super.selectList(paramsMap);
    }
}
