/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation test data.s
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ITestDataSet {
    /**
     * Alias for {@link #locations() locations}.
     */
    String[] value() default {};

    /**
     * locations.
     */
    String[] locations() default {};

    /**
     * dsNames.
     */
    String[] dsNames() default {};

    /**
     * setupOperation.
     */
    String setupOperation() default "REFRESH";

    /**
     * teardownOperation.
     */
    String teardownOperation() default "DELETE";

    /**
     * fileType.
     */
    String fileType() default "xls";
}
