package com.scs.usermanagement.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 菜单对象
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月18日
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuFunctionVO implements Serializable {
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

}
