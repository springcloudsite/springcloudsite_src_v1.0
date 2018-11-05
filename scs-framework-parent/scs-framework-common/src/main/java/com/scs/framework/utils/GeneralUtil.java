package com.scs.framework.utils;

import java.util.UUID;

/**
 * 
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月16日
 */
public class GeneralUtil {
    private GeneralUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取UUID
     * 
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取UUID （32位）
     * 
     * @return
     */
    public static String getUUID32() {
        return getUUID().replaceAll("-", "");
    }
}
