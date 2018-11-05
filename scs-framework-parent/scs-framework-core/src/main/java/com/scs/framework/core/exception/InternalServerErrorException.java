/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.exception;

/**
 * 内部错误异常，即不可描述的系统异常
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class InternalServerErrorException extends BusinessException {

    
    /**  */
    private static final long serialVersionUID = 5518795515158273138L;

    /**
     * Constructor for GoneException.
     */
    public InternalServerErrorException() {
        super();
    }

    /**
     * Constructor for GoneException.
     * 
     * @param msg the detail message
     */
    public InternalServerErrorException(String msg) {
        super(msg);
    }

    /**
     * Constructor for GoneException.
     * 
     * @param msg the detail message
     * @param ex the wrapped exception
     */
    public InternalServerErrorException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
