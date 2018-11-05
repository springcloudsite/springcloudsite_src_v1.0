/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.ReflectionUtils;

/**
 * MockitoDependencyInjectionTestExecutionListener. 自动注入需要Mock的对象.
 * 例如，OrganizationControllerTest单元测试中，测试OrganizationController中的方法。
 * 而controller调用了organizationService中的方法
 * ，但是organizationService中需要调用resstInvoker方法，但是restInvoker方法是调用外部的服务，可能不存在服务，所以需要mock一下。
 * 那么只需要在OrganizationControllerTest类中声明：
 * 
 * @Autowired private OrganizationService organizationService;
 * @Mock
 * @Autowired private RestInvoker restInvoker;
 * 
 * 
 *            然后在测试的方法中对对象进行mock操作，Mockito.when(this.restInvoker.get(Mockito.anyString(),
 *            Mockito.anyString(), Mockito.any(Object[].class))).thenReturn(null);
 *            当代码执行到真正调用restInvoker的方法时，就会执行mock之后的方法了.
 * 
 * @version Araf v1.0
 * @author Yu Tao, 2015-4-13
 */
public class MockitoDependencyInjectionTestExecutionListener extends
        DependencyInjectionTestExecutionListener {

    private Map<String, MockObject> mockObject = new HashMap<String, MockObject>();
    private List<Field> injectFields = new ArrayList<Field>();

    @Override
    protected void injectDependencies(final TestContext testContext) throws Exception {
        super.injectDependencies(testContext);
        init(testContext);
    }

    protected void injectMock(final TestContext testContext, Field field)
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        AutowireCapableBeanFactory beanFactory = testContext.getApplicationContext()
                .getAutowireCapableBeanFactory();
        Object cglibO = beanFactory.getBean(field.getType());
        Object o = this.getCglibProxyTargetObject(cglibO);
        if (null != o) {
            for (Iterator it = mockObject.keySet().iterator(); it.hasNext();) {
                String key = (String) it.next();
                Field f = ReflectionUtils.findField(o.getClass(), key);
                if (f != null) {
                    f.setAccessible(true);
                    f.set(o, mockObject.get(key).getObj());
                }
            }
        }
    }

    private static Object getCglibProxyTargetObject(Object proxy) {
        try {
            Field h = ReflectionUtils.findField(proxy.getClass(), "CGLIB$CALLBACK_0");

            if (h == null) {
                return proxy;
            }

            h.setAccessible(true);
            Object dynamicAdvisedInterceptor = h.get(proxy);

            Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
            advised.setAccessible(true);

            Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor))
                    .getTargetSource().getTarget();

            return target;
        } catch (Exception e) {
            e.printStackTrace();
            return proxy;
        }
    }

    /**
     * 查看测试类中的每一个字段，找到标注有Spy Mock的字段，替换成Mockito之后的对象，并且保存下Mockito之后的对象，将其注入到其他使用到了这个类的对象中.
     * 
     * @param testContext
     * @throws Exception
     */
    private void init(final TestContext testContext) throws Exception {
        Object bean = testContext.getTestInstance(); // 测试类 , ***Test
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation antt : annotations) {
                if (antt instanceof org.mockito.Mock) {
                    // 注入mock实例
                    MockObject obj = new MockObject();
                    obj.setType(field.getType());
                    obj.setObj(Mockito.mock(field.getType()));
                    field.setAccessible(true);
                    field.set(bean, obj.getObj());
                    mockObject.put(field.getName(), obj);
                } else if (antt instanceof org.mockito.Spy) {
                    // 注入mock实例
                    MockObject obj = new MockObject();
                    obj.setType(field.getType());

                    field.setAccessible(true);
                    // 原始对象
                    Object ori = getCglibProxyTargetObject(field.get(bean));
                    obj.setObj(Mockito.spy(ori));
                    field.setAccessible(true);
                    field.set(bean, obj.getObj());
                    mockObject.put(field.getName(), obj);
                } else if (antt instanceof Autowired) {
                    // 只对autowire重新注入
                    injectFields.add(field);
                }
            }
        }
        for (Field field : injectFields) {
            field.setAccessible(true);
            Object object = field.get(bean);
            if (object instanceof Proxy) {
                // 如果是代理的话，找到真正的对象
                Class targetClass = AopUtils.getTargetClass(object);
                if (targetClass == null) {
                    // 可能是远程实现
                    return;
                }
                Field[] targetFields = targetClass.getDeclaredFields();
                for (int i = 0; i < targetFields.length; i++) {
                    // 针对每个需要重新注入的字段
                    for (Map.Entry<String, MockObject> entry : mockObject.entrySet()) {
                        // 针对每个mock的字段
                        if (targetFields[i].getName().equals(entry.getKey())) {
                            targetFields[i].setAccessible(true);
                            targetFields[i].set(
                                    getTargetObject(object, entry.getValue().getType()), entry
                                            .getValue().getObj());
                        }
                    }
                }
            } else {
                injectMock(testContext, field);
            }
        }
    }

    protected <T> T getTargetObject(Object proxy, Class<T> targetClass) throws Exception {
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return (T) ((Advised) proxy).getTargetSource().getTarget();
        } else {
            return (T) proxy; // expected to be cglib proxy then, which is simply a specialized
                              // class
        }
    }

    public static class MockObject {
        private Object obj;
        private Class<?> type;

        public MockObject() {
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public Class<?> getType() {
            return type;
        }

        public void setType(Class<?> type) {
            this.type = type;
        }
    }

}
