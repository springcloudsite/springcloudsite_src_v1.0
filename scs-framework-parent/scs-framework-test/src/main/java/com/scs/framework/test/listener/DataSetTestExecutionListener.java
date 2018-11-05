/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.test.listener;

import static java.lang.String.format;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.DefaultDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.ext.oracle.Oracle10DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.core.Constants;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.scs.framework.test.ITestException;
import com.scs.framework.test.annotation.ITestDataSet;
import com.scs.framework.test.dbunit.dataset.excel.XlsDataSet;
import com.scs.framework.test.dbunit.dataset.excel.XlsTableWrapper;
import com.scs.framework.test.dbunit.operation.DeleteOperation;
import com.scs.framework.test.dbunit.operation.RefreshOperation;
import com.scs.framework.test.util.AnnotationUtil;

/**
 * DataSetTestExecutionListener.
 * 
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月20日
 */
public class DataSetTestExecutionListener extends AbstractTestExecutionListener {
    private static final Constants databaseOperations = new Constants(DatabaseOperation.class);
    private static final String XML = "xml";

    private static final String XLS = "xls";

    private static Pattern pattern = Pattern.compile("\\.");

    protected final Log log = LogFactory.getLog(getClass());
    private final Map<Method, List<DatasetConfig>> datasetConfigCache = Collections
            .synchronizedMap(new IdentityHashMap<Method, List<DatasetConfig>>());

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        List<DatasetConfig> datasetConfigs = getDatasetConfigs(testContext);
        if (datasetConfigs == null || datasetConfigs.size() == 0) {
            return;
        }

        datasetConfigCache.put(testContext.getTestMethod(), datasetConfigs);
        
        

        for (DatasetConfig datasetConfig : datasetConfigs) {
            if (log.isInfoEnabled()) {
                log.info(format(
                        "Loading dataset from class path resource  '%s' using operation '%s' "
                                + "with dataSourceName '%s'.", datasetConfig.getLocation(),
                        datasetConfig.getSetupOperation(), datasetConfig.getDsName()));
            }
            
            // add by yutao: 删除数据的时候可能会有外键冲突的问题
            Connection conn = datasetConfig.getDatabaseTester().getConnection().getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
            
            datasetConfig.getDatabaseTester().onSetup();
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        List<DatasetConfig> datasetConfigs = datasetConfigCache.get(testContext.getTestMethod());
        if (datasetConfigs == null || datasetConfigs.size() == 0) {
            return;
        }

        for (DatasetConfig datasetConfig : datasetConfigs) {
            if (log.isInfoEnabled()) {
                log.info(format("Tearing down dataset using operation '%s'",
                        datasetConfig.getTeardownOperation()));
            }
            
            // add by yutao: 删除数据的时候可能会有外键冲突的问题
            Connection conn = datasetConfig.getDatabaseTester().getConnection().getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");

            // modified by qixiu:put connection close in the finally statement,because "onTearDown"
            // may throw exception
            try {
                datasetConfig.getDatabaseTester().onTearDown();

                clearDb(datasetConfig);

            } catch (Exception e1) {
                throw e1;
            } finally {
                if (!datasetConfig.isTransactional()) {
                    try {
                        datasetConfig.getDatabaseTester().getConnection().getConnection().close();
                    } catch (Exception e) {
                    }
                }
            }
        }

        datasetConfigCache.remove(testContext.getTestMethod());
    }

    /**
     * clearDb.
     */
    private void clearDb(DatasetConfig datasetConfig) {

        try {

            Connection conn = datasetConfig.getDatabaseTester().getConnection().getConnection();
            Statement stmt = conn.createStatement();

            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
            // query all tables
            ResultSet rs = stmt
                    .executeQuery("SELECT TaBLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'");
            List<String> tableNamesInDB = new ArrayList<String>();
            while (rs.next()) {
                tableNamesInDB.add(rs.getString(1));
            }
            rs.close();

            // truncate all tables
            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
            for (String tableName : tableNamesInDB) {
                stmt.execute("TRUNCATE TABLE " + tableName);
//                log.info("tuncate table ： " + tableName);                
            }
//            Thread.currentThread().sleep(60000);// add bu yutao: tomcat连接池的情况下，执行tuncate需要等待完成
            stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
            stmt.close();

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
        }
    }

