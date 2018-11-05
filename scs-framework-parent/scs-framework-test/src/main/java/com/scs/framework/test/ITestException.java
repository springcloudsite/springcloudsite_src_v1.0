/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test;

/**
 * 
 * Exception type, used for all unrecoverable exceptions that occur in iTest.
 * 
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
public class ITestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * ITestException.
     */
    public ITestException() {
    }

    /**
     * ITestException.
     * 
     * @param message message
     */
    public ITestException(String message) {
        super(message);
    }

    /**
     * ITestException.
     * 
     * @param message message
     * @param cause cause
     */
    public ITestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * ITestException.
     * 
     * @param cause cause
     */
    public ITestException(Throwable cause) {
        super(cause);
    }

}
