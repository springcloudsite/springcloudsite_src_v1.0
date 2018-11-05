/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.exception;

/**
 * Base class of the exception
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月17日
 */
public class BaseException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new runtime base exception with null as its detail message.
     */
    public BaseException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * 
     * @param msg the detail message.
     */
    public BaseException(String msg) {
        super(msg);
    }

    /**
     * Construct a new runtime exception with the specified detail message and nested exception.
     * 
     * @param msg the detail message
     * @param cause the nested exception
     */
    public BaseException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause and a detail message of
     * <tt>(cause==null ? null : cause.toString()).
     * 
     * @param cause
     */
    public BaseException(Throwable cause) {
        super(cause);
    }

    /**
     * return message include cause.
     * 
     * @return message include cause
     */
    public String getAllMessage() {

        return buildMessage(super.getMessage(), getCause());
    }

    /**
     * Retrieve the innermost cause of this exception, if any.
     * 
     * @return the innermost exception, or {@code null} if none
     */
    public Throwable getRootCause() {
        Throwable rootCause = null;
        Throwable cause = getCause();
        while (cause != null && cause != rootCause) {
            rootCause = cause;
            cause = cause.getCause();
        }

        return rootCause;
    }

    /**
     * Retrieve the most specific cause of this exception, that is, either the innermost cause or
     * this exception itself.
     * <p>
     * Differs from {@link #getRootCause()} in that it falls back to the present exception if there
     * is no root cause.
     * 
     * @return the most specific cause (never {@code null})
     */
    public Throwable getMostSpecificCause() {
        Throwable rootCause = getRootCause();
        return (rootCause != null ? rootCause : this);
    }

    /**
     * Check whether this exception contains an exception of the given type: either it is of the
     * given class itself or it contains a nested cause of the given type.
     * 
     * @param exType the exception type to look for
     * @return whether there is a nested exception of the specified type
     */
    public boolean contains(Class<?> exType) {
        if (exType == null) {
            return false;
        }
        if (exType.isInstance(this)) {
            return true;
        }
        Throwable cause = getCause();
        if (cause == this) {
            return false;
        }
        if (cause instanceof BaseException) {
            return ((BaseException) cause).contains(exType);
        } else {
            while (cause != null) {
                if (exType.isInstance(cause)) {
                    return true;
                }
                if (cause.getCause() == cause) {
                    break;
                }
                cause = cause.getCause();
            }
            return false;
        }
    }

    /**
     * Build a message for the given base message and root cause.
     * 
     * @param message the base message
     * @param cause the root cause
     * @return the full exception message
     */
    private static String buildMessage(String message, Throwable cause) {
        if (cause != null) {
            StringBuilder sb = new StringBuilder();
            if (message != null) {
                sb.append(message).append("; ");
            }
            sb.append("nested exception is ").append(cause);
            return sb.toString();
        } else {
            return message;
        }
    }
}
