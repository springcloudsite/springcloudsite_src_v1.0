package com.scs.usermanagement.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 * 通过SCS工具自动生成。表sys_function的VO对象<br/>
 * 对应表名：sys_function,备注：后端功能表
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysFunctionVO implements Serializable {
	private static final long serialVersionUID = 1L;

    /** 对应字段：id,备注：主键 */
    private Integer id;

    /** 对应字段：parent_id,备注：父id */
    private Integer parentId;

    /** 对应字段：icon,备注：图标 */
    private String icon;

    /** 对应字段：function_code,备注：功能编号,唯一 */
    private String functionCode;

    /** 对应字段：function_name,备注：功能名称 */
    private String functionName;

    /** 对应字段：function_description,备注：功能描述 */
    private String functionDescription;

    /** 对应字段：url,备注：功能资源链接 */
    private String url;

    /** 对应字段：show_sort,备注：排序字段 */
    private Integer showSort;

    /** 对应字段：function_type,备注：资源类型1菜单2按钮3接口 */
    private String functionType;

    /** 对应字段：is_disable,备注：是否禁用，0：可用，1：禁用 */
    private String isDisable;

    /** 对应字段：created_by,备注：创建人 */
    private String createdBy;

    /** 对应字段：created_date,备注：创建时间 */
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone="GMT+8")
    private Date createdDate;

    /** 对应字段：last_modified_by,备注：修改人 */
    private String lastModifiedBy;

    /** 对应字段：last_modified_date,备注：修改时间 */
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone="GMT+8")
    private Date lastModifiedDate;
    
    /** 父节点中文名称*/
    private String parentFtnName;


}
