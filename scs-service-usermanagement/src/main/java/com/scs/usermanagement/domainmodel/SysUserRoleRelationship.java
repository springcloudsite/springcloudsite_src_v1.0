package com.scs.usermanagement.domainmodel;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

/**
 *
 * 通过SCS工具自动生成，请勿手工修改。表sys_user_role_relationship的PO对象<br/>
 * 对应表名：sys_user_role_relationship,备注：人员角色关系表
 *
 */
@Data
public class SysUserRoleRelationship implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 对应字段：id,备注：主键 */
	private Integer id;

	/** 对应字段：user_id,备注：人员id */
	private Integer userId;

	/** 对应字段：role_id,备注：角色主键 */
	private Integer roleId;

	/** 对应字段：created_by,备注：创建人 */
	private String createdBy;

	/** 对应字段：created_date,备注：创建时间 */
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd" , timezone="GMT+8")
	private Date createdDate;

	/** 对应字段：last_modified_by,备注：修改人 */
	private String lastModifiedBy;

	/** 对应字段：last_modified_date,备注：修改时间 */
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd" , timezone="GMT+8")
	private Date lastModifiedDate;


}
