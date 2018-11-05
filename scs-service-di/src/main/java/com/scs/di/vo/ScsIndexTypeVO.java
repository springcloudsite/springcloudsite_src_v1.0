package com.scs.di.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 * 通过Fyz工具自动生成。表scs_index_type的VO对象<br/>
 * 对应表名：scs_index_type,备注：指标类型表
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScsIndexTypeVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 对应字段：id,备注：主键ID */
	private Integer id;
	
	/** 对应字段：parent_id,备注：父级主键 */
    private Integer parentId;

	/** 对应字段：urlpath,备注：选择器路径地址 */
	private String urlpath;

	/** 对应字段：name,备注：类型名称 */
	private String name;

	/** 对应字段：typeid,备注：关联[对象类型表].[ID] */
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
	
	/** 父级指标类型 */
	private String parentTypeName;

	/** 分析对象类型显示名称 */
	private String objectType;
	
}