    /**
     * Supports two methods of multiple data sources can only use one
     * 
     * @param testContext
     * @return
     * @throws Exception
     */
    List<DatasetConfig> getDatasetConfigs(TestContext testContext) throws Exception {
        ITestDataSet annotation = findAnnotation(testContext.getTestInstance().getClass(),
                testContext.getTestMethod());
        if (annotation == null)
            return null;

        String[] locations = determineLocations(testContext, annotation);
        String[] dsNames = determineDsNames(testContext, annotation);
        if (dsNames.length > 1 && locations.length != dsNames.length) {
            String errorMsg = format(
                    "dsNames number '%s' does'nt matchs the locations number '%s'.",
                    dsNames.length, locations.length);
            log.error(errorMsg);
            throw new ITestException(errorMsg);
        }

        List<DatasetConfig> datasetConfigs = Collections
                .synchronizedList(new LinkedList<DatasetConfig>());

        for (int i = 0; i < locations.length; i++) {
            String location = locations[i];
            String fileType = location.substring(location.lastIndexOf(".") + 1);
            String dsName = dsNames.length == 1 ? dsNames[0] : dsNames[i];
            // build dataSet begin
            ReplacementDataSet dataSet;
            if (XLS.equalsIgnoreCase(fileType)) {
                XlsDataSet xlsDataSet = new XlsDataSet(new DefaultResourceLoader().getResource(
                        location).getInputStream());
                // if(annotation.dsNames().length==0){//DataSource name maybe defined in xls sheet
                String[] sheetNames = xlsDataSet.getTableNames();
                for (String sheetName : sheetNames) {
                    String[] temp = pattern.split(sheetName);
                    String tableName = sheetName;
                    if (temp.length == 2) {
                        // add by qixiu, remove sheets that has define the dsnames,use different
                        // datasets
                        sheetNames = (String[]) ArrayUtils.removeElement(sheetNames, sheetName);
                        String dsNameTmp = temp[0];
                        tableName = temp[1];
                        dataSet = new ReplacementDataSet(new DefaultDataSet(new XlsTableWrapper(
                                tableName, xlsDataSet.getTable(sheetName))));
                        buildDataBaseConfig(testContext, annotation, datasetConfigs, location,
                                dsNameTmp, dataSet);
                    }
                }
                // add by qixiu, for normal sheets use one dataset
                int sheetCounts = sheetNames.length;
                ITable[] tables = new ITable[sheetCounts];
                for (int j = 0; j <= sheetCounts - 1; j++) {
                    tables[j] = new XlsTableWrapper(sheetNames[j],
                            xlsDataSet.getTable(sheetNames[j]));
                }
                dataSet = new ReplacementDataSet(new DefaultDataSet(tables));
                buildDataBaseConfig(testContext, annotation, datasetConfigs, location, dsName,
                        dataSet);
                /*
                 * }else{ dataSet = new ReplacementDataSet(xlsDataSet);
                 * buildDataBaseConfig(testContext, annotation, datasetConfigs, location, dsName,
                 * dataSet); }
                 */

            } else if (XML.equalsIgnoreCase(fileType)) {
                dataSet = new ReplacementDataSet(new FlatXmlDataSet(new DefaultResourceLoader()
                        .getResource(location).getInputStream()));
                dataSet.addReplacementObject("[NULL]", null);
                buildDataBaseConfig(testContext, annotation, datasetConfigs, location, dsName,
                        dataSet);
            } else {
                String errorMsg = format("Unsupported file type,file '%s' must be xls or xml.",
                        location);
                log.error(errorMsg);
                throw new ITestException(errorMsg);
            }

            // build dataSet end
        }
        return datasetConfigs;
    }

    private void buildDataBaseConfig(TestContext testContext, ITestDataSet annotation,
                                     List<DatasetConfig> datasetConfigs, String location,
                                     String dsName, ReplacementDataSet dataSet)
        throws DatabaseUnitException, SQLException {
        DataSource dataSource = (DataSource) testContext.getApplicationContext().getBean(dsName);
        Connection connection = DataSourceUtils.getConnection(dataSource);

        // build databaseTester start
        IDatabaseConnection Iconn = getDatabaseConnection(dataSource, connection);
        DatabaseConfig config = Iconn.getConfig();

        String dbType = connection.getMetaData().getDatabaseProductName();
        if ("MySQL".equalsIgnoreCase(dbType)) {
            config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
        } else if ("Oracle".equalsIgnoreCase(dbType)) {
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                    new Oracle10DataTypeFactory());
        }

        Date dbNow = getDbCurrentTime(connection, dbType);
        addSysdateReplacement(dataSet, dbNow);
        addTimeStampReplacement(dataSet, dbNow);

        IDatabaseTester databaseTester = new DefaultDatabaseTester(Iconn);
        databaseTester.setDataSet(dataSet);
        String setUp = annotation.setupOperation();
        DatabaseOperation setUpOperation = "REFRESH".equals(setUp) ? new RefreshOperation()
                : (DatabaseOperation) databaseOperations.asObject(setUp);
        databaseTester.setSetUpOperation(setUpOperation);

        String teardown = annotation.teardownOperation();
        DatabaseOperation teardownOperation = "DELETE".equals(teardown) ? new DeleteOperation()
                : (DatabaseOperation) databaseOperations.asObject(teardown);
        databaseTester.setTearDownOperation(teardownOperation);
        // build databaseTester end

        boolean transactional = DataSourceUtils.isConnectionTransactional(connection, dataSource);
        DatasetConfig datasetConfig = new DatasetConfig(databaseTester, transactional)
                .location(location).dsName(dsName).setupOperation(annotation.setupOperation())
                .teardownOperation(annotation.teardownOperation());

