package com.scs.usermanagement.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 用户列表页面查询条件
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月8日
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchUser {
    /** 对应字段：login_name,备注：登录名称 */
    private String loginName;
    
    /** 对应字段：login_name,备注：登录名称(模糊查询) */
    private String loginNameLike;

    /** 对应字段：nickname,备注：昵称 */
    private String nickname;
    
    /** 对应字段：nickname,备注：昵称(模糊查询) */
    private String nicknameLike;

    /** 对应字段：is_disable,备注：是否禁用，0：可用，1：禁用 */
    private String isDisable;

    /** 对应字段：is_delete,备注：逻辑删除，0：未删除，1：删除 */
    private String isDelete;
}
