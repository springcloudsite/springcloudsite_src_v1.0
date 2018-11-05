package com.scs.usermanagement.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 角色权限查询资源条件对象
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月18日
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FunctionOriginVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 角色主键 */
    private Integer roleId;

    /** 对应字段：function_type,备注：资源类型 */
    private String functionType;

    /** 对应字段：parent_id,备注：父级节点Id */
    private Integer parentId;

    /** 对应字段：is_disable,备注：是否禁用，0：可用，1：禁用 */
    private String isDisable;
    
    /** 角色主键 */
    private List<Integer> roleIds;
    
    /** 角色编码 */
    private List<String> roleCodes;

}
