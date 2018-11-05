/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
@RunWith(SpringRunner.class)
@Rollback
@Transactional
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class,
    MockitoDependencyInjectionTestExecutionListener.class })
public class BaseDbTest {

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
