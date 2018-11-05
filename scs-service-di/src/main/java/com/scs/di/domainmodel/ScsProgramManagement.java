package com.scs.di.domainmodel;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

/**
 *
 * 通过SCS工具自动生成，请勿手工修改。表scs_program_management的PO对象<br/>
 * 对应表名：scs_program_management,备注：方案管理
 *
 */
@Data
public class ScsProgramManagement implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 对应字段：id,备注：主键ID */
	private Integer id;

	/** 对应字段：name,备注：对象名称 */
	private String name;

	/** 对应字段：analyzetype,备注：类型(智能分析，对标分析，线性分析) */
	private String analyzetype;

	/** 对应字段：authority,备注：查看权限(角色查看的权限) */
	private String authority;

	/** 对应字段：createtime,备注：创建时间 */
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone="GMT+8")
	private Date createtime;

	/** 对应字段：typeid,备注：方案类型ID 关联[对象类型表].[ID] */
	private Integer typeid;

	/** 对应字段：created_by,备注：创建人(审计字段) */
	private Integer createdBy;

	/** 对应字段：created_date,备注：创建时间(审计字段) */
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd" , timezone="GMT+8")
	private Date createdDate;

	/** 对应字段：last_modified_by,备注：修改人(审计字段) */
	private Integer lastModifiedBy;

	/** 对应字段：last_modified_date,备注：修改时间(审计字段) */
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd" , timezone="GMT+8")
	private Date lastModifiedDate;


}
