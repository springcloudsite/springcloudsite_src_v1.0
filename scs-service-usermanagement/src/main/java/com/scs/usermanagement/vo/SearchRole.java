package com.scs.usermanagement.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 角色列表页面查询条件
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月10日
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRole {
    /** 对应字段：role_code,备注：角色编码 */
    private String roleCode;
    
    /** 对应字段：role_code,备注：角色编码(模糊查询) */
    private String roleCodeLike;

    /** 对应字段：role_name,备注：角色名称 */
    private String roleName;
    
    /** 对应字段：role_name,备注：角色名称(模糊查询) */
    private String roleNameLike;

    /** 对应字段：is_disable,备注：是否禁用，0：可用，1：禁用 */
    private String isDisable;

    /** 对应字段：is_delete,备注：逻辑删除，0：未删除，1：删除 */
    private String isDelete;
}
