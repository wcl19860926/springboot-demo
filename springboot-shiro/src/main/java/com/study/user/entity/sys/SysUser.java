package com.study.user.entity.sys;


import com.study.common.core.mybaties.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(value = "", description = "")
public class SysUser extends BaseEntity<String> {


    private static final long serialVersionUID = 1L;


    /**
     * 登录用户名
     */
    @ApiModelProperty(value = "登录用户名")
    private String userCode;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 盐值
     */
    @ApiModelProperty(value = "盐值")
    private String salt;

    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    private Date expireTime;

    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    private Date effectiveTime;

    /**
     * 是否锁定
     */
    @ApiModelProperty(value = "是否锁定")
    private Boolean isLocked;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
