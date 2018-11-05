/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.exception;

import java.io.Serializable;
import java.util.Map;

/**
 * api返回错误的json对象
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class ErrorMessage implements Serializable {

    /**
     * 
     */
    public ErrorMessage() {
        super();
    }

    /**
     * @param errorCode
     * @param errorMessage
     */
    public ErrorMessage(String errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * @param errorCode
     * @param errorMessage
     * @param invalidMessages
     */
    public ErrorMessage(String errorCode, String errorMessage,
            Map<String, String> invalidMessages) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.invalidMessages = invalidMessages;
    }

    /**  */
    private static final long serialVersionUID = -6065990208387911547L;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 用来存放校验结果信息，key代表前端显示用的field id, value代表错误消息
     */
    private Map<String, String> invalidMessages;

    /**
     * @return the error
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param error the error to set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the invalidMessages
     */
    public Map<String, String> getInvalidMessages() {
        return invalidMessages;
    }

    /**
     * @param invalidMessages the invalidMessages to set
     */
    public void setInvalidMessages(Map<String, String> invalidMessages) {
        this.invalidMessages = invalidMessages;
    }

}
