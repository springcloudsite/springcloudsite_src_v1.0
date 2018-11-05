package com.scs.usermanagement.vo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 用户登录对象
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月12日
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 对应字段：login_name,备注：登录名称 */
    @NotBlank(message = "登录名称不能为空")
    private String loginName;

    /** 对应字段：password,备注：密码 */
    @NotBlank(message = "登录密码不能为空")
    private String password;

}
