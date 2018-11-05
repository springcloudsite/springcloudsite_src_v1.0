/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.web.auth.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年5月8日
 */
public class JWTUserDetails implements UserDetails {

    /**  */
    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String password;

    // 登录名
    private final String username;

    // 角色列表
    private final Collection<String> roles;

    // 权限列表
    private final Collection<? extends GrantedAuthority> authorities;

    // 账户是否未过期
    private final boolean accountNonExpired;

    // 账户是否未锁定
    private final boolean accountNonLocked;

    // 密码是否未过期
    private final boolean credentialsNonExpired;

    // 是否激活
    private final boolean enabled;

    public JWTUserDetails(int userId, String username, String password,
            Collection<? extends GrantedAuthority> authorities, Collection<String> roles) {
        this(userId, username, password, true, true, true, true, authorities, roles);
    }

    public JWTUserDetails(int userId, String username, String password, boolean enabled,
            boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities, Collection<String> roles) {
        if (username != null && !"".equals(username) && password != null) {
            this.userId = userId;
            this.username = username;
            this.password = password;
            this.enabled = enabled;
            this.accountNonExpired = accountNonExpired;
            this.credentialsNonExpired = credentialsNonExpired;
            this.accountNonLocked = accountNonLocked;
            this.authorities = authorities;
            this.roles = roles;
        } else {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public long getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
