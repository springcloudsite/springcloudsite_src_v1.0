/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.web.context;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.scs.framework.utils.JacksonHelper;

/**
 * RequestHeaderUtils.
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class RequestHeaderUtils {

    public static final String USER_ID = "userId";

    public static final String USER_NAME = "userName";

    public static final String USER_ROLE = "userRole";

    private static final String X_REAL_IP = "X-Real-IP";

    private static final String PROXY_FLAG = "X-Forwarded-For";

    private static final String SYSTEM_USER_ID = "2";

    private static final String SYSTEM_USER_NAME = "SystemAdmin";

    public static final String SYSTEM_USER_ROLE = "sys_admin";

    private static Logger log = LoggerFactory.getLogger(RequestHeaderUtils.class);

    private RequestHeaderUtils() {

    }

    /**
     * 得到userId.
     * 
     * @return
     */
    public static String getUserId() {
        try {
            
            if (RequestContextHolder.getRequestAttributes() == null) {
                return SYSTEM_USER_ID;
            }
            
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            if (request != null) {
                if (StringUtils.isEmpty(request.getHeader(USER_ID))) {
                    return SYSTEM_USER_ID;
                } else {
                    return request.getHeader(USER_ID);
                }
            }

            return null;

        } catch (Exception e) {
            log.debug("get request error.", e);
            return null;
        }
    }

    /**
     * 得到登录名.
     * 
     * @return
     */
    public static String getUserName() {
        try {
            
            if (RequestContextHolder.getRequestAttributes() == null) {
                return SYSTEM_USER_NAME;
            }
            
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            if (request != null) {
                if (StringUtils.isEmpty(request.getHeader(USER_NAME))) {
                    return SYSTEM_USER_NAME;
                } else {
                    return request.getHeader(USER_NAME);
                }
            }
            return null;

        } catch (Exception e) {
            log.debug("get request error.", e);
            return null;
        }
    }

    /**
     * 得到登录角色.
     * 
     * @return
     */
    public static List<String> getUserRole() {
        List<String> list = new ArrayList<>();

        try {
            
            if (RequestContextHolder.getRequestAttributes() == null) {
                return list;
            }
            
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            if (request != null) {
                if (StringUtils.isEmpty(request.getHeader(USER_ROLE))) {
                    list.add(SYSTEM_USER_ROLE);
                    return list;
                } else {
                    return JacksonHelper.jsonToEntity(List.class, request.getHeader(USER_ROLE));
                }
            }

            return list;

        } catch (Exception e) {
            log.debug("get request error.", e);
            return list;
        }
    }

    /**
     * 获取客户端ip.
     * 
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request.getHeader(PROXY_FLAG) != null) {
            return request.getHeader(PROXY_FLAG);
        }

        if (request.getHeader(X_REAL_IP) != null) {
            return request.getHeader(X_REAL_IP);
        }
        return request.getRemoteAddr();
    }

    /**
     * 得到header中指定字段.
     * 
     * @return
     */
    public static String getHeaderProperty(String key) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            if (request != null) {
                return request.getHeader(key);
            }

            return null;
        } catch (Exception e) {
            log.debug("get request error.", e);
            return null;
        }
    }
}
