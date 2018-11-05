/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.exception;

/**
 * Represent Exception that occured in ScsDI's framework project.
 * 框架异常基类
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月17日
 */
public class ScsFrameworkException extends BaseException {
    

    /**  */
    private static final long serialVersionUID = 9199607764842774181L;

    /**
     * Constructs a new runtime base exception with null as its detail message.
     */
    public ScsFrameworkException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * 
     * @param msg the detail message.
     */
    public ScsFrameworkException(String msg) {
        super(msg);
    }

    /**
     * Construct a new runtime exception with the specified detail message and nested exception.
     * 
     * @param msg the detail message
     * @param cause the nested exception
     */
    public ScsFrameworkException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
