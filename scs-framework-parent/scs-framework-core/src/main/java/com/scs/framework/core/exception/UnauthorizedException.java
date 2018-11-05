/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.exception;

/**
 * 表示用户没有权限（令牌、用户名、密码错误）。
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class UnauthorizedException extends BusinessException {

    /**  */
    private static final long serialVersionUID = 3052014031725651877L;

    /**
     * Constructor for UnauthorizedException.
     */
    public UnauthorizedException() {
        super();
    }

    /**
     * Constructor for UnauthorizedException.
     * 
     * @param msg the detail message
     */
    public UnauthorizedException(String msg) {
        super(msg);
    }

    /**
     * Constructor for UnauthorizedException.
     * 
     * @param msg the detail message
     * @param ex the wrapped exception
     */
    public UnauthorizedException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
