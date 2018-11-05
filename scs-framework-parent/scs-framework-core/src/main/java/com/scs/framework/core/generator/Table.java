/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.generator;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Table.
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class Table {

    private String name;
    private String comment;
    private List<Column> columns;
    private List<PrimaryKey> primaryKeys;
    

    public Table() {
        super();
    }

    public String getName() {
        return this.name;
    }

    public String getComment() {
        return this.comment;
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public List<PrimaryKey> getPrimaryKeys() {
        return this.primaryKeys;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void setPrimaryKeys(List<PrimaryKey> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    protected boolean canEqual(Object other) {
        return other instanceof Table;
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String toString() {
        return "Table(name=" + getName() + ", comment=" + getComment() + ", columns=" + getColumns()
                + ", primaryKeys=" + getPrimaryKeys() + ")";
    }
}
