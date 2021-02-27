package study.service;

import com.github.pagehelper.Page;
import com.study.entity.SysUser;

import java.util.Map;

public interface SysUserService {

    Page<SysUser> pageSysUser(Map<String, Object> params);

}
