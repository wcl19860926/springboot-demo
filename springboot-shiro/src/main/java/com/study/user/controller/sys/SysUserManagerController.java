package com.study.user.controller.sys;


import com.study.common.base.dto.Page;
import com.study.common.base.dto.PageQuery;
import com.study.common.base.dto.ResultDto;
import com.study.common.core.web.controller.BaseController;
import com.study.common.i18n.I18nMessageHelper;
import com.study.user.service.sys.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "用户权限管理", tags = "用户权限管理")
@RestController
@RequestMapping("/api/sysUser/manger")
public class SysUserManagerController extends BaseController {


    private static Logger logger = LoggerFactory.getLogger(SysUserManagerController.class);


    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "分面查询用户列表", notes = "分面查询用户列表")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "pageIndex", value = "分页", dataType = "int", paramType = "query", defaultValue
                    = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数,max<2000", dataType = "int", paramType = "query",
                    defaultValue = "10"),

    })

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultDto list(@RequestParam  Integer pageIndex ,
                          @RequestParam  Integer  pageSize) throws Exception {
        PageQuery pageParams = new PageQuery(pageSize , pageIndex);
        I18nMessageHelper.getI18nMessage("auth.session.invalid");
        I18nMessageHelper.getI18nMessage("hello");
        return ResultDto.success(sysUserService.selectByPage(pageParams));
    }



    @RequiresPermissions("456")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResultDto test(@RequestParam  Integer pageIndex ,
                          @RequestParam  Integer  pageSize) throws Exception {
        PageQuery pageParams = new PageQuery(pageSize , pageIndex);
        return ResultDto.success(sysUserService.selectByPage(pageParams));
    }



    @RequiresPermissions(value = "123,456" ,logical = Logical.OR)
    @RequestMapping(value = "/testOR", method = RequestMethod.POST)
    public ResultDto testOR(@RequestParam  Integer pageIndex ,
                          @RequestParam  Integer  pageSize) throws Exception {
        PageQuery pageParams = new PageQuery(pageSize , pageIndex);
        return ResultDto.success(sysUserService.selectByPage(pageParams));
    }



    @RequiresPermissions(value = "123,456" ,logical = Logical.OR)
    @RequestMapping(value = "/testAnd", method = RequestMethod.POST)
    public ResultDto testAnd(@RequestParam  Integer pageIndex ,
                            @RequestParam  Integer  pageSize) throws Exception {
        PageQuery pageParams = new PageQuery(pageSize , pageIndex);
        return ResultDto.success(sysUserService.selectByPage(pageParams));
    }


}
