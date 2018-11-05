/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.configuration;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.github.miemiedev.mybatis.paginator.OffsetLimitInterceptor;
import com.github.miemiedev.mybatis.paginator.dialect.DB2Dialect;
import com.github.miemiedev.mybatis.paginator.dialect.H2Dialect;
import com.github.miemiedev.mybatis.paginator.dialect.HSQLDialect;
import com.github.miemiedev.mybatis.paginator.dialect.MySQLDialect;
import com.github.miemiedev.mybatis.paginator.dialect.OracleDialect;
import com.github.miemiedev.mybatis.paginator.dialect.PostgreSQLDialect;
import com.github.miemiedev.mybatis.paginator.dialect.SQLServerDialect;
import com.github.miemiedev.mybatis.paginator.dialect.SybaseDialect;
import com.github.miemiedev.mybatis.paginator.jackson2.PageListJsonMapper;
import com.scs.framework.core.dao.intercepts.OriginInterceptor;

/**
 * 基本的mybatis配置.
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月22日
 */
@Deprecated
public class SimpleMyBatisConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SimpleMyBatisConfiguration.class);

    @Bean("dataSource")
    @Primary
    @ConfigurationProperties(prefix = "datasource")
    public DataSource getPrimaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public OffsetLimitInterceptor offsetLimitInterceptor(@Qualifier("dataSource") DataSource dataSource)
        throws SQLException {
        OffsetLimitInterceptor offsetLimitInterceptor = new OffsetLimitInterceptor();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();
            if (databaseProductName.toLowerCase().contains("mysql"))
                offsetLimitInterceptor.setDialectClass(MySQLDialect.class.getName());
            else if (databaseProductName.toLowerCase().contains("oracle"))
                offsetLimitInterceptor.setDialectClass(OracleDialect.class.getName());
            else if (databaseProductName.toLowerCase().contains("db2"))
                offsetLimitInterceptor.setDialectClass(DB2Dialect.class.getName());
            else if (databaseProductName.toLowerCase().contains("postgre"))
                offsetLimitInterceptor.setDialectClass(PostgreSQLDialect.class.getName());
            else if (databaseProductName.toLowerCase().contains("sql server"))
                offsetLimitInterceptor.setDialectClass(SQLServerDialect.class.getName());
            else if (databaseProductName.toLowerCase().contains("h2"))
                offsetLimitInterceptor.setDialectClass(H2Dialect.class.getName());
            else if (databaseProductName.toLowerCase().contains("hsql"))
                offsetLimitInterceptor.setDialectClass(HSQLDialect.class.getName());
            else if (databaseProductName.toLowerCase().contains("sybase"))
                offsetLimitInterceptor.setDialectClass(SybaseDialect.class.getName());
            else
                throw new IllegalArgumentException(
                        "Unsupport Database [" + databaseProductName + "]");
            logger.info("Current databaseProductName is [{}]", databaseProductName);
        } catch (SQLException e) {
            logger.error("数据源未找到", e);
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return offsetLimitInterceptor;
    }

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        // 设置中文编码格式
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
        // 配置分页
        mappingJackson2HttpMessageConverter.setObjectMapper(new PageListJsonMapper());
        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public OriginInterceptor originInterceptor(@Qualifier("dataSource") DataSource dataSource)
        throws SQLException {
        OriginInterceptor origin = new OriginInterceptor();
        return origin;
    }

}
