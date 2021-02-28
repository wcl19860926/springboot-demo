package com.study.user.service.sys;

import com.study.user.entity.sys.SysUser;
import com.study.common.core.mybaties.service.BaseService;
/**
 *  服务类
 *
 * @author
 * @date Thu Jun 18 15:05:00 CST 2020
 */

public interface SysUserService  extends   BaseService<SysUser , String> {


    SysUser   findUserByUserCode(String  userCode);


}

