/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.utils;

/**
 * SCS通用工具类
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月22日
 */
public class ScsUtil {
    /**
     * obj转字符串
     * 
     * @param obj
     * @return
     */
    public static String objectToString(Object obj) {
        return obj == null ? null : obj.toString();
    }
}
