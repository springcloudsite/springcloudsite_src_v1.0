/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.service;

import java.lang.reflect.Field;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import com.scs.framework.core.web.context.RequestHeaderUtils;

/**
 * 审计字段的自动处理.
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class AuditPropertyUtils {

    private static Logger logger = LoggerFactory.getLogger(AuditPropertyUtils.class);

    private AuditPropertyUtils() {

    }

    /**
     * 处理添加人.
     * 
     * @param entity
     */
    public static void processCreatedBy(Object entity) {
        //processAuditor("createdBy", entity, PrincipalContext.getCurrentUser());
        processAuditor("createdBy", entity, RequestHeaderUtils.getUserName());
    }

    /**
     * 处理修改人.
     * 
     * @param entity
     */
    public static void processModifyBy(Object entity) {
        //processAuditor("lastModifiedBy", entity, PrincipalContext.getCurrentUser());
        processAuditor("lastModifiedBy", entity, RequestHeaderUtils.getUserName());
    }

    private static void processAuditor(String field, Object entity, Object value) {
        Field createdBy = ReflectionUtils.findField(entity.getClass(), field);
        if (createdBy != null) {
            createdBy.setAccessible(true);
            try {
                createdBy.set(entity, value);
            } catch (Exception e) {
                logger.error("{} error.", field, e);
            }
        }
    }

    /**
     * 处理添加日期.
     * 
     * @param entity
     */
    public static void processCreatedDate(Object entity) {
        processAuditor("createdDate", entity, new Date());
    }

    /**
     * 处理修改日期.
     * 
     * @param entity
     */
    public static void processModifyDate(Object entity) {
        processAuditor("lastModifiedDate", entity, new Date());
    }
}
