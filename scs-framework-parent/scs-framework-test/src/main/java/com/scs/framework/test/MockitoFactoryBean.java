/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test;

import org.mockito.Mockito;
import org.springframework.beans.factory.FactoryBean;

/**
 * 
 * MockitoFactoryBean.
 * 
 * @param <T> class to mock
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
public class MockitoFactoryBean<T> implements FactoryBean<T> {

    private Class<T> mockedClass;

    public Class<T> getMockedClass() {
        return mockedClass;
    }

    public void setMockedClass(Class<T> mockedClass) {
        this.mockedClass = mockedClass;
    }

    @Override
    public T getObject() throws Exception {
        return Mockito.mock(mockedClass);
    }

    @Override
    public Class<?> getObjectType() {
        return mockedClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
