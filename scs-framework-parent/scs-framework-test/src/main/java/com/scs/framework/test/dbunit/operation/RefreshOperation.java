/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test.dbunit.operation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.BitSet;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.IPreparedBatchStatement;
import org.dbunit.database.statement.SimplePreparedStatement;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.NoPrimaryKeyException;
import org.dbunit.dataset.RowOutOfBoundsException;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.operation.InsertOperation;
import org.dbunit.operation.OperationData;
import org.dbunit.operation.UpdateOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This operation literally refreshes dataset contents into the database. This means that data of
 * existing rows is updated and non-existing row get inserted. Any rows which exist in the database
 * but not in dataset stay unaffected.
 * 
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
public class RefreshOperation extends AbstractOperation {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(RefreshOperation.class);

    private final InsertOperation _insertOperation;
    private final UpdateOperation _updateOperation;

    /**
     * 
     * RefreshOperation.
     */
    public RefreshOperation() {
        _insertOperation = (InsertOperation) DatabaseOperation.INSERT;
        _updateOperation = (UpdateOperation) DatabaseOperation.UPDATE;
    }

    private boolean isEmpty(ITable table) throws DataSetException {
        return AbstractBatchOperation.isEmpty(table);
    }

    /**
     * 
     * execute.
     * 
     * @param connection connection
     * @param dataSet dataSet
     * @throws DatabaseUnitException DatabaseUnitException
     * @throws SQLException SQLException
     * @see org.dbunit.operation.DatabaseOperation#execute(org.dbunit.database.IDatabaseConnection,
     *      org.dbunit.dataset.IDataSet)
     */
    public void execute(IDatabaseConnection connection, IDataSet dataSet)
        throws DatabaseUnitException, SQLException {
        log.debug("execute(connection={}, dataSet) - start", connection);

        // for each table
        ITableIterator iterator = dataSet.iterator();
        while (iterator.next()) {
            ITable table = iterator.getTable();

            // Do not process empty table
            if (isEmpty(table)) {
                continue;
            }

            ITableMetaData metaData = getOperationMetaData(connection, table.getTableMetaData());
            RowOperation updateRowOperation = createUpdateOperation(connection, metaData);
            RowOperation insertRowOperation = new InsertRowOperation(connection, metaData);

            try {
                // refresh all rows
                for (int i = 0;; i++) {
                    if (!updateRowOperation.execute(table, i)) {
                        insertRowOperation.execute(table, i);
                    }
                }
            } catch (RowOutOfBoundsException e) {
                // This exception occurs when records are exhausted
                // and we reach the end of the table. Ignore this error.

                // end of table
            } finally {
                // cleanup
                updateRowOperation.close();
                insertRowOperation.close();
            }
        }

    }

    private RowOperation createUpdateOperation(IDatabaseConnection connection,
                                               ITableMetaData metaData) throws DataSetException,
        SQLException {
        log.debug("createUpdateOperation(connection={}, metaData={}) - start", connection, metaData);

        // update only if columns are not all primary keys
        if (metaData.getColumns().length > metaData.getPrimaryKeys().length) {
            return new UpdateRowOperation(connection, metaData);
        }

        // otherwise, operation only verify if row exist
        return new RowExistOperation(connection, metaData);
    }

    /**
     * This class represents a operation executed on a single table row.
     */
    class RowOperation {

        protected IPreparedBatchStatement _statement;
        protected OperationData _operationData;
        protected BitSet _ignoreMapping;

        /**
         * Logger for this class.
         */
        private final Logger logger = LoggerFactory.getLogger(RowOperation.class);

        /**
         * Execute this operation on the sepcified table row.
         * 
         * @return <code>true</code> if operation have been executed on the row.
         */
        public boolean execute(ITable table, int row) throws DataSetException, SQLException {
            logger.debug("execute(table={}, row={}) - start", table, String.valueOf(row));

            Column[] columns = _operationData.getColumns();
            for (int i = 0; i < columns.length; i++) {
                // Bind value only if not in ignore mapping
                if (_ignoreMapping == null || !_ignoreMapping.get(i)) {
                    Object value = table.getValue(row, columns[i].getColumnName());
                    _statement.addValue(value, columns[i].getDataType());
                }
            }
            _statement.addBatch();
            int result = _statement.executeBatch();
            _statement.clearBatch();

            return result == 1;
        }

        /**
         * Cleanup this operation state.
         */
        public void close() throws SQLException {
            logger.debug("close() - start");

            if (_statement != null) {
                _statement.close();
            }
        }
    }

    /**
     * Insert row operation.
     */
    private class InsertRowOperation extends RowOperation {

        /**
         * Logger for this class.
         */
        private final Logger logger = LoggerFactory.getLogger(InsertRowOperation.class);

        private IDatabaseConnection _connection;
        private ITableMetaData _metaData;

        /**
         * 
         * InsertRowOperation.
         * 
         * @param connection connection
         * @param metaData metaData
         * @throws DataSetException DataSetException
         * @throws SQLException object
         */
        public InsertRowOperation(IDatabaseConnection connection, ITableMetaData metaData)
            throws DataSetException, SQLException {
            _connection = connection;
            _metaData = metaData;
        }

