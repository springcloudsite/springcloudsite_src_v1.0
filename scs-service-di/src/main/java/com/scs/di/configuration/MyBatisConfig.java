/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.di.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.scs.framework.core.configuration.AbtractDruidDataSourceConfig;

/**
 * mybatis configuration
 *
 * @version Di v1.0
 * @author Yao YanJun, 2018年9月26日
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.scs.di")
public class MyBatisConfig extends AbtractDruidDataSourceConfig {

}
