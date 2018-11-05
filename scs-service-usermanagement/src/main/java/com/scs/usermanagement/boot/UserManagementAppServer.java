/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.usermanagement.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.scs.framework.core.web.boot.ApplicationServer;

/**
 * 
 * 
 *
 * @version usermanagerment v1.0
 * @author Sun Yunxu, 2018年5月7日
 */
@ServletComponentScan
@ComponentScan(basePackages = "com.scs.usermanagement, com.scs.framework.core.web, com.scs.framework.core.dao, com.scs.framework.core.configuration")
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableFeignClients(basePackages = "com.scs.usermanagement")
public class UserManagementAppServer extends ApplicationServer {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementAppServer.class, args);
    }
}
