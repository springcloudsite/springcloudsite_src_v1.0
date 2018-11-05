/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.scs.framework.test.listener.DataSetTestExecutionListener;

/**
 * H2DbBaseTest.
 * 
 * @TransactionConfiguration(defaultRollback = false)必须和@Transactional共用.
 * 
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration(value = "src/main/webapp")  
@Commit
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class,
        DataSetTestExecutionListener.class })
public class H2DbBaseTest {

}
