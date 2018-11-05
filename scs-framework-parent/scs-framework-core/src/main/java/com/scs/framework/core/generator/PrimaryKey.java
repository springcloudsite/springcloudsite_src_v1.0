/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.generator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * PrimaryKey.
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class PrimaryKey {

    private String pkName;
    private int keySeq;
    private String columnName;

    public String getPkName() {
        return this.pkName;
    }

    public int getKeySeq() {
        return this.keySeq;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public void setKeySeq(int keySeq) {
        this.keySeq = keySeq;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    protected boolean canEqual(Object other) {
        return other instanceof PrimaryKey;
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String toString() {
        return "PrimaryKey(pkName=" + getPkName() + ", keySeq=" + getKeySeq() + ", columnName="
                + getColumnName() + ")";
    }
}
