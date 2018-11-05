/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class DatabaseUtils {

    private Connection conn = null;
    private String schema;
    Database database = null;

    public static DatabaseUtils getInstance(Connection conn, String schema) {
        DatabaseUtils obj = new DatabaseUtils();
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            Database database = new Database();
            database.setProductName(metaData.getDatabaseProductName());
            database.setProductVersion(metaData.getDatabaseProductVersion());
            obj.conn = conn;
            obj.database = database;
            obj.schema = schema;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public Map<String, String> getAllTableNamesMap() {
        ResultSet rs = null;
        Map result = new HashMap();
        try {
            DatabaseMetaData metaData = this.conn.getMetaData();
            rs = metaData.getTables(null, this.schema, null, new String[] { "TABLE" });
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                result.put(tableName.toLowerCase(), tableName);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isMySQL() {
        return (this.database.getProductName().toLowerCase().contains("mysql"));
    }

    public Table getTableInfo(String tableName) {
        Table table = new Table();
        table.setName(tableName);

        ResultSet rs = null;
        try {
            DatabaseMetaData metaData = this.conn.getMetaData();
            rs = metaData.getTables(null, this.schema, tableName, new String[] { "TABLE" });
            if (rs.next())
                table.setComment(rs.getString("REMARKS"));

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setColumns(getTableColumns(tableName));
        table.setPrimaryKeys(getTablePrimaryKeys(tableName));
        return table;
    }

    private List<Column> getTableColumns(String tableName) {
        if (isMySQL())
            return getTableColumnsByMySQL(tableName);

        return getTableColumnsByMetadata(tableName);
    }

    private List<Column> getTableColumnsByMySQL(String tableName) {
        List columns = new ArrayList();

        ResultSet rs = null;
        try {
            String tableFieldsSql = String.format("show full fields from %s",
                    new Object[] { tableName });
            rs = this.conn.prepareStatement(tableFieldsSql).executeQuery();

            while (rs.next()) {
                Column column = new Column();
                column.setName(rs.getString("Field"));
                column.setType(rs.getString("Type"));
                column.setSize(-1);
                column.setComment(rs.getString("Comment"));
                column.setNullable(rs.getBoolean("Null"));
                column.setDefaultValue(rs.getString("Default"));
                columns.add(column);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columns;
    }

    private List<Column> getTableColumnsByMetadata(String tableName) {
        List columns = new ArrayList();

        ResultSet rs = null;
        try {
            DatabaseMetaData metaData = this.conn.getMetaData();
            rs = metaData.getColumns(null, this.schema, tableName, null);
            while (rs.next()) {
                Column column = new Column();
                column.setName(rs.getString("COLUMN_NAME"));
                column.setType(rs.getString("TYPE_NAME"));
                column.setSize(rs.getInt("COLUMN_SIZE"));
                column.setComment(rs.getString("REMARKS"));
                column.setNullable(rs.getBoolean("NULLABLE"));
                column.setDefaultValue(rs.getString("COLUMN_DEF"));
                columns.add(column);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columns;
    }

    private List<PrimaryKey> getTablePrimaryKeys(String tableName) {
        List primaryKeys = new ArrayList();

        ResultSet rs = null;
        try {
            DatabaseMetaData metaData = this.conn.getMetaData();
            rs = metaData.getPrimaryKeys(null, this.schema, tableName);
            while (rs.next()) {
                PrimaryKey obj = new PrimaryKey();
                obj.setColumnName(rs.getString("COLUMN_NAME"));
                obj.setKeySeq(rs.getInt("KEY_SEQ"));
                obj.setPkName(rs.getString("PK_NAME"));
                primaryKeys.add(obj);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return primaryKeys;
    }
}
