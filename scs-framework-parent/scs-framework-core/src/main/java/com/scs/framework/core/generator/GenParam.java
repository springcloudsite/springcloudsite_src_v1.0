/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.generator;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * GenParam.
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class GenParam {

    private String module;
    private String[] tables;

    public GenParam(String module, String[] tables) {
        this.tables = tables;
        this.module = module;
    }

    public String getModule() {
        return this.module;
    }

    public String[] getTables() {
        return this.tables;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setTables(String[] tables) {
        this.tables = tables;
    }

    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    protected boolean canEqual(Object other) {
        return other instanceof GenParam;
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String toString() {
        return "GenParam(module=" + getModule() + ", tables=" + Arrays.deepToString(getTables())
                + ")";
    }
}
