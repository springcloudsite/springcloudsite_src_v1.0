/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.configuration;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.github.miemiedev.mybatis.paginator.OffsetLimitInterceptor;
import com.github.miemiedev.mybatis.paginator.dialect.H2Dialect;
import com.github.miemiedev.mybatis.paginator.dialect.MySQLDialect;
import com.github.miemiedev.mybatis.paginator.dialect.PostgreSQLDialect;
import com.github.miemiedev.mybatis.paginator.jackson2.PageListJsonMapper;
import com.scs.framework.core.dao.intercepts.OriginInterceptor;

/**
 * Druid的DataResource配置类 凡是被Spring管理的类，实现接口 EnvironmentAware 重写方法 setEnvironment 可以在工程启动时，
 * 获取到系统环境变量和application配置文件中的变量。 还有一种方式是采用注解的方式获取 @value("${变量的key值}") 获取application配置文件中的变量。
 * 这里采用第一种要方便些
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月26日
 */
public abstract class AbtractDruidDataSourceConfig implements EnvironmentAware {

    private RelaxedPropertyResolver propertyResolver;

    private Logger logger = LoggerFactory.getLogger(AbtractDruidDataSourceConfig.class);

    @Override
    public void setEnvironment(Environment env) {
        this.propertyResolver = new RelaxedPropertyResolver(env, "druid.datasource.");
    }

    @Bean("dataSource") // 声明其为Bean实例
    @Primary // 在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource() {

        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(propertyResolver.getProperty("url"));
        datasource.setDriverClassName(propertyResolver.getProperty("driver-class-name"));
        datasource.setUsername(propertyResolver.getProperty("username"));
        datasource.setPassword(propertyResolver.getProperty("password"));
        datasource.setInitialSize(Integer.valueOf(propertyResolver.getProperty("initialSize")));
        datasource.setMinIdle(Integer.valueOf(propertyResolver.getProperty("minIdle")));
        datasource.setMaxWait(Long.valueOf(propertyResolver.getProperty("maxWait")));
        datasource.setMaxActive(Integer.valueOf(propertyResolver.getProperty("maxActive")));
        datasource.setMinEvictableIdleTimeMillis(
                Long.valueOf(propertyResolver.getProperty("minEvictableIdleTimeMillis")));
        try {
            datasource.setFilters(propertyResolver.getProperty("spring.datasource.filters"));
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }

        return datasource;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<>();
        // initParameters.put("loginUsername", "druid");// 用户名
        // initParameters.put("loginPassword", "druid");// 密码
        // initParameters.put("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
        // initParameters.put("allow", "127.0.0.1"); // IP白名单 (没有配置或者为空，则允许所有访问)
        // initParameters.put("deny", "192.168.20.38");// IP黑名单
        // (存在共同时，deny优先于allow)
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions",
                "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Bean
    public OffsetLimitInterceptor offsetLimitInterceptor(@Qualifier("dataSource") DataSource dataSource)
        throws SQLException {

        OffsetLimitInterceptor offsetLimitInterceptor = new OffsetLimitInterceptor();

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();

            if (databaseProductName.toLowerCase().contains("mysql"))
                offsetLimitInterceptor.setDialectClass(MySQLDialect.class.getName());
            else if (databaseProductName.toLowerCase().contains("postgre"))
                offsetLimitInterceptor.setDialectClass(PostgreSQLDialect.class.getName());
            else if (databaseProductName.toLowerCase().contains("h2"))
                offsetLimitInterceptor.setDialectClass(H2Dialect.class.getName());
            else
                throw new IllegalArgumentException(
                        "Unsupport Database [" + databaseProductName + "]");

            logger.info("Current databaseProductName is [{}]", databaseProductName);

        } catch (SQLException ex) {
            logger.info("数据源未找到", ex);
        } finally {
            if (conn != null) {
                conn.close();
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
    public OriginInterceptor originInterceptor(@Qualifier("dataSource") DataSource dataSource) {
        OriginInterceptor origin = new OriginInterceptor();
        return origin;
    }

}
