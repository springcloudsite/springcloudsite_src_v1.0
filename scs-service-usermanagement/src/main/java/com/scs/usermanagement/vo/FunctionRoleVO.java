package com.scs.usermanagement.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 资源与角色管理列表对象
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月12日
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FunctionRoleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 对应字段：id,备注：关系主键 */
    private Integer id;

    /** 资源主键 */
    private Integer functionId;

    /** 角色主键 */
    private Integer roleId;

    /** 对应字段：function_type,备注：资源类型 */
    private String functionType;

    /** 对应字段：function_code,备注：资源编码 */
    private String functionCode;

    /** 对应字段：function_name,备注：资源名称 */
    private String functionName;

    /** 对应字段：parent_id,备注：父级节点Id */
    private Integer parentId;

    /** 对应字段：is_disable,备注：是否禁用，0：可用，1：禁用 */
    private String isDisable;

}