        datasetConfigs.add(datasetConfig);
    }

    private Date getDbCurrentTime(Connection connection, String dbType) throws SQLException {
        Date currentTime = null;
        String sql = null;
        if ("MySQL".equalsIgnoreCase(dbType)) {
            sql = "SELECT now()";
        } else if ("Oracle".equalsIgnoreCase(dbType)) {
            sql = "SELECT sysdate FROM dual";
        }

        if (sql != null) {
            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {

                currentTime = rs.getTimestamp(1);
            }
        } else {
            currentTime = new Date();
        }

        return currentTime;
    }

    private ITestDataSet findAnnotation(Class<?> testClass, Method testMethod) {
        ITestDataSet annotation = (ITestDataSet) AnnotationUtils.findAnnotation(testMethod,
                ITestDataSet.class);
        if (annotation == null) {

            annotation = (ITestDataSet) AnnotationUtil
                    .findAnnotation(testClass, ITestDataSet.class);
        }
        return annotation;
    }

    private String[] determineLocations(TestContext testContext, ITestDataSet annotation) {
        Class<?> testClass = testContext.getTestInstance().getClass();
        String fileType = annotation.fileType();
        String[] value = annotation.value();
        String[] locations = annotation.locations();
        if (!ArrayUtils.isEmpty(value) && !ArrayUtils.isEmpty(locations)) {
            String msg = String
                    .format("Test class [%s] has been configured with @ITestDataSetListener' 'value' [%s] and 'locations' [%s] attributes. Use one or the other, but not both.",
                            testClass, ArrayUtils.toString(value), ArrayUtils.toString(locations));
            throw new ITestException(msg);
        } else if (!ArrayUtils.isEmpty(value)) {
            locations = value;
        }

        if (ArrayUtils.isEmpty(locations)) {// user undefined,using default location
            locations = ResourceLocationProcessingUtil.generateDefaultLocations(testClass, "."
                    + fileType);
        } else {// process to standard resource
            locations = ResourceLocationProcessingUtil.modifyLocations(testClass, locations);
        }
        return locations;
    }

    private String[] determineDsNames(TestContext testContext, ITestDataSet annotation) {
        String[] dsNames = annotation.dsNames();
        return dsNames;
    }

    private DatabaseConnection getDatabaseConnection(DataSource dataSource, Connection connection)
        throws DatabaseUnitException, SQLException {
        String schemaName = getSchemaName(dataSource, connection);
        if (schemaName != null) {
            return new DatabaseConnection(connection, schemaName) {
                public void close() throws SQLException {

                }
            };
        } else {
            return new DatabaseConnection(connection) {
                public void close() throws SQLException {

                }
            };
        }

    }

    private String getSchemaName(DataSource dataSource, Connection connection) throws SQLException {
//        String schemaName = null;
//        if (dataSource instanceof DriverManagerDataSource) {
//            schemaName = "PUBLIC";
//        }
        return "PUBLIC";
    }

    private void addSysdateReplacement(ReplacementDataSet dataSet, Date currentTime) {
        dataSet.addReplacementObject("sysdate", currentTime);
        long day = 3600 * 24 * 1000;
        for (int i = 1; i < 32; i++) {
            dataSet.addReplacementObject("sysdate-" + i, new java.util.Date(currentTime.getTime()
                    - i * day));
            dataSet.addReplacementObject("sysdate+" + i, new java.util.Date(currentTime.getTime()
                    + i * day));
        }
    }

    private void addTimeStampReplacement(ReplacementDataSet dataSet, Date currentTime) {
        dataSet.addReplacementObject("itest-timestamp", currentTime.getTime());
    }

    static class DatasetConfig {
        private IDatabaseTester databaseTester;
        private boolean transactional;

        private String location;
        private String dsName;
        private String setupOperation;
        private String teardownOperation;

        public DatasetConfig location(String location) {
            this.location = location;
            return this;
        }

        public DatasetConfig dsName(String dsName) {
            this.dsName = dsName;
            return this;
        }

        public DatasetConfig setupOperation(String setupOperation) {
            this.setupOperation = setupOperation;
            return this;
        }

        public DatasetConfig teardownOperation(String teardownOperation) {
            this.teardownOperation = teardownOperation;
            return this;
        }

        public DatasetConfig(IDatabaseTester databaseTester, boolean transactional) {
            this.databaseTester = databaseTester;
            this.transactional = transactional;
        }

        public IDatabaseTester getDatabaseTester() {
            return databaseTester;
        }

        public boolean isTransactional() {
            return transactional;
        }

        public String getLocation() {
            return location;
        }

        public String getDsName() {
            return dsName;
        }

        public String getSetupOperation() {
            return setupOperation;
        }

        public String getTeardownOperation() {
            return teardownOperation;
        }

    }

}
