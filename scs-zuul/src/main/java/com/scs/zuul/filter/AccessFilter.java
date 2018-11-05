/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.scs.framework.core.exception.ErrorMessage;
import com.scs.framework.core.web.auth.jwt.JWTUtils;
import com.scs.framework.utils.JacksonHelper;
import com.scs.zuul.service.AuthenticationService;

/**
 * 
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年5月8日
 */
public class AccessFilter extends ZuulFilter {

    public static final String USER_ID = "userId";

    public static final String USER_NAME = "userName";

    private static final String USER_ROLE = "userRole";

    private static final String LOGO_PATH = "/logo/";

    private static final String CAROUSEL_PATH = "/carousel/";

    private static final String FILE_SERVICE_NAME = "scs-service-filemanagement";

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationService authenticationService;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String curToken = request.getHeader(header);

        if (StringUtils.isEmpty(curToken)) {
            curToken = request.getParameter(header);
        }

        if (StringUtils.isEmpty(curToken)) {
            logger.debug("Token String is empty!");

            // 如果没有登录只能看logo图片
            if (!"OPTIONS".equals(request.getMethod())
                    && request.getRequestURI().contains(FILE_SERVICE_NAME)
                    && !(request.getRequestURI().contains(LOGO_PATH)
                            || request.getRequestURI().contains(CAROUSEL_PATH))) {
                // throw new UnauthorizedException("No access to resources!");
                processUnauthorized(ctx);
            }

        } else {
            // 查看token是否有效

            String newToken = authenticationService.getNewToken(curToken); // 获取新的token
            if (StringUtils.isBlank(newToken)) {
                // throw new UnauthorizedException("No access to resources!");
                processUnauthorized(ctx);
            } else {
                ctx.getResponse().setHeader(header, newToken);
            }

            ctx.addZuulRequestHeader(USER_ID,
                    String.valueOf(jwtUtils.getUserIdFromToken(newToken)));
            ctx.addZuulRequestHeader(USER_NAME, jwtUtils.getUsernameFromToken(newToken));
            ctx.addZuulRequestHeader(USER_ROLE, jwtUtils.getUserRoleFromToken(newToken));
        }

        // TODO 添加过滤后的用户信息

        return null;
    }

    /**
     * 未登录，没有权限访问，返回401
     * 
     * @param ctx
     */
    private void processUnauthorized(RequestContext ctx) {

        ErrorMessage errorMessage = new ErrorMessage("No access to resources!",
                "No access to resources!");

        ctx.setResponseStatusCode(401);
        ctx.setSendZuulResponse(false);
        ctx.setResponseBody(JacksonHelper.entityToJson(errorMessage));

    }

}
