package com.study.user.mapper.sys;

import com.study.user.entity.sys.SysUser;
import com.study.common.core.mybaties.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface   SysUserMapper   extends  BaseMapper<SysUser , String> {

}