package com.study.user.controller.sys;


import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.study.common.core.web.controller.BaseController;
import com.study.user.service.sys.SysRoleService;

/**
 *  前端控制器
 *
 * @author
 * @date Thu Jun 18 15:05:00 CST 2020
 */
@Api(value = "", tags = "")
@RestController
@RequestMapping("/ssyRole")
public class SsyRoleController extends  BaseController {


    @Autowired
    private SysRoleService ssyRoleService;





}
