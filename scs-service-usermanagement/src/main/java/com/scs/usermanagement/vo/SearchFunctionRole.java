package com.scs.usermanagement.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 资源与角色关系列表页面查询条件
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月12日
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchFunctionRole {
    /** 对应字段：function_name,备注：资源名称 */
    private String functionName;
    
    /** 对应字段：function_name,备注：资源名称(模糊查询) */
    private String functionNameLike;

    /** 对应字段：is_disable,备注：是否禁用，0：可用，1：禁用 */
    private String isDisable;

    /** 角色主键*/
    private Integer roleId;
}
