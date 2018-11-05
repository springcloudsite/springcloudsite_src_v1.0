/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * AnnotationUtil.
 * 
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
public class AnnotationUtil {

    private AnnotationUtil() {
    }

    /**
     * 
     * findClassesAnnotationDeclaredWith.
     * 
     * @param clazz clazz
     * @param annotationType annotationType
     * @return object.
     */
    public static List<Class> findClassesAnnotationDeclaredWith(Class<?> clazz,
                                                                Class<? extends Annotation> annotationType) {
        List<Class> classesAnnotationDeclared = new ArrayList<Class>();
        Class<?> classAnnotationDeclared = findClassAnnotationDeclaredWith(clazz, annotationType);
        while (classAnnotationDeclared != null) {
            classesAnnotationDeclared.add(classAnnotationDeclared);
            classAnnotationDeclared = findClassAnnotationDeclaredWith(
                    classAnnotationDeclared.getSuperclass(), annotationType);
        }
        return classesAnnotationDeclared;
    }

    /**
     * 
     * findAnnotation.
     * 
     * @param <A> <A>
     * @param clazz clazz
     * @param annotationType annotationType
     * @return object.
     */
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        A annotation = clazz.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        for (Class<?> ifc : clazz.getInterfaces()) {
            annotation = findAnnotation(ifc, annotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null || superClass == Object.class) {
            return null;
        }
        return findAnnotation(superClass, annotationType);
    }

    /**
     * 
     * findClassAnnotationDeclaredWith.
     * 
     * @param clazz clazz
     * @param annotationType annotationType
     * @return object.
     */
    public static Class<?> findClassAnnotationDeclaredWith(Class<?> clazz,
                                                           Class<? extends Annotation> annotationType) {
        if (clazz == null || clazz.equals(Object.class)) {
            return null;
        }
        return (isAnnotationDeclaredWith(clazz, annotationType)) ? clazz
                : findClassAnnotationDeclaredWith(clazz.getSuperclass(), annotationType);
    }

    /**
     * 
     * isAnnotationDeclaredWith.
     * 
     * @param clazz clazz
     * @param annotationType annotationType
     * @return object.
     */
    public static boolean isAnnotationDeclaredWith(Class<?> clazz,
                                                   Class<? extends Annotation> annotationType) {
        boolean annotationDeclared = false;
        for (Annotation annotation : Arrays.asList(clazz.getDeclaredAnnotations())) {
            if (annotation.annotationType().equals(annotationType)) {
                annotationDeclared = true;
                break;
            }
        }
        return annotationDeclared;
    }

}
