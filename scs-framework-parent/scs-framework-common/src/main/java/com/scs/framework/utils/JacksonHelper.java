/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.utils;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JacksonHelper.提供静态方法实体到json，json到实体的转换
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年5月8日
 */
public class JacksonHelper {

    private JacksonHelper() {

    }

    private static Logger log = LoggerFactory.getLogger(JacksonHelper.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * entity to json.
     * 
     * @param entity
     * @return
     */
    public static String entityToJson(Object entity) {

        if (entity == null) {
            return "";
        }

        try {
            return objectMapper.writeValueAsString(entity);

        } catch (IOException e) {

            log.error(e.getMessage());
            return "";

        }

    }

    /**
     * map to json
     * 
     * @param map
     * @return
     */
    public static String mapToJson(Map map) {
        if (map == null) {
            return "";
        }

        try {
            return objectMapper.writeValueAsString(map);
        } catch (IOException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    /**
     * json to entity. NOTE: clazz不能是一个内部类
     * 
     * @param <T>
     * @param clazz
     * @param str
     * @return
     */
    public static <T> T jsonToEntity(Class<T> clazz, String str) {
        if (str == null) {
            return null;
        }

        try {

            return objectMapper.readValue(str, clazz);

        } catch (IOException e) {

            log.error(e.getMessage());
            return null;

        }
    }

    /**
     * 通过给定的属性名，读取值.
     * 
     * @param json
     * @param propertyName
     * @return
     */
    public static String getValue(String json, String propertyName) {
        try {
            return objectMapper.readTree(json).get(propertyName).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    
    /**
     * 通过给定的属性名，读取值 去掉 头尾 双引号
     * 
     * @param json
     * @param propertyName
     * @return
     */
    public static String getValueTwo(String json, String propertyName) {
        try {
        	String str = objectMapper.readTree(json).get(propertyName).toString();
        	if(str.indexOf('"') == 0) {
        		str = str.substring(1, str.length());
        	}
        	if(str.lastIndexOf('"') == str.length()-1) {
        		str = str.substring(0,str.length()-1);
        	}
            return str;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
