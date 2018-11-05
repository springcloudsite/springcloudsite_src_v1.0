/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.exception;

import java.util.Map;

/**
 * Represents the data validation exception.数据校验异常
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月17日
 */
public class UnprocesableEntityException extends BusinessException {

    /**  */
    private static final long serialVersionUID = -8360421444308981849L;

    /**
     * 用来存放校验结果信息，key代表前端显示用的field id, value代表错误消息
     */
    private Map<String, String> map;

    /**
     * Constructor for UnprocesableEntityException.
     */
    public UnprocesableEntityException() {
        super();
    }

    /**
     * Constructor for UnprocesableEntityException.
     * 
     * @param msg the detail message
     */
    public UnprocesableEntityException(String msg) {
        super(msg);
    }

    /**
     * Constructor for UnprocesableEntityException 包含多个字段的验证错误，用于前端显示用
     * 
     * @param map Map, key is property name, value is error message.
     * @param msg String 公共的错误信息
     */
    public UnprocesableEntityException(String msg, Map<String, String> map) {
        super(msg);
        this.map = map;
    }

    /**
     * Constructor for UnprocesableEntityException.
     * 
     * @param msg the detail message
     * @param ex the wrapped exception
     */
    public UnprocesableEntityException(String msg, Throwable ex) {
        super(msg, ex);
    }

    /**
     * @return the map
     */
    public Map<String, String> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
