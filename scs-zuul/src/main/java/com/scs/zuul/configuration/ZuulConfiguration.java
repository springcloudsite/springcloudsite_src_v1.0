/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.zuul.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.scs.zuul.filter.AccessFilter;
import com.scs.zuul.filter.ExportFilter;

/**
 * 
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年5月23日
 */
@Configuration
public class ZuulConfiguration {

    /**
     * 请求接入过滤器
     * 
     * @return
     */
    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }

    /**
     * 请求响应过滤器
     * 
     * @return
     */
    @Bean
    public ExportFilter exportFilter() {
        return new ExportFilter();
    }
}
