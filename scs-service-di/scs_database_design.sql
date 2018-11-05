SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS scs_index;
DROP TABLE IF EXISTS scs_index_type;
DROP TABLE IF EXISTS scs_object;
DROP TABLE IF EXISTS scs_program_management;
DROP TABLE IF EXISTS scs_object_type;
DROP TABLE IF EXISTS scs_program_analysis_results;
DROP TABLE IF EXISTS scs_selector;
DROP TABLE IF EXISTS scs_unit_relation;
DROP TABLE IF EXISTS scs_unit;
DROP TABLE IF EXISTS scs_unit_type;




/* Create Tables */

-- 指标表
CREATE TABLE scs_index
(
	-- 主键ID
	id int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
	-- 类型名称
	name varchar(500) COMMENT '类型名称',
	-- 对象描述
	description varchar(500) COMMENT '对象描述',
	-- 业务唯一标识(到[分析对象管理]功能查询后，添加对应字段)
	businessid varchar(500) COMMENT '业务唯一标识(到[分析对象管理]功能查询后，添加对应字段)',
	-- 对象ID(关联[对象表].[ID]，通过businessid与typeid查询后填入)
	objectid int COMMENT '对象ID(关联[对象表].[ID]，通过businessid与typeid查询后填入)',
	-- 运算单位符号(单位都先与基础单位运算，然后基础运算在与对应单位做运算，公共类解决，提供写死的列表),单位主键
	unit_id int COMMENT '运算单位符号(单位都先与基础单位运算，然后基础运算在与对应单位做运算，公共类解决，提供写死的列表),单位主键',
	-- 历史线性数据(是否为线性数据)
	lineardata varchar(2) COMMENT '历史线性数据(是否为线性数据)',
	-- 指标访问地址(自定义查询格式并提供返回数据使用的地址)
	resturl varchar(500) COMMENT '指标访问地址(自定义查询格式并提供返回数据使用的地址)',
	-- 选择器类型(提供查询条件的选择器)
	selectortype varchar(2) COMMENT '选择器类型(提供查询条件的选择器)',
	-- 关联[指标类型表].[ID]  (来自于左侧树)
	targetid int NOT NULL COMMENT '关联[指标类型表].[ID]  (来自于左侧树)',
	-- 创建人(审计字段)
	created_by int COMMENT '创建人(审计字段)',
	-- 创建时间(审计字段)
	created_date datetime COMMENT '创建时间(审计字段)',
	-- 修改人(审计字段)
	last_modified_by int COMMENT '修改人(审计字段)',
	-- 修改时间(审计字段)
	last_modified_date datetime COMMENT '修改时间(审计字段)',
	PRIMARY KEY (id)
) COMMENT = '指标表';


-- 指标类型表
CREATE TABLE scs_index_type
(
	-- 主键ID
	id int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
	-- 父级主键
	parent_id int COMMENT '父级主键',
	-- 选择器路径地址
	urlpath varchar(500) COMMENT '选择器路径地址',
	-- 类型名称
	name varchar(500) COMMENT '类型名称',
	-- 关联[对象类型表].[ID]
	typeid int NOT NULL COMMENT '关联[对象类型表].[ID]',
	-- 创建人(审计字段)
	created_by int COMMENT '创建人(审计字段)',
	-- 创建时间(审计字段)
	created_date datetime COMMENT '创建时间(审计字段)',
	-- 修改人(审计字段)
	last_modified_by int COMMENT '修改人(审计字段)',
	-- 修改时间(审计字段)
	last_modified_date datetime COMMENT '修改时间(审计字段)',
	PRIMARY KEY (id)
) COMMENT = '指标类型表';


