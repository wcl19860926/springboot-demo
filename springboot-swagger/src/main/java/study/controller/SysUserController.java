package study.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.entity.SysUser;
import com.study.service.SysUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "测试分页插件")
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/list/{page}")
    public PageInfo<SysUser> lists(@PathVariable int page) {
        //设置分页规则
        PageHelper.startPage(page, 10);
        Map<String ,Object>  params  = new HashMap<>();
        // 返回所有分页信息参数为查询所有记录的信息
        PageInfo<SysUser> pageInfo = new PageInfo<>(sysUserService.pageSysUser(params));
        return pageInfo;
    }


}
