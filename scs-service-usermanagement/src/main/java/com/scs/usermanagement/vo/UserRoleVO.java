package com.scs.usermanagement.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 用户与角色管理列表对象
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月11日
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRoleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 对应字段：id,备注：关系主键 */
    private Integer id;

    /** 用户主键 */
    private Integer userId;

    /** 角色主键 */
    private Integer roleId;

    /** 对应字段：login_name,备注：登录名称 */
    private String loginName;

    /** 对应字段：nickname,备注：昵称 */
    private String nickname;

    /** 对应字段：is_disable,备注：是否禁用，0：可用，1：禁用 */
    private String isDisable;

}
