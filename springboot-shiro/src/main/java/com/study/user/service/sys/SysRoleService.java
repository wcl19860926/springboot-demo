package com.study.user.service.sys;

import com.study.common.core.mybaties.service.BaseService;
import com.study.user.entity.sys.SysRole;

import java.util.List;

/**
 * 服务类
 *
 * @author
 * @date Thu Jun 18 15:05:00 CST 2020
 */

public interface SysRoleService extends BaseService<SysRole, String> {


    /**
     * 根据角色Id批量查询角色
     *
     * @param roleIds
     * @return   List<SysRole>
     */
    List<SysRole> queryByRoleIds(List<String> roleIds);


}

