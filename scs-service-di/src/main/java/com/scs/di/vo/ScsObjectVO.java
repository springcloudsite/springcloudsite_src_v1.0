package com.scs.di.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 * 通过Fyz工具自动生成。表scs_object的VO对象<br/>
 * 对应表名：scs_object,备注：对象表
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScsObjectVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 对应字段：id,备注：主键ID */
	private Integer id;

	/** 对应字段：businessid,备注：业务唯一标识（在分支业务系统内的ID(来自于采集的表，如股票代码，热力站名称),在相同typeid时候，不可以重复） */
	private String businessid;

	/** 对应字段：name,备注：对象名称 */
	private String name;

	/** 对应字段：typeid,备注：关联[对象类型表].[ID] */
	private Integer typeid;

	/** 对应字段：description,备注：对象描述 */
	private String description;

	/** 对应字段：founder,备注：创建人 */
	private String founder;

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
