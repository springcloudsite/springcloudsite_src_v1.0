/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test.dbunit.dataset.excel;

import org.dbunit.dataset.AbstractTable;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.springframework.util.StringUtils;
import org.unitils.core.UnitilsException;

/**
 * 
 * After the sheet name of the datasource in order to obtain the data table name.
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
public class XlsTableWrapper extends AbstractTable {
    private ITable delegate;
    private String tableName;

    public XlsTableWrapper(String tableName, ITable table) {
        this.delegate = table;
        this.tableName = tableName;
    }
    public int getRowCount() {
        return delegate.getRowCount();
    }

    public ITableMetaData getTableMetaData() {
        ITableMetaData meta = delegate.getTableMetaData();
        try {
            return new DefaultTableMetaData(tableName, meta.getColumns(), meta.getPrimaryKeys());
        } catch (DataSetException e) {
            throw new UnitilsException("Don't get the meta info from  " + meta, e);
        }
    }

    public Object getValue(int row, String column) throws DataSetException {
        Object delta = delegate.getValue(row, column);
        if (delta instanceof String) {
            if (StringUtils.isEmpty((String) delta)) {
                return null;
            }
        }
        return delta;
    }

}
