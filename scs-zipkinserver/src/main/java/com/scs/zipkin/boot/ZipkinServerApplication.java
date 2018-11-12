/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.zipkin.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import zipkin.server.EnableZipkinServer;
 
/**
 * Eureka Server
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */

@SpringBootApplication
@EnableZipkinServer
public class ZipkinServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZipkinServerApplication.class, args);
    }
}
