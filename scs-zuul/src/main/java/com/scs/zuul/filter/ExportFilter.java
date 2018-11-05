/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.zuul.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.scs.framework.utils.ConvertUtil;
import com.scs.zuul.service.AuthenticationService;
import com.scs.zuul.vo.LoginUserVO;

/**
 * 
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月15日
 */
public class ExportFilter extends ZuulFilter {
    @Value("${zuul.routes.scs-service-usermanagement}")
    private String userManagement;
    private static final String LOGIN_URL = "sysuser/checklogin";

    @Autowired
    private AuthenticationService authenticationService;

    @Value("${jwt.access_token.expiration:900}")
    private Integer tokenExpiration;
    @Value("${jwt.cookie.httpOnly:false}")
    private Boolean httpOnly;
    @Value("${jwt.cookie.secure:false}")
    private Boolean secure;
    @Value("${jwt.cookie.domain}")
    private String domain;
    @Value("${jwt.cookie.path:/}")
    private String path;
    @Value("${jwt.header:authorization}")
    private String authorization;

    private static Logger logger = LoggerFactory.getLogger(ExportFilter.class);

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.RIBBON_ROUTING_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        response.setHeader("Access-Control-Expose-Headers", authorization);

//        response.addHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        String url = request.getRequestURI();
        if (request.getMethod().equals("GET")
                && userManagement.replace("**", LOGIN_URL).equals(url)) {
            try {
                String json = ConvertUtil.parseString(ctx.getResponseDataStream());
                ctx.setResponseDataStream(ConvertUtil.parseInputStream(json));
                ObjectMapper mapper = new ObjectMapper();
                LoginUserVO user = mapper.readValue(json, LoginUserVO.class);
                String token = authenticationService.getNewTokenByUser(user);
                if (StringUtils.isNotBlank(token)) {
                    response.setHeader(authorization, token);
                }

//                Cookie cookie = new Cookie("scs-token", token);
//                cookie.setPath(path);
//                if (StringUtils.isNoneBlank(domain)) {
//                    cookie.setDomain(domain);
//                }
//                cookie.setSecure(secure);
//                cookie.setHttpOnly(httpOnly);
//                cookie.setMaxAge(tokenExpiration);
//                response.addCookie(cookie);

            } catch (Exception e) {
                logger.error("获取登录返回对象异常", e);
            }
        } else {
            String curToken = response.getHeader(authorization);
            if (StringUtils.isBlank(curToken)) {
                curToken = request.getHeader(authorization);
                if (StringUtils.isNotBlank(curToken)) {
                    response.setHeader(authorization, curToken);
                }
            }
        }
        return null;
    }
}
