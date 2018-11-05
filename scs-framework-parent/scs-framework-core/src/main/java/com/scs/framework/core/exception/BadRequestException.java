/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.exception;

/**
 * 表示由于资源状态冲突，不能够对此资源操作的异常。 比如订单在某种状态下不能修改。
 * 用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
 * 
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月17日
 */
public class BadRequestException extends BusinessException {
    
    /**  */
    private static final long serialVersionUID = 5254142072828106976L;

    /**
     * Constructor for EmptyResultDataAccessException.
     */
    public BadRequestException() {
        super();
    }

    /**
     * Constructor for EmptyResultDataAccessException.
     * 
     * @param msg the detail message
     */
    public BadRequestException(String msg) {
        super(msg);
    }

    /**
     * Constructor for EmptyResultDataAccessException.
     * 
     * @param msg the detail message
     * @param ex the wrapped exception
     */
    public BadRequestException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