        public boolean execute(ITable table, int row) throws DataSetException, SQLException {
            logger.debug("execute(table={}, row={}) - start", table, String.valueOf(row));

            // If current row has a different ignore value mapping than
            // previous one, we generate a new statement
            if (_ignoreMapping == null || !equalsIgnoreMapping(_ignoreMapping, table, row)) {
                // Execute and close previous statement
                if (_statement != null) {
                    _statement.close();
                }

                _ignoreMapping = getIgnoreMapping(table, row);
                _operationData = _insertOperation.getOperationData(_metaData, _ignoreMapping,
                        _connection);
                _statement = new SimplePreparedStatement(_operationData.getSql(),
                        _connection.getConnection());
            }

            return super.execute(table, row);
        }

    }

    /**
     * Update row operation.
     */
    private class UpdateRowOperation extends RowOperation {
        PreparedStatement _countStatement;

        public UpdateRowOperation(IDatabaseConnection connection, ITableMetaData metaData)
            throws DataSetException, SQLException {
            // setup update statement
            _operationData = _updateOperation.getOperationData(metaData, null, connection);
            _statement = new SimplePreparedStatement(_operationData.getSql(),
                    connection.getConnection());
        }
    }

    /**
     * This operation verify if a row exists in the database.
     */
    private class RowExistOperation extends RowOperation {

        /**
         * Logger for this class.
         */
        private final Logger logger = LoggerFactory.getLogger(RowExistOperation.class);

        PreparedStatement _countStatement;

        public RowExistOperation(IDatabaseConnection connection, ITableMetaData metaData)
                throws DataSetException, SQLException {
            // setup select count statement
            _operationData = getSelectCountData(metaData, connection);
            _countStatement = connection.getConnection().prepareStatement(_operationData.getSql());
        }

        private OperationData getSelectCountData(ITableMetaData metaData,
                                                 IDatabaseConnection connection)
            throws DataSetException {
            logger.debug("getSelectCountData(metaData={}, connection={}) - start", metaData,
                    connection);

            Column[] primaryKeys = metaData.getPrimaryKeys();

            // cannot construct where clause if no primary key
            if (primaryKeys.length == 0) {
                throw new NoPrimaryKeyException(metaData.getTableName());
            }

            // select count
            StringBuffer sqlBuffer = new StringBuffer(128);
            sqlBuffer.append("select COUNT(*) from ");
            sqlBuffer.append(getQualifiedName(connection.getSchema(), metaData.getTableName(),
                    connection));

            // where
            sqlBuffer.append(" where ");
            for (int i = 0; i < primaryKeys.length; i++) {
                Column column = primaryKeys[i];

                if (i > 0) {
                    sqlBuffer.append(" and ");
                }
                sqlBuffer.append(getQualifiedName(null, column.getColumnName(), connection));
                sqlBuffer.append(" = ?");
            }

            return new OperationData(sqlBuffer.toString(), primaryKeys);
        }

        // //////////////////////////////////////////////////////////////////////
        // RowOperation class

        /**
         * Verify if the specified table row exists in the database.
         * 
         * @return <code>true</code> if row exists.
         */
        public boolean execute(ITable table, int row) throws DataSetException, SQLException {
            logger.debug("execute(table={}, row={}) - start", table, String.valueOf(row));

            Column[] columns = _operationData.getColumns();
            for (int i = 0; i < columns.length; i++) {
                Object value = table.getValue(row, columns[i].getColumnName());
                DataType dataType = columns[i].getDataType();
                dataType.setSqlValue(value, i + 1, _countStatement);
            }

            ResultSet resultSet = _countStatement.executeQuery();
            try {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            } finally {
                resultSet.close();
            }
        }

        public void close() throws SQLException {
            logger.debug("close() - start");

            _countStatement.close();
        }
    }

    protected boolean equalsIgnoreMapping(BitSet ignoreMapping, ITable table, int row)
        throws DataSetException {
        if (log.isDebugEnabled()) {
            log.debug("equalsIgnoreMapping(ignoreMapping={}, table={}, row={}) - start",
                    new Object[] { ignoreMapping, table, String.valueOf(row) });
        }

        Column[] columns = table.getTableMetaData().getColumns();

        for (int i = 0; i < columns.length; i++) {
            boolean bit = ignoreMapping.get(i);
            Object value = table.getValue(row, columns[i].getColumnName());
            if ((bit && value != ITable.NO_VALUE) || (!bit && value == ITable.NO_VALUE)) {
                return false;
            }
        }

        return true;
    }

    protected BitSet getIgnoreMapping(ITable table, int row) throws DataSetException {
        if (log.isDebugEnabled())
            log.debug("getIgnoreMapping(table={}, row={}) - start", table, String.valueOf(row));

        Column[] columns = table.getTableMetaData().getColumns();

        BitSet ignoreMapping = new BitSet();
        for (int i = 0; i < columns.length; i++) {
            Object value = table.getValue(row, columns[i].getColumnName());
            if (value == ITable.NO_VALUE) {
                ignoreMapping.set(i);
            }
        }
        return ignoreMapping;
    }

}
