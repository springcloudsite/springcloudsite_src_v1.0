package com.scs.di.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 用户登录成功返回对象
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月12日
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 对应字段：id,备注：主键 */
    private Integer userId;

    /** 对应字段：login_name,备注：登录名称 */
    private String loginName;

    /** 对应字段：nickname,备注：昵称 */
    private String nickname;

    /** 角色编码 */
    private List<String> roleCodes;

}
