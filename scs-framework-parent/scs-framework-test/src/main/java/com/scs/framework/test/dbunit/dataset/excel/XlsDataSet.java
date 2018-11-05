/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test.dbunit.dataset.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dbunit.dataset.AbstractDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTableIterator;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.OrderedTableNameMap;
import org.dbunit.dataset.excel.XlsDataSetWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Helpless copy the code, because the class must be rewritten XlsTable, but the class can not
 * inherit another (@ link XlsDataSet) can not inherit the basic.
 * 
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
public class XlsDataSet extends AbstractDataSet {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(XlsDataSet.class);

    private final OrderedTableNameMap orderedTableNameMap;

    /**
     * Creates a new XlsDataSet object that loads the specified Excel document.
     * 
     * @param file file
     * @throws IOException IOException
     * @throws DataSetException object
     */
    public XlsDataSet(File file) throws IOException, DataSetException {
        this(new FileInputStream(file));
    }

    /**
     * 
     * Creates a new XlsDataSet object that loads the specified Excel document.
     * 
     * @param in in
     * @throws IOException IOException
     * @throws DataSetException object
     */
    public XlsDataSet(InputStream in) throws IOException, DataSetException {
        orderedTableNameMap = super.createTableNameMap();

        HSSFWorkbook workbook = new HSSFWorkbook(in);
        int sheetCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            ITable table = new XlsTable(workbook, workbook.getSheetName(i), workbook.getSheetAt(i));
            orderedTableNameMap.add(table.getTableMetaData().getTableName(), table);
        }
    }

    /**
     * 
     * Creates a new XlsDataSet object with the specified workbook.
     * 
     * @param workbook workbook
     * @throws IOException IOException
     * @throws DataSetException object
     */
    public XlsDataSet(HSSFWorkbook workbook) throws IOException, DataSetException {
        orderedTableNameMap = super.createTableNameMap();

        int sheetCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            ITable table = new XlsTable(workbook, workbook.getSheetName(i), workbook.getSheetAt(i));
            orderedTableNameMap.add(table.getTableMetaData().getTableName(), table);
        }
    }

    /**
     * Write the specified dataset to the specified Excel document.
     * 
     * @param dataSet dataSet
     * @param out out
     * @throws IOException IOException
     * @throws DataSetException object.
     */
    public static void write(IDataSet dataSet, OutputStream out) throws IOException,
        DataSetException {
        log.debug("write(dataSet={}, out={}) - start", dataSet, out);

        new XlsDataSetWriter().write(dataSet, out);
    }

    // //////////////////////////////////////////////////////////////////////////
    // AbstractDataSet class

    @SuppressWarnings("unchecked")
    @Override
    protected ITableIterator createIterator(boolean reversed) throws DataSetException {
        if (log.isDebugEnabled()) {
            log.debug("createIterator(reversed={}) - start", String.valueOf(reversed));
        }
        ITable[] tables = (ITable[]) orderedTableNameMap.orderedValues().toArray(new ITable[0]);
        return new DefaultTableIterator(tables, reversed);
    }
}
