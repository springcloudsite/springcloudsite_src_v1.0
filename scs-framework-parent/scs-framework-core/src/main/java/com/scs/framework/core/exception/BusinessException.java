/*
 * Copyright (c) Travelsky Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.exception;

/**
 * Represents exception of business.业务异常基类
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月17日
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = -5952133866654373982L;

    /**
     * Constructs a new runtime base exception with null as its detail message.
     */
    public BusinessException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * 
     * @param msg the detail message.
     */
    public BusinessException(String msg) {
        super(msg);
    }

    /**
     * Construct a new runtime exception with the specified detail message and nested exception.
     * 
     * @param msg the detail message
     * @param cause the nested exception
     */
    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

}
