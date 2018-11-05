/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期相关工具类
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年5月7日
 */
public class DateUtil {

    private DateUtil() {

    }

    /**
     * 获取当前日期yyyyMMdd
     * 
     * @return
     */
    public static String getSystemDate() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    /**
     * 获取当前时间yyyyMMddHHmmss
     * 
     * @return
     */
    public static String getSystemDatetime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

}
