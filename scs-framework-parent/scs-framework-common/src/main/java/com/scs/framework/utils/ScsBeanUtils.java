/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.StringUtils;

/**
 * 
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class ScsBeanUtils extends BeanUtils {
    /**
     * 功能 : 只复制source对象的非空属性到target对象上
     */
    public static void copyNoNullProperties(Object source, Object target) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(),
                        targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        // 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
                        if (value != null) {

                            // 判断对象的属性，如果是Collection，且为空，则不拷贝。值不为空，才会拷贝
                            Class<?> clazz = sourcePd.getPropertyType();
                            if (Collection.class.isAssignableFrom(clazz)
                                    && ((Collection) value).isEmpty()) {
                                continue;

                            }

                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier
                                    .isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);

                        }
                    } catch (Exception ex) {
                        throw new FatalBeanException(
                                "Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }

    /**
     * 功能 : 复制source对象的到target对象上, source中的Collection类型，只有不为空的时候才会赋值.
     */
    public static void copyPropertiesExceptEmpytList(Object source, Object target) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(),
                        targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        // 判断对象的属性，如果是Collection，则需要判断值不为空，才会拷贝
                        Class<?> clazz = sourcePd.getPropertyType();
                        if (Collection.class.isAssignableFrom(clazz)
                                && (StringUtils.isEmpty(value) || ((Collection) value).isEmpty())) {
                            continue;

                        }
                        // 直接赋值
                        Method writeMethod = targetPd.getWriteMethod();
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(target, value);

                    } catch (Exception ex) {
                        throw new FatalBeanException(
                                "Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }
}
