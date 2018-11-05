package com.scs.di.domainmodel;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

/**
 *
 * 通过SCS工具自动生成，请勿手工修改。表scs_index的PO对象<br/>
 * 对应表名：scs_index,备注：指标表
 *
 */
@Data
public class ScsIndex implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 对应字段：id,备注：主键ID */
	private Integer id;

	/** 对应字段：name,备注：类型名称 */
	private String name;

	/** 对应字段：description,备注：对象描述 */
	private String description;

	/** 对应字段：businessid,备注：业务唯一标识(到[分析对象管理]功能查询后，添加对应字段) */
	private String businessid;

	/** 对应字段：objectid,备注：对象ID(关联[对象表].[ID]，通过businessid与typeid查询后填入) */
	private Integer objectid;

	/** 对应字段：unit_id,备注：运算单位符号(单位都先与基础单位运算，然后基础运算在与对应单位做运算，公共类解决，提供写死的列表),单位主键 */
	private Integer unitId;

	/** 对应字段：lineardata,备注：历史线性数据(是否为线性数据) */
	private String lineardata;

	/** 对应字段：resturl,备注：指标访问地址(自定义查询格式并提供返回数据使用的地址) */
	private String resturl;

	/** 对应字段：selectortype,备注：选择器类型(提供查询条件的选择器) */
	private String selectortype;

	/** 对应字段：targetid,备注：关联[指标类型表].[ID]  (来自于左侧树) */
	private Integer targetid;

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
