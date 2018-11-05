package com.scs.di.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 * 通过Fyz工具自动生成。表scs_unit_relation的VO对象<br/>
 * 对应表名：scs_unit_relation,备注：单位关系表
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScsUnitRelationVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 对应字段：id,备注：主键ID */
	private Integer id;

	/** 对应字段：unitoriginalid,备注：原始单位ID 关联[单位表].[ID] */
	private Integer unitoriginalid;

	/** 对应字段：unittargetid,备注：目标单位ID 关联[单位表].[ID] */
	private Integer unittargetid;

	/** 对应字段：name,备注：对象名称 */
	private String name;

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
