/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.exception;

/**
 * 用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月17日
 */
public class NotFoundException extends BusinessException {

    /**  */
    private static final long serialVersionUID = 7530683194713369226L;

    /**
     * Constructor for NotFoundException.
     */
    public NotFoundException() {
        super();
    }

    /**
     * Constructor for NotFoundException.
     * 
     * @param msg the detail message
     */
    public NotFoundException(String msg) {
        super(msg);
    }

    /**
     * Constructor for NotFoundException.
     * 
     * @param msg the detail message
     * @param ex the wrapped exception
     */
    public NotFoundException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