-- 对象表
CREATE TABLE scs_object
(
	-- 主键ID
	id int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
	-- 业务唯一标识（在分支业务系统内的ID(来自于采集的表，如股票代码，热力站名称),在相同typeid时候，不可以重复）
	businessid varchar(100) COMMENT '业务唯一标识（在分支业务系统内的ID(来自于采集的表，如股票代码，热力站名称),在相同typeid时候，不可以重复）',
	-- 对象名称
	name varchar(200) COMMENT '对象名称',
	-- 关联[对象类型表].[ID]
	typeid int NOT NULL COMMENT '关联[对象类型表].[ID]',
	-- 对象描述
	description varchar(500) COMMENT '对象描述',
	-- 创建人
	founder varchar(50) COMMENT '创建人',
	-- 创建人(审计字段)
	created_by int COMMENT '创建人(审计字段)',
	-- 创建时间(审计字段)
	created_date datetime COMMENT '创建时间(审计字段)',
	-- 修改人(审计字段)
	last_modified_by int COMMENT '修改人(审计字段)',
	-- 修改时间(审计字段)
	last_modified_date datetime COMMENT '修改时间(审计字段)',
	PRIMARY KEY (id)
) COMMENT = '对象表';


-- 对象类型表
CREATE TABLE scs_object_type
(
	-- 主键ID
	id int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
	-- 对象名称
	name varchar(200) COMMENT '对象名称',
	-- 选择器路径地址
	urlpath varchar(500) COMMENT '选择器路径地址',
	-- 创建人
	founder varchar(50) COMMENT '创建人',
	-- 创建人(审计字段)
	created_by int COMMENT '创建人(审计字段)',
	-- 创建时间(审计字段)
	created_date datetime COMMENT '创建时间(审计字段)',
	-- 修改人(审计字段)
	last_modified_by int COMMENT '修改人(审计字段)',
	-- 修改时间(审计字段)
	last_modified_date datetime COMMENT '修改时间(审计字段)',
	PRIMARY KEY (id)
) COMMENT = '对象类型表';


-- 分析数据结果表
CREATE TABLE scs_program_analysis_results
(
	-- 主键ID
	id int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
	-- 对象名称
	name varchar(200) COMMENT '对象名称',
	-- 类型(智能分析，对标分析，线性分析)
	analyzetype varchar(20) COMMENT '类型(智能分析，对标分析，线性分析)',
	-- 查看权限(角色查看的权限)
	authority varchar(20) COMMENT '查看权限(角色查看的权限)',
	-- 创建时间
	createtime datetime COMMENT '创建时间',
	-- 生成结果时间
	generatetime datetime COMMENT '生成结果时间',
	-- 生成信息，建立过程中的信息
	loginfo varchar(2000) COMMENT '生成信息，建立过程中的信息',
	-- 生成结果(1成功，2失败)
	result varchar(1) COMMENT '生成结果(1成功，2失败)',
	-- 生成结果存放路径(可以是URL，可以是相对路径)
	jsonpath varchar(500) COMMENT '生成结果存放路径(可以是URL，可以是相对路径)',
	-- 创建人(审计字段)
	created_by int COMMENT '创建人(审计字段)',
	-- 创建时间(审计字段)
	created_date datetime COMMENT '创建时间(审计字段)',
	-- 修改人(审计字段)
	last_modified_by int COMMENT '修改人(审计字段)',
	-- 修改时间(审计字段)
	last_modified_date datetime COMMENT '修改时间(审计字段)',
	PRIMARY KEY (id)
) COMMENT = '分析数据结果表';


-- 方案管理
CREATE TABLE scs_program_management
(
	-- 主键ID
	id int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
	-- 对象名称
	name varchar(200) COMMENT '对象名称',
	-- 类型(智能分析，对标分析，线性分析)
	analyzetype varchar(20) COMMENT '类型(智能分析，对标分析，线性分析)',
	-- 查看权限(角色查看的权限)
	authority varchar(20) COMMENT '查看权限(角色查看的权限)',
	-- 创建时间
	createtime datetime COMMENT '创建时间',
	-- 方案类型ID 关联[对象类型表].[ID]
	typeid int NOT NULL COMMENT '方案类型ID 关联[对象类型表].[ID]',
	-- 创建人(审计字段)
	created_by int COMMENT '创建人(审计字段)',
	-- 创建时间(审计字段)
	created_date datetime COMMENT '创建时间(审计字段)',
	-- 修改人(审计字段)
	last_modified_by int COMMENT '修改人(审计字段)',
	-- 修改时间(审计字段)
	last_modified_date datetime COMMENT '修改时间(审计字段)',
	PRIMARY KEY (id)
) COMMENT = '方案管理';


