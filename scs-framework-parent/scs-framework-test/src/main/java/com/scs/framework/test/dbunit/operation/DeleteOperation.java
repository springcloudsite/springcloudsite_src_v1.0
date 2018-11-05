/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test.dbunit.operation;

import java.util.BitSet;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.NoPrimaryKeyException;
import org.dbunit.operation.OperationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Deletes only the dataset contents from the database. This operation does not delete the entire
 * table contents but only data that are present in the dataset.
 * 
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
public class DeleteOperation extends AbstractBatchOperation {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(DeleteOperation.class);

    /**
     * 
     * DeleteOperation.
     */
    public DeleteOperation() {
        _reverseRowOrder = true;
    }

    /**
     * 
     * iterator.
     * 
     * @param dataSet dataSet
     * @return ITableIterator
     * @throws DatabaseUnitException DatabaseUnitException
     * @see araf.core.test.dbunit.operation.AbstractBatchOperation#iterator(org.dbunit.dataset.IDataSet)
     */
    protected ITableIterator iterator(IDataSet dataSet) throws DatabaseUnitException {
        log.debug("iterator(dataSet={}) - start", dataSet);
        return dataSet.reverseIterator();
    }

    /**
     * 
     * getOperationData.
     * 
     * @param metaData metaData
     * @param ignoreMapping ignoreMapping
     * @param connection connection
     * @return OperationData
     * @throws DataSetException DataSetException
     * @see araf.core.test.dbunit.operation.AbstractBatchOperation#getOperationData(org.dbunit.dataset.ITableMetaData,
     *      java.util.BitSet, org.dbunit.database.IDatabaseConnection)
     */
    public OperationData getOperationData(ITableMetaData metaData, BitSet ignoreMapping,
                                          IDatabaseConnection connection) throws DataSetException {
        if (log.isDebugEnabled()) {
            log.debug("getOperationData(metaData={}, ignoreMapping={}, connection={}) - start",
                    new Object[] { metaData, ignoreMapping, connection });
        }

        // cannot construct where clause if no primary key
        Column[] primaryKeys = metaData.getPrimaryKeys();
        if (primaryKeys.length == 0) {
            throw new NoPrimaryKeyException(metaData.getTableName());
        }

        // delete from
        StringBuffer sqlBuffer = new StringBuffer(128);
        sqlBuffer.append("delete from ");
        sqlBuffer.append(getQualifiedName(connection.getSchema(), metaData.getTableName(),
                connection));

        // where
        sqlBuffer.append(" where ");
        for (int i = 0; i < primaryKeys.length; i++) {
            // escape column name
            String columnName = getQualifiedName(null, primaryKeys[i].getColumnName(), connection);
            sqlBuffer.append(columnName);

            sqlBuffer.append(" = ?");
            if (i + 1 < primaryKeys.length) {
                sqlBuffer.append(" and ");
            }
        }

        return new OperationData(sqlBuffer.toString(), primaryKeys);
    }

}
