/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.generator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Column.
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class Column {

    private String name;
    private String type;
    private int size;
    private String defaultValue;
    private String comment;
    private boolean nullable;

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public int getSize() {
        return this.size;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String getComment() {
        return this.comment;
    }

    public boolean isNullable() {
        return this.nullable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    protected boolean canEqual(Object other) {
        return other instanceof Column;
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String toString() {
        return "Column(name=" + getName() + ", type=" + getType() + ", size=" + getSize()
                + ", defaultValue=" + getDefaultValue() + ", comment=" + getComment()
                + ", nullable=" + isNullable() + ")";
    }
}
