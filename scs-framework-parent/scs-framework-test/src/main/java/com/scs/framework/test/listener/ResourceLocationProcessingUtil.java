/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test.listener;

import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

/**
 * 
 * ResourceLocationProcessingUtil.
 * 
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
public class ResourceLocationProcessingUtil {

    private ResourceLocationProcessingUtil() {
    }

    /**
     * 
     * generateDefaultLocations.
     * 
     * @param clazz clazz
     * @param suffix suffix
     * @return object.
     */
    public static String[] generateDefaultLocations(Class<?> clazz, String suffix) {
        return new String[] { ResourceUtils.CLASSPATH_URL_PREFIX + "/"
                + clazz.getName().replace('.', '/') + suffix };
    }

    /**
     * 
     * modifyLocations.
     * 
     * @param clazz clazz
     * @param locations locations
     * @return object.
     */
    public static String[] modifyLocations(Class<?> clazz, String... locations) {
        String[] modifiedLocations = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            String path = locations[i];
            if (path.startsWith("/")) {
                modifiedLocations[i] = ResourceUtils.CLASSPATH_URL_PREFIX + path;
            } else if (!ResourcePatternUtils.isUrl(path)) {
                modifiedLocations[i] = ResourceUtils.CLASSPATH_URL_PREFIX
                        + "/"
                        + StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(clazz) + "/"
                                + path);
            } else {
                modifiedLocations[i] = StringUtils.cleanPath(path);
            }
        }
        return modifiedLocations;
    }
}
