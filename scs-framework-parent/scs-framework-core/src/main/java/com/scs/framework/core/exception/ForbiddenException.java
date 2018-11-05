/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.exception;

/**
 * 表示用户得到授权（与UnauthorizedException错误相对），但是访问是被禁止的，就是没有权限。
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月17日
 */
public class ForbiddenException extends BusinessException {

    /**  */
    private static final long serialVersionUID = -2269608512672741966L;
    /**
     * the privilege code.
     */
    private final String privilegeCode;

    /**
     * Constructs a default ForbiddenException with privilege code.
     * 
     * @param privilegeCode the privilege code.
     */
    public ForbiddenException(String privilegeCode) {
        super(privilegeCode);
        this.privilegeCode = privilegeCode;
    }

    public String getPrivilegeCode() {
        return privilegeCode;
    }

}
