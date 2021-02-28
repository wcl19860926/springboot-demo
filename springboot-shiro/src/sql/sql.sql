CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `userCode` varchar(50) NOT NULL COMMENT '登录用户名',
  `password` varchar(150) NOT NULL COMMENT '密码',
  `salt` varchar(20) NOT NULL COMMENT '盐值',
  `expireTime` timestamp NOT NULL COMMENT '到期时间',
  `effectiveTime` timestamp NOT NULL COMMENT '生效时间',
  `isLocked` bit(1) DEFAULT b'0' COMMENT '是否锁定',
  `isDeleted` bit(1) DEFAULT b'0' COMMENT '是否删除',
  `createTime` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP comment '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sys_role` (
   `id` varchar(32) NOT NULL ,
   `code` varchar(50) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

CREATE TABLE `sys_role_user` (
  `id` varchar(32)  NOT NULL ,
  `userId` varchar(32)  NOT NULL,
  `roleId` varchar(32)  NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`),
  KEY `idx_roleId` (`roleId`)
) ENGINE=InnoDB ;

CREATE TABLE `sys_permission` (
  `id` varchar(32) NOT NULL ,
  `code` varchar(32) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;
CREATE TABLE `sys_permission_role` (
  `id` varchar(32)  NOT NULL ,
  `roleId` varchar(32)  NOT NULL,
  `permissionId` varchar(32)  NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_role2` (`roleId`),
  KEY `idx_permission` (`permissionId`)
) ENGINE=InnoDB ;



INSERT INTO `sys_user` (`id`, `userCode`, `password`)
VALUES
	(1,'admin','6d789d4353c72e4f625d21c6b7ac2982'),
	(2,'user','36f1cab655c5252fc4f163a1409500b8');


INSERT INTO `sys_role` (`id`, `name` ,`code`)
VALUES
	(2,'ROLE_USER','ROLE_USER' ),
	(3,'ROLE_ADMIN','ROLE_ADMIN');

INSERT INTO `sys_role_user` (`id`, `userId`, `roleId`)
VALUES
	(6,1,3),
	(7,2,2);


INSERT INTO `sys_permission` (`id`, `code` ,`name`,  `description` )
VALUES
	(1,'ROLE_HOME','首页权限',NULL),
	(2,'ROLE_ADMIN','管理员权限',NULL),
	(3,'ROLE_USER','用户权限',NULL);

INSERT INTO `sys_permission_role` (`id`, `roleId`, `permissionId`)
VALUES
	(10,2,1),
	(11,2,3),
	(12,3,1),
	(13,3,2),
	(15,2,2);
















