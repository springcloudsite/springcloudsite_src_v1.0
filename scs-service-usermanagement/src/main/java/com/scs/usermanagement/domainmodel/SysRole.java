package com.scs.usermanagement.domainmodel;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

/**
 *
 * 通过SCS工具自动生成，请勿手工修改。表sys_role的PO对象<br/>
 * 对应表名：sys_role,备注：管理端角色表
 *
 */
@Data
public class SysRole implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 对应字段：id,备注：主键 */
	private Integer id;

	/** 对应字段：role_code,备注：角色代号 */
	private String roleCode;

	/** 对应字段：role_name,备注：角色名称 */
	private String roleName;

	/** 对应字段：role_description,备注：角色描述 */
	private String roleDescription;

	/** 对应字段：is_disable,备注：是否禁用，0：可用，1：禁用 */
	private String isDisable;

	/** 对应字段：is_delete,备注：逻辑删除，0：未删除，1：删除 */
	private String isDelete;

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
