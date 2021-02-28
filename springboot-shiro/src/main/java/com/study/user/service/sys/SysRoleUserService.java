package com.study.user.service.sys;

import com.study.user.entity.sys.SysRoleUser;
import com.study.common.core.mybaties.service.BaseService;

import java.util.List;

/**
 *  服务类
 *
 * @author
 * @date Thu Jun 18 15:05:00 CST 2020
 */

public interface SysRoleUserService  extends   BaseService<SysRoleUser , String> {


    /**
     *能过用户Id查询对应用户角色关系
     * @param userId
     * @return
     */
    List<SysRoleUser>  findByUserId(String userId);


}

