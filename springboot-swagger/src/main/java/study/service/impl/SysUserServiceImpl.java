package study.service.impl;

import com.github.pagehelper.Page;
import com.study.entity.SysUser;
import com.study.mapper.SysUserMapper;
import com.study.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysUserServiceImpl  implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Page<SysUser> pageSysUser(Map<String ,Object> params) {
        return (Page<SysUser>) sysUserMapper.queryListPage(params);
    }
}
