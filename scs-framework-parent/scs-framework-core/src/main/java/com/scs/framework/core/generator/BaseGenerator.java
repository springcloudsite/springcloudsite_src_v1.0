/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * BaseGenerator.
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public abstract class BaseGenerator {

    protected static final String JAVA_SUFFIX = ".java";
    protected static final String XML_SUFFIX = ".xml";
    protected GenConfig genConfig;
    protected List<GenParam> paramList;
    protected Database database;
    protected boolean fileOvervide;

    BaseGenerator() {
        this.fileOvervide = false;
    }

    protected abstract void run(Table paramTable, String paramString) throws Exception;

    public void setGenConfig(GenConfig genConfig) {
        this.genConfig = genConfig;
    }

    public void setParamList(List<GenParam> paramList) {
        this.paramList = paramList;
    }

    protected static String getPathFromPackageName(String packageName) {
        if (StringUtils.isEmpty(packageName))
            return "";

        return packageName.replace(".", File.separator);
    }

    protected static String getFilePath(String savePath, String segment) {
        File folder = new File(savePath, segment);
        if (!(folder.exists()))
            folder.mkdirs();

        return folder.getPath();
    }

    protected boolean containsGenType(GenType genType) {
        GenType[] arrayOfGenType = this.genConfig.getGenTypes();
        int i = arrayOfGenType.length;
        for (int j = 0; j < i; ++j) {
            GenType gen = arrayOfGenType[j];
            if (gen == genType)
                return true;
        }

        return false;
    }

    public void generate() {
        if ((this.genConfig.getSaveDirForVo() == null)
                || (this.genConfig.getSaveDirForVo().trim().length() == 0)) {
            this.genConfig.setSaveDirForVo(this.genConfig.getSaveDir());
        }

        if ((this.genConfig.getSaveDirForXml() == null)
                || (this.genConfig.getSaveDirForXml().trim().length() == 0))
            this.genConfig.setSaveDirForXml(
                    new File(this.genConfig.getSaveDir(), "../resources/mapper").getAbsolutePath());

        if (((containsGenType(GenType.VO)) && (this.genConfig.getSaveDirForVo() == null))
                || (this.genConfig.getSaveDirForVo().trim().length() == 0))
            throw new IllegalArgumentException("生成VO时需要设置SaveDirForVo参数");

        if (((containsGenType(GenType.MAPPER_XML)) && (this.genConfig.getSaveDirForXml() == null))
                || (this.genConfig.getSaveDirForXml().trim().length() == 0))
            throw new IllegalArgumentException("生成Mapper XML时需要设置SaveDirForXml参数");

        if (((containsGenType(GenType.BASE_MAPPER_XML))
                && (this.genConfig.getSaveDirForXml() == null))
                || (this.genConfig.getSaveDirForXml().trim().length() == 0))
            throw new IllegalArgumentException("生成Base Mapper XML时需要设置SaveDirForXml参数");

        try {
            Class.forName(this.genConfig.getDbDriverName());
            Properties props = new Properties();
            props.setProperty("user", this.genConfig.getDbUser());
            props.setProperty("password", this.genConfig.getDbPassword());
            props.setProperty("remarks", "true");
            props.setProperty("useInformationSchema", "true");

            Connection conn = DriverManager.getConnection(this.genConfig.getDbUrl(), props);

            DatabaseUtils databaseUtils = DatabaseUtils.getInstance(conn,
                    this.genConfig.getDbSchema());
            this.database = databaseUtils.database;

            Map tableNamesMap = databaseUtils.getAllTableNamesMap();
            if (tableNamesMap.size() == 0) {
                return;
            }

            for (Iterator localIterator = this.paramList.iterator(); localIterator.hasNext();) {
                GenParam genParam = (GenParam) localIterator.next();
                String[] tableNames = genParam.getTables();
                for (int i = 0; i < tableNames.length; ++i) {
                    String tableName = tableNames[i].toLowerCase();
                    if (!(tableNamesMap.containsKey(tableName))) {
                        System.err.println(new StringBuilder().append("Can't find table ")
                                .append(tableName).toString());
                    } else {
                        Table table = databaseUtils
                                .getTableInfo((String) tableNamesMap.get(tableName));
                        run(table, genParam.getModule());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean validFile(String dirPath, String beanName, String suffix) {
        File file = new File(dirPath,
                new StringBuilder().append(beanName).append(suffix).toString());
        return ((!(file.exists())) || (this.fileOvervide));
    }

    protected String getBeanName(String table, boolean includePrefix) {
        StringBuilder sb = new StringBuilder();
        if (table.contains("_")) {
            String[] tables = table.split("_");
            int l = tables.length;
            int s = 0;
            if (includePrefix)
                s = 1;

            for (int i = s; i < l; ++i) {
                String temp = tables[i].trim();
                sb.append(temp.substring(0, 1).toUpperCase())
                        .append(temp.substring(1).toLowerCase());
            }
        } else {
            sb.append(table.substring(0, 1).toUpperCase()).append(table.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    protected String processType(String type) {
        if (this.database.getProductName().toLowerCase().contains("oracle"))
            return oracleProcessType(type);
        else if (this.database.getProductName().toLowerCase().contains("postgresql"))
            return postgresqlProcessType(type);

        return mysqlProcessType(type);
    }

    protected String mysqlProcessType(String type) {
        String t = type.toLowerCase();
        if (t.contains("char"))
            return "String";
        if (t.contains("bigint"))
            return "Long";
        if (t.contains("int"))
            return "Integer";
        if ((t.contains("date")) || (t.contains("timestamp")))
            return "Date";
        if (t.contains("text"))
            return "String";
        if (t.contains("binary(1)"))
            return "Boolean";
        if (t.contains("bit"))
            return "Boolean";
        if (t.contains("numeric"))
            return "BigDecimal";
        if (t.contains("decimal"))
            return "BigDecimal";
        if (t.contains("blob"))
            return "byte[]";
        if (t.contains("float"))
            return "Float";
        if (t.contains("double"))
            return "Double";

        System.err.println(
                new StringBuilder().append("unkown type [").append(type).append("]").toString());
        return null;
    }
    
    protected String postgresqlProcessType(String type) {
        String t = type.toLowerCase();
        if (t.contains("char"))
            return "String";
        if (t.contains("bigint"))
            return "Long";
        if (t.contains("int"))
            return "Integer";
        if ((t.contains("date")) || (t.contains("timestamp")))
            return "Date";
        if (t.contains("text"))
            return "String";
        if (t.contains("binary(1)"))
            return "Boolean";
        if (t.contains("bit"))
            return "Boolean";
        if (t.contains("numeric"))
            return "BigDecimal";
        if (t.contains("decimal"))
            return "BigDecimal";
        if (t.contains("blob"))
            return "byte[]";
        if (t.contains("float"))
            return "Float";
        if (t.contains("double"))
            return "Double";
        
        System.err.println(
                new StringBuilder().append("unkown type [").append(type).append("]").toString());
        return null;
    }

    protected String oracleProcessType(String type) {
        String t = type.toUpperCase();
        if (t.contains("CHAR"))
            return "String";
        if ((t.contains("DATE")) || (t.contains("TIMESTAMP")))
            return "Date";
        if (t.contains("NUMBER"))
            return "Double";
        if (t.contains("FLOAT"))
            return "Float";
        if (t.contains("BLOB"))
            return "Object";
        if (t.contains("RAW"))
            return "byte[]";

        System.err.println(
                new StringBuilder().append("unkown type [").append(type).append("]").toString());
        return null;
    }

    protected boolean isDate(List<String> types) {
        for (Iterator localIterator = types.iterator(); localIterator.hasNext();) {
            String type = (String) localIterator.next();
            String t = type.toLowerCase();
            if ((t.contains("date")) || (t.contains("timestamp")))
                return true;
        }

        return false;
    }

    protected boolean isDecimal(List<String> types) {
        for (Iterator localIterator = types.iterator(); localIterator.hasNext();) {
            String type = (String) localIterator.next();
            if (type.toLowerCase().contains("decimal"))
                return true;
        }

        return false;
    }

    protected String processField(String field) {
        StringBuilder sb = new StringBuilder();
        String[] fields = field.split("_");
        sb.append(fields[0].toLowerCase());
        for (int i = 1; i < fields.length; ++i) {
            String temp = fields[i];
            sb.append(temp.substring(0, 1).toUpperCase());
            sb.append(temp.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    protected BufferedWriter buildClassComment(BufferedWriter bw, String prefix, String text)
        throws IOException {
        bw.newLine();
        bw.write("/**");
        bw.newLine();
        bw.write(" *");
        bw.newLine();
        if ((prefix != null) && (prefix.trim().length() > 0)) {
            bw.write(new StringBuilder().append(" * ").append(prefix).toString());
            bw.newLine();
        }
        bw.write(new StringBuilder().append(" * ").append(text).toString());
        bw.newLine();
        bw.write(" *");
        bw.newLine();
        bw.write(" */");
        return bw;
    }

    protected void openDir() {
        String osName;
        try {
            osName = System.getProperty("os.name");
            if (osName != null)
                if (osName.contains("Mac"))
                    Runtime.getRuntime().exec(new StringBuilder().append("open ")
                            .append(this.genConfig.getSaveDir()).toString());
                else if (osName.contains("Windows"))
                    Runtime.getRuntime().exec(new StringBuilder().append("cmd /c start ")
                            .append(this.genConfig.getSaveDir()).toString());
                else
                    System.err.println(new StringBuilder().append("save dir:")
                            .append(this.genConfig.getSaveDir()).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
