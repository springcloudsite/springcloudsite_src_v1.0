/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test.dbunit;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.ext.oracle.Oracle10DataTypeFactory;

import com.scs.framework.test.ITestException;

/**
 * 
 * DbUnitTools.
 * 
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
public class DbUnitTools {
    private static final Log log = LogFactory.getLog(DbUnitTools.class);

    private DbUnitTools() {
    }

    /**
     * 
     * generateDataSetFile.
     * 
     * @param destFilePath destFilePath
     * @param dataSource dataSource
     * @param querySqlsSplitBySemicolon object.
     */
    public static void generateDataSetFile(String destFilePath, DataSource dataSource,
                                           String querySqlsSplitBySemicolon) {
        String[] querySqls = querySqlsSplitBySemicolon.split(";");
        File destFile = new File(destFilePath);
        DatabaseConnection databaseConnection = null;
        try {
            Connection connection = dataSource.getConnection();
            databaseConnection = getDatabaseConnection(dataSource, connection);
            DatabaseConfig config = databaseConnection.getConfig();
            if ("MySQL".equalsIgnoreCase(connection.getMetaData().getDatabaseProductName())) {
                config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER,
                        new MySqlMetadataHandler());
            } else if ("Oracle"
                    .equalsIgnoreCase(connection.getMetaData().getDatabaseProductName())) {
                config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                        new Oracle10DataTypeFactory());
            }
            QueryDataSet queryDataSet = new QueryDataSet(databaseConnection);
            for (int i = 0; i < querySqls.length; i++) {
                queryDataSet.addTable(getTableNameFromQuerySql(querySqls[i]), querySqls[i]);
            }
            String destFileName = destFile.getName();
            if (destFileName.endsWith(".xls")) {
                XlsDataSet.write(queryDataSet, new FileOutputStream(destFile));
            } else if (destFileName.endsWith(".xml")) {
                FlatXmlDataSet.write(queryDataSet, new FileOutputStream(destFile));
            } else {
                throw new ITestException("destFile shoud be a xls or xml file!");
            }
            log.info("Dbunit Write Data To : " + destFile.getAbsolutePath());
        } catch (Exception e) {
            log.error("DbunitWriteDataToExcel failed");
            e.printStackTrace();
            throw new IllegalStateException("DbunitWriteDataToFile failed", e);
        } finally {
            try {
                if (databaseConnection != null) {
                    databaseConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private static String getTableNameFromQuerySql(String querySql) {
        String tableName = null;
        String[] array = querySql.split(" ");
        for (int i = 0; i < array.length; i++) {
            if ("From".equalsIgnoreCase(array[i].trim())) {
                tableName = array[i + 1].trim();
                break;
            }
        }
        return tableName;

    }

    private static DatabaseConnection getDatabaseConnection(DataSource dataSource,
                                                            Connection connection)
        throws DatabaseUnitException, SQLException {
        String schemaName = getSchemaName(dataSource, connection);
        if (schemaName != null) {
            return new DatabaseConnection(connection, schemaName);
        } else {
            return new DatabaseConnection(connection);
        }

    }

    private static String getSchemaName(DataSource dataSource, Connection connection)
        throws SQLException {
        String schemaName = null;

        if (schemaName == null) {
            log.warn("No schemaName is specified, use username as shcemaName defaultly");
            schemaName = connection.getMetaData().getUserName();
        }
        return schemaName;
    }

}
