package study.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by LombokPlugin on 2020/01/16
 * Table:     sys_user
 */
@Data
public class SysUser {
    /**
     *   user id
     *   sys_user.id
     */
    private Integer id;

    /**
     *   工号
     *   sys_user.code
     */
    private String code;

    /**
     *   用户名
     *   sys_user.userName
     */
    private String userName;

    /**
     *   姓名
     *   sys_user.fullName
     */
    private String fullName;

    /**
     *   密码
     *   sys_user.password
     */
    private String password;

    /**
     *   盐值
     *   sys_user.credentialSalt
     */
    private String credentialSalt;

    /**
     *   性别 F:女 M：男
     *   sys_user.sex
     */
    private String sex;

    /**
     *   移动电话
     *   sys_user.mobilePhone
     */
    private Long mobilePhone;

    /**
     *   座机电话
     *   sys_user.telephone
     */
    private String telephone;

    /**
     *   职位代码
     *   sys_user.positionCode
     */
    private String positionCode;

    /**
     *   电子邮件
     *   sys_user.email
     */
    private String email;

    /**
     *   是否需要重新设置密码 0：不是 1：是
     *   sys_user.needResetPwd
     */
    private Boolean needResetPwd;

    /**
     *   头像Id
     *   sys_user.avatarId
     */
    private String avatarId;

    /**
     *   是否系统预设 0：不是 1：是
     *   sys_user.isSystem
     */
    private Boolean isSystem;

    /**
     *   是否锁定 0：不是 1：是
     *   sys_user.isLocked
     */
    private Boolean isLocked;

    /**
     *   是否删除 0：不是 1：是
     *   sys_user.isDeleted
     */
    private Boolean isDeleted;

    /**
     *   描述
     *   sys_user.description
     */
    private String description;

    /**
     *   创建时间
     *   sys_user.createTime
     */
    private Date createTime;

    /**
     *   最近一次修改时间
     *   sys_user.lastUpdateTime
     */
    private Date lastUpdateTime;
}