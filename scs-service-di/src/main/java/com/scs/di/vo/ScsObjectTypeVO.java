package com.scs.di.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 * 通过Fyz工具自动生成。表scs_object_type的VO对象<br/>
 * 对应表名：scs_object_type,备注：对象类型表
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScsObjectTypeVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 对应字段：id,备注：主键ID */
	private Integer id;

	/** 对应字段：name,备注：对象名称 */
	private String name;

	/** 对应字段：urlpath,备注：选择器路径地址 */
	private String urlpath;

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
