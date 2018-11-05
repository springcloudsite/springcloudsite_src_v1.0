SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS sys_role_function_relationship;
DROP TABLE IF EXISTS sys_function;
DROP TABLE IF EXISTS sys_user_role_relationship;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user;




/* Create Tables */

-- 后端功能表
CREATE TABLE sys_function
(
	-- 主键
	id int NOT NULL AUTO_INCREMENT COMMENT '主键',
	-- 父id
	parent_id int COMMENT '父id',
	-- 图标
	icon varchar(100) COMMENT '图标',
	-- 功能编号,唯一
	function_code varchar(40) NOT NULL COMMENT '功能编号,唯一',
	-- 功能名称
	function_name varchar(100) NOT NULL COMMENT '功能名称',
	-- 功能描述
	function_description varchar(200) COMMENT '功能描述',
	-- 功能资源链接
	url varchar(200) COMMENT '功能资源链接',
	-- 排序字段
	show_sort int COMMENT '排序字段',
	-- 资源类型1菜单2按钮3接口
	function_type char(1) DEFAULT '1' COMMENT '资源类型1菜单2按钮3接口',
	-- 是否禁用，0：可用，1：禁用
	is_disable char(1) NOT NULL COMMENT '是否禁用，0：可用，1：禁用',
	-- 创建人
	created_by varchar(50) COMMENT '创建人',
	-- 创建时间
	created_date datetime COMMENT '创建时间',
	-- 修改人
	last_modified_by varchar(50) COMMENT '修改人',
	-- 修改时间
	last_modified_date datetime COMMENT '修改时间',
	PRIMARY KEY (id),
	UNIQUE (function_code)
) COMMENT = '后端功能表';


-- 管理端角色表
CREATE TABLE sys_role
(
	-- 主键
	id int NOT NULL AUTO_INCREMENT COMMENT '主键',
	-- 角色代号
	role_code varchar(20) NOT NULL COMMENT '角色代号',
	-- 角色名称
	role_name varchar(40) COMMENT '角色名称',
	-- 角色描述
	role_description varchar(400) COMMENT '角色描述',
	-- 是否禁用，0：可用，1：禁用
	is_disable char(1) COMMENT '是否禁用，0：可用，1：禁用',
	-- 逻辑删除，0：未删除，1：删除
	is_delete char(1) COMMENT '逻辑删除，0：未删除，1：删除',
	-- 创建人
	created_by varchar(50) COMMENT '创建人',
	-- 创建时间
	created_date datetime COMMENT '创建时间',
	-- 修改人
	last_modified_by varchar(50) COMMENT '修改人',
	-- 修改时间
	last_modified_date datetime COMMENT '修改时间',
	PRIMARY KEY (id)
) COMMENT = '管理端角色表';


-- 后台角色和功能关系表
CREATE TABLE sys_role_function_relationship
(
	-- 主键
	id int NOT NULL AUTO_INCREMENT COMMENT '主键',
	-- 主键
	role_id int NOT NULL COMMENT '主键',
	-- 主键
	function_id int NOT NULL COMMENT '主键',
	-- 创建人
	created_by varchar(50) COMMENT '创建人',
	-- 创建时间
	created_date datetime COMMENT '创建时间',
	-- 修改人
	last_modified_by varchar(50) COMMENT '修改人',
	-- 修改时间
	last_modified_date datetime COMMENT '修改时间',
	PRIMARY KEY (id)
) COMMENT = '后台角色和功能关系表';


-- 系统人员表
CREATE TABLE sys_user
(
	-- 主键
	id int NOT NULL AUTO_INCREMENT COMMENT '主键',
	-- 登录名称
	login_name varchar(20) NOT NULL COMMENT '登录名称',
	-- 昵称
	nickname varchar(40) COMMENT '昵称',
	-- 密码
	password char(32) COMMENT '密码',
	-- 登录名类型，1：登录名，2：手机，3：邮箱
	login_type char(1) COMMENT '登录名类型，1：登录名，2：手机，3：邮箱',
	-- 是否禁用，0：可用，1：禁用
	is_disable char(1) DEFAULT '0' NOT NULL COMMENT '是否禁用，0：可用，1：禁用',
	-- 逻辑删除，0：未删除，1：删除
	is_delete char(1) DEFAULT '0' NOT NULL COMMENT '逻辑删除，0：未删除，1：删除',
	-- 创建人
	created_by varchar(50) COMMENT '创建人',
	-- 创建时间
	created_date datetime COMMENT '创建时间',
	-- 修改人
	last_modified_by varchar(50) COMMENT '修改人',
	-- 修改时间
	last_modified_date datetime COMMENT '修改时间',
	PRIMARY KEY (id)
) COMMENT = '系统人员表';


-- 人员角色关系表
CREATE TABLE sys_user_role_relationship
(
	-- 主键
	id int NOT NULL AUTO_INCREMENT COMMENT '主键',
	-- 人员id
	user_id int NOT NULL COMMENT '人员id',
	-- 角色主键
	role_id int NOT NULL COMMENT '角色主键',
	-- 创建人
	created_by varchar(50) COMMENT '创建人',
	-- 创建时间
	created_date datetime COMMENT '创建时间',
	-- 修改人
	last_modified_by varchar(50) COMMENT '修改人',
	-- 修改时间
	last_modified_date datetime COMMENT '修改时间',
	PRIMARY KEY (id)
) COMMENT = '人员角色关系表';



/* Create Foreign Keys */

ALTER TABLE sys_role_function_relationship
	ADD FOREIGN KEY (function_id)
	REFERENCES sys_function (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE sys_role_function_relationship
	ADD FOREIGN KEY (role_id)
	REFERENCES sys_role (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE sys_user_role_relationship
	ADD FOREIGN KEY (role_id)
	REFERENCES sys_role (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE sys_user_role_relationship
	ADD FOREIGN KEY (user_id)
	REFERENCES sys_user (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



