/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.usermanagement.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.scs.framework.core.configuration.AbtractDruidDataSourceConfig;

/**
 * mybatis configuration
 * 
 *
 * @version usermanagement v1.0
 * @author Sun Yunxu, 2018年5月7日
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.scs.usermanagement.dao")
public class MyBatisConfig extends AbtractDruidDataSourceConfig {

}