-- 选择器表
CREATE TABLE scs_selector
(
	-- 主键ID
	id int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
	-- 对象名称
	name varchar(200) COMMENT '对象名称',
	-- 选择器路径地址
	urlpath varchar(500) COMMENT '选择器路径地址',
	-- 创建人(审计字段)
	created_by int COMMENT '创建人(审计字段)',
	-- 创建时间(审计字段)
	created_date datetime COMMENT '创建时间(审计字段)',
	-- 修改人(审计字段)
	last_modified_by int COMMENT '修改人(审计字段)',
	-- 修改时间(审计字段)
	last_modified_date datetime COMMENT '修改时间(审计字段)',
	PRIMARY KEY (id)
) COMMENT = '选择器表';


-- 单位表
CREATE TABLE scs_unit
(
	-- 主键ID
	id int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
	-- 对象名称
	name varchar(200) COMMENT '对象名称',
	-- 关联[单位类型表].[ID]
	typeid int NOT NULL COMMENT '关联[单位类型表].[ID]',
	-- 创建人(审计字段)
	created_by int COMMENT '创建人(审计字段)',
	-- 创建时间(审计字段)
	created_date datetime COMMENT '创建时间(审计字段)',
	-- 修改人(审计字段)
	last_modified_by int COMMENT '修改人(审计字段)',
	-- 修改时间(审计字段)
	last_modified_date datetime COMMENT '修改时间(审计字段)',
	PRIMARY KEY (id)
) COMMENT = '单位表';


-- 单位关系表
CREATE TABLE scs_unit_relation
(
	-- 主键ID
	id int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
	-- 原始单位ID 关联[单位表].[ID]
	unitoriginalid int NOT NULL COMMENT '原始单位ID 关联[单位表].[ID]',
	-- 目标单位ID 关联[单位表].[ID]
	unittargetid int NOT NULL COMMENT '目标单位ID 关联[单位表].[ID]',
	-- 对象名称
	name varchar(200) COMMENT '对象名称',
	-- 创建人(审计字段)
	created_by int COMMENT '创建人(审计字段)',
	-- 创建时间(审计字段)
	created_date datetime COMMENT '创建时间(审计字段)',
	-- 修改人(审计字段)
	last_modified_by int COMMENT '修改人(审计字段)',
	-- 修改时间(审计字段)
	last_modified_date datetime COMMENT '修改时间(审计字段)',
	PRIMARY KEY (id)
) COMMENT = '单位关系表';


-- 单位类型表
CREATE TABLE scs_unit_type
(
	-- 主键ID
	id int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
	-- 对象名称
	name varchar(200) COMMENT '对象名称',
	-- 创建人(审计字段)
	created_by int COMMENT '创建人(审计字段)',
	-- 创建时间(审计字段)
	created_date datetime COMMENT '创建时间(审计字段)',
	-- 修改人(审计字段)
	last_modified_by int COMMENT '修改人(审计字段)',
	-- 修改时间(审计字段)
	last_modified_date datetime COMMENT '修改时间(审计字段)',
	PRIMARY KEY (id)
) COMMENT = '单位类型表';



/* Create Foreign Keys */

ALTER TABLE scs_index
	ADD FOREIGN KEY (targetid)
	REFERENCES scs_index_type (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE scs_index_type
	ADD FOREIGN KEY (typeid)
	REFERENCES scs_object_type (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE scs_object
	ADD FOREIGN KEY (typeid)
	REFERENCES scs_object_type (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE scs_program_management
	ADD FOREIGN KEY (typeid)
	REFERENCES scs_object_type (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE scs_unit_relation
	ADD FOREIGN KEY (unittargetid)
	REFERENCES scs_unit (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE scs_unit_relation
	ADD FOREIGN KEY (unitoriginalid)
	REFERENCES scs_unit (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE scs_unit
	ADD FOREIGN KEY (typeid)
	REFERENCES scs_unit_type (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



