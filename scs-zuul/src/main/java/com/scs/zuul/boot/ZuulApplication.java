/*
 * Copyright (c) Travelsky Corp.
 * All Rights Reserved.
 */
package com.scs.zuul.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * 
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年5月4日
 */
@EnableZuulProxy
@EnableEurekaClient
@ComponentScan(basePackages = "com.scs.framework.core.web, com.scs.framework.core.dao, com.scs.zuul, com.scs.framework.core.configuration")
@EnableFeignClients(basePackages = "com.scs.zuul")
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ZuulApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ZuulApplication.class).web(true).run(args);
    }

}
