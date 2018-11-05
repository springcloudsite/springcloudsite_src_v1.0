/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.exception;

/**
 * 用户请求的资源被永久删除，且不会再得到的。
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class GoneException extends BusinessException {

    /**  */
    private static final long serialVersionUID = 4269104691356641863L;

    /**
     * Constructor for GoneException.
     */
    public GoneException() {
        super();
    }

    /**
     * Constructor for GoneException.
     * 
     * @param msg the detail message
     */
    public GoneException(String msg) {
        super(msg);
    }

    /**
     * Constructor for GoneException.
     * 
     * @param msg the detail message
     * @param ex the wrapped exception
     */
    public GoneException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
