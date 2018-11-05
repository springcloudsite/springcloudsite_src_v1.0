/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.di.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.scs.framework.core.web.boot.ApplicationServer;

/**
 * 
 *
 * @version Di v1.0
 * @author Yao YanJun, 2018年9月26日
 */
@ServletComponentScan
@ComponentScan(basePackages = "com.scs.di, com.scs.framework.core.web, com.scs.framework.core.dao, com.scs.framework.core.configuration")
@EnableScheduling
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableFeignClients(basePackages = "com.scs.di")
public class DiAppServer extends ApplicationServer {

    public static void main(String[] args) {
        SpringApplication.run(DiAppServer.class, args);
    }
}
