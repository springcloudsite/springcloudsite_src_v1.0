/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test;

import javax.servlet.ServletException;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * MockMvcWebTest. 默认使用applicationContext-test.xml, webApplicationContext.xml配置文件. 提供了mockMvc对象,
 * 并且包含了必须的一些filter.
 * 
 * 如果需要在单元测试中连接生产库，则可以修改这里的配置文件，或者修改配置文件中的数据库连接信息等.
 * 
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
//@ContextConfiguration(locations = { "classpath:applicationContext-test.xml",
//    "classpath:webApplicationContext.xml" })
@TestExecutionListeners({ MockitoDependencyInjectionTestExecutionListener.class })
public class MockMvcWebTest extends H2DbBaseTest {

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Before
    public void prepareMvc() throws ServletException {
        OpenEntityManagerInViewFilter openEntityManagerInViewFilter = new OpenEntityManagerInViewFilter();
        MockFilterConfig filterConfig = new MockFilterConfig(wac.getServletContext(),
                "openEntityManagerInViewFilter");
        openEntityManagerInViewFilter.init(filterConfig);

        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(openEntityManagerInViewFilter).build();
    }
}
