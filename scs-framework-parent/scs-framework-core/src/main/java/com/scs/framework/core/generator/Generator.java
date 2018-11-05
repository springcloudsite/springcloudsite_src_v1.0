/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Generator.
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class Generator extends BaseGenerator {

    private GenFileInfo voInfo;
    private GenFileInfo poInfo;
    private GenFileInfo daoInfo;
    private GenFileInfo serviceInfo;
    private GenFileInfo baseMapperXmlInfo;
    private GenFileInfo mapperXmlInfo;

    private String assemblePackage(String module, String catalog) {
        String result = this.genConfig.getBasePackage() + "." + module;
        if ((catalog != null) && (catalog.trim().length() > 0))
            result = result + "." + catalog;

        return result;
    }

    private String assembleXmlPackage(String module) {
        String result = "";
        if ((module != null) && (module.trim().length() > 0))
            result = module;
        else
            result = "misc";

        return result;
    }

    private void resetFileInfo(String beanName, String module) {
        String saveDir = this.genConfig.getSaveDir();

        String name = beanName + "VO";
        String packageName = assemblePackage(module, "vo");
        String path = getFilePath(this.genConfig.getSaveDirForVo(),
                getPathFromPackageName(packageName));
        this.voInfo = new GenFileInfo(name, packageName, path);

        name = beanName;
        packageName = assemblePackage(module, "domainmodel");
        path = getFilePath(saveDir, getPathFromPackageName(packageName));
        this.poInfo = new GenFileInfo(name, packageName, path);

        name = beanName + "Dao";
        packageName = assemblePackage(module, "dao");
        path = getFilePath(saveDir, getPathFromPackageName(packageName));
        this.daoInfo = new GenFileInfo(name, packageName, path);

        name = beanName + "Service";
        packageName = assemblePackage(module, "service");
        path = getFilePath(saveDir, getPathFromPackageName(packageName));
        this.serviceInfo = new GenFileInfo(name, packageName, path);

        name = beanName + "BaseDao";
        packageName = assembleXmlPackage(module);
        String xmlPath = getFilePath(this.genConfig.getSaveDirForXml(), "base");
        path = getFilePath(xmlPath, getPathFromPackageName(packageName));
        this.baseMapperXmlInfo = new GenFileInfo(name, packageName, path);

        name = beanName + "Dao";
        packageName = assembleXmlPackage(module);
        xmlPath = getFilePath(this.genConfig.getSaveDirForXml(), "custom");
        path = getFilePath(xmlPath, getPathFromPackageName(packageName));
        this.mapperXmlInfo = new GenFileInfo(name, packageName, path);
    }

    protected void run(Table table, String module) throws Exception {
        System.out.println("============处理表" + table.getName() + "==================");
        if (table.getPrimaryKeys().size() == 0) {
            System.out.println("表" + table.getName() + "没有主键字段，忽略生成，请手工编写.");
            return;
        }
        if (table.getPrimaryKeys().size() > 1) {
            System.out.println("表" + table.getName() + "为联合主键，忽略生成,请手工编写.");
            return;
        }
        String beanName = getBeanName(table.getName(), false);
        resetFileInfo(beanName, module);

        this.fileOvervide = false;
        if ((containsGenType(GenType.VO))
                && (validFile(this.voInfo.getPath(), this.voInfo.getName(), ".java")))
            buildVo(table);

        this.fileOvervide = true;
        if ((containsGenType(GenType.PO))
                && (validFile(this.poInfo.getPath(), this.poInfo.getName(), ".java"))) {
            buildPo(table);
        }

        this.fileOvervide = false;
        if ((containsGenType(GenType.DAO))
                && (validFile(this.daoInfo.getPath(), this.daoInfo.getName(), ".java")))
            buildDao(table);

        this.fileOvervide = false;
        if ((containsGenType(GenType.SERVICE))
                && (validFile(this.serviceInfo.getPath(), this.serviceInfo.getName(), ".java")))
            buildService(table);

        this.fileOvervide = true;
        if ((containsGenType(GenType.BASE_MAPPER_XML))
                && (validFile(this.baseMapperXmlInfo.getPath(), this.baseMapperXmlInfo.getName(),
                        ".xml")))
            buildBaseXml(table);

        this.fileOvervide = false;
        if ((containsGenType(GenType.MAPPER_XML))
                && (validFile(this.mapperXmlInfo.getPath(), this.mapperXmlInfo.getName(), ".xml")))
            buildXml(table);
    }

    private List<String> getTableColumnTypes(Table table) {
        List types = new ArrayList();
        List columns = table.getColumns();
        int size = columns.size();
        for (int i = 0; i < size; ++i) {
            Column column = (Column) columns.get(i);
            types.add(column.getType());
        }
        return types;
    }

    protected void buildPo(Table table) throws IOException {
        List types = getTableColumnTypes(table);
        File beanFile = new File(this.poInfo.getPath(), this.poInfo.getName() + ".java");
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(beanFile)));
        bw.write("package " + this.poInfo.getPackageName() + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import java.io.Serializable;");
        bw.newLine();
        if (isDate(types)) {
            bw.write("import java.util.Date;");
            bw.newLine();
        }
        if (isDecimal(types)) {
            bw.write("import java.math.BigDecimal;");
            bw.newLine();
        }

        if (isDate(types)) {
            bw.newLine();
            bw.write("import com.fasterxml.jackson.annotation.JsonFormat;");
            bw.newLine();
            bw.write("import com.fasterxml.jackson.annotation.JsonFormat.Shape;");
            bw.newLine();
        }
        bw.newLine();
        bw.write("import lombok.Data;");
        bw.newLine();
        String classComment = "对应表名：" + table.getName();
        if ((table.getComment() != null) && (table.getComment().trim().length() > 0))
            classComment = classComment + ",备注：" + table.getComment().trim();

        bw = buildClassComment(bw, "通过SCS工具自动生成，请勿手工修改。表" + table.getName() + "的PO对象<br/>",
                classComment);

        bw.newLine();
        bw.write("@Data");
        bw.newLine();
        bw.write("public class " + this.poInfo.getName() + " implements Serializable {");
        bw.newLine();
        bw.write("\tprivate static final long serialVersionUID = 1L;");
        bw.newLine();
        bw.newLine();
        List columns = table.getColumns();
        int size = columns.size();
        for (int i = 0; i < size; ++i) {
            Column column = (Column) columns.get(i);
            String field = processField(column.getName());
            bw.write("\t/** 对应字段：" + column.getName());
            String comment = column.getComment();

            // 格式化comment信息，生成备注和校验代码
            // 比如备注为“项目名称@NotBlank(message = \"name must not be empty!\")@Size(max=100, min=50)”
            // 生成为字符串数组
            // 项目名称
            // NotBlank(message = "name must not be empty!")
            // Size(max=100, min=50)
            String[] comments = null;
            if ((comment != null) && (comment.trim().length() > 0)) {
                comments = comment.split("@");
                bw.write(",备注：" + comments[0].trim());
            }

            bw.write(" */");
            bw.newLine();

            if ((comment != null) && (comment.trim().length() > 0)) {
                for (int j = 1; j < comments.length; j++) {
                    bw.write("\t@" + comments[j]);
                    bw.newLine();
                }
            }

            // 数据类型如果要是date，转换成可读格式
            String t = column.getType().toLowerCase();
            if (t.contains("timestamp")) {
                bw.write(
                        "\t@JsonFormat(shape = Shape.STRING, pattern = \"yyyy-MM-dd HH:mm:ss\" , timezone=\"GMT+8\")");
                bw.newLine();
            }
            if (t.contains("date")) {
                bw.write(
                        "\t@JsonFormat(shape = Shape.STRING, pattern = \"yyyy-MM-dd\" , timezone=\"GMT+8\")");
                bw.newLine();
            }

            bw.write("\tprivate " + processType(column.getType()) + " " + field + ";");
            bw.newLine();
            bw.newLine();
        }

        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
        System.out.println("Generate PO file " + beanFile.getAbsolutePath());
    }

    protected void buildVo(Table table) throws IOException {
        List types = getTableColumnTypes(table);
        File beanFile = new File(this.voInfo.getPath(), this.voInfo.getName() + ".java");
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(beanFile)));
        bw.write("package " + this.voInfo.getPackageName() + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import java.io.Serializable;");
        bw.newLine();
        if (isDate(types)) {
            bw.write("import java.util.Date;");
            bw.newLine();
        }
        if (isDecimal(types)) {
            bw.write("import java.math.BigDecimal;");
            bw.newLine();
        }
        if (isDate(types)) {
            bw.newLine();
            bw.write("import com.fasterxml.jackson.annotation.JsonFormat;");
            bw.newLine();
            bw.write("import com.fasterxml.jackson.annotation.JsonFormat.Shape;");
            bw.newLine();
        }
        bw.newLine();
        bw.write("import com.fasterxml.jackson.annotation.JsonIgnoreProperties;");
        bw.newLine();
        bw.write("import lombok.Data;");
        bw.newLine();
        String classComment = "对应表名：" + table.getName();
        if ((table.getComment() != null) && (table.getComment().trim().length() > 0))
            classComment = classComment + ",备注：" + table.getComment().trim();

        bw = buildClassComment(bw, "通过SCS工具自动生成。表" + table.getName() + "的VO对象<br/>", classComment);

        bw.newLine();
        bw.write("@Data");
        bw.newLine();
        bw.write("@JsonIgnoreProperties(ignoreUnknown = true)");
        bw.newLine();
        bw.write("public class " + this.voInfo.getName() + " implements Serializable {");
        bw.newLine();
        bw.write("\tprivate static final long serialVersionUID = 1L;");
        bw.newLine();
        bw.newLine();
        List columns = table.getColumns();
        int size = columns.size();
        for (int i = 0; i < size; ++i) {
            Column column = (Column) columns.get(i);
            String field = processField(column.getName());
            bw.write("\t/** 对应字段：" + column.getName());
            String comment = column.getComment();

            // 格式化comment信息，生成备注和校验代码
            // 比如备注为“项目名称@NotBlank(message = \"name must not be empty!\")@Size(max=100, min=50)”
            // 生成为字符串数组
            // 项目名称
            // NotBlank(message = "name must not be empty!")
            // Size(max=100, min=50)
            String[] comments = null;
            if ((comment != null) && (comment.trim().length() > 0)) {
                comments = comment.split("@");
                bw.write(",备注：" + comments[0].trim());
            }

            bw.write(" */");
            bw.newLine();

            if ((comment != null) && (comment.trim().length() > 0)) {
                for (int j = 1; j < comments.length; j++) {
                    bw.write("\t@" + comments[j]);
                    bw.newLine();
                }
            }

            // 数据类型如果要是date，转换成可读格式
            String t = column.getType().toLowerCase();
            if (t.contains("timestamp")) {
                bw.write(
                        "\t@JsonFormat(shape = Shape.STRING, pattern = \"yyyy-MM-dd HH:mm:ss\" , timezone=\"GMT+8\")");
                bw.newLine();
            }
            if (t.contains("date")) {
                bw.write(
                        "\t@JsonFormat(shape = Shape.STRING, pattern = \"yyyy-MM-dd\" , timezone=\"GMT+8\")");
                bw.newLine();
            }

            bw.write("\tprivate " + processType(column.getType()) + " " + field + ";");
            bw.newLine();
            bw.newLine();
        }

        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
        System.out.println("Generate VO file " + beanFile.getAbsolutePath());
    }

    protected void buildService(Table table) throws IOException {
        List primaryKeys = table.getPrimaryKeys();
        if (primaryKeys.size() != 1)
            throw new IllegalArgumentException("目前只支持单一主键的表");

        PrimaryKey primaryKey = (PrimaryKey) primaryKeys.get(0);
        Column idColumn = null;
        List columns = table.getColumns();
        int size = columns.size();
        for (int i = 0; i < size; ++i) {
            Column column = (Column) columns.get(i);
            if (column.getName().equalsIgnoreCase(primaryKey.getColumnName())) {
                idColumn = column;
                break;
            }
        }
        if (idColumn == null) {
            throw new IllegalArgumentException("找不到主键名对应的字段");
        }

        File serviceFile = new File(this.serviceInfo.getPath(),
                this.serviceInfo.getName() + ".java");
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(serviceFile), "utf-8"));
        bw.write("package " + this.serviceInfo.getPackageName() + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import org.springframework.beans.factory.annotation.Autowired;");
        bw.newLine();
        bw.write("import org.springframework.stereotype.Service;");
        bw.newLine();
        bw.newLine();

        bw.write("import com.scs.framework.core.service.AbstractBaseService;");
        bw.newLine();
        bw.write("import " + this.daoInfo.getPackageName() + "." + this.daoInfo.getName() + ";");
        bw.newLine();
        bw.write("import " + this.poInfo.getPackageName() + "." + this.poInfo.getName() + ";");
        bw.newLine();

        bw.newLine();
        bw.write("@Service");
        bw.newLine();
        bw.write("public class " + this.serviceInfo.getName() + " extends AbstractBaseService<"
                + this.poInfo.getName() + ", " + processType(idColumn.getType()) + "> {");

        bw.newLine();
        bw.newLine();
        bw.write("    @Autowired");
        bw.newLine();
        bw.write("    private " + this.poInfo.getName() + "Dao "
                + toLowerCaseFirstOne(this.poInfo.getName()) + "Dao;");
        bw.newLine();
        bw.newLine();
        bw.write("    @Override");
        bw.newLine();
        bw.write("    protected " + this.poInfo.getName() + "Dao " + "getDao() {");
        bw.newLine();
        bw.write("        return " + toLowerCaseFirstOne(this.poInfo.getName()) + "Dao;");
        bw.newLine();
        bw.write("    }");

        bw.newLine();
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
        System.out.println("Generate Service file " + serviceFile.getAbsolutePath());
    }

    protected void buildDao(Table table) throws IOException {
        List primaryKeys = table.getPrimaryKeys();
        if (primaryKeys.size() != 1)
            throw new IllegalArgumentException("目前只支持单一主键的表");

        PrimaryKey primaryKey = (PrimaryKey) primaryKeys.get(0);
        Column idColumn = null;
        List columns = table.getColumns();
        int size = columns.size();
        for (int i = 0; i < size; ++i) {
            Column column = (Column) columns.get(i);
            if (column.getName().equalsIgnoreCase(primaryKey.getColumnName())) {
                idColumn = column;
                break;
            }
        }
        if (idColumn == null) {
            throw new IllegalArgumentException("找不到主键名对应的字段");
        }

        File mapperFile = new File(this.daoInfo.getPath(), this.daoInfo.getName() + ".java");
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + this.daoInfo.getPackageName() + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import org.apache.ibatis.annotations.Mapper;");
        bw.newLine();
        bw.newLine();
        bw.write("import " + this.poInfo.getPackageName() + "." + this.poInfo.getName() + ";");
        bw.newLine();
        bw.write("import com.scs.framework.core.dao.MyBatisBaseDao;");
        bw.newLine();

        bw = buildClassComment(bw, "",
                "表" + table.getName() + "对应的基于MyBatis实现的Dao接口<br/>\r\n * 在其中添加自定义方法");
        bw.newLine();
        bw.write("@Mapper");
        bw.newLine();
        bw.write("public interface " + this.daoInfo.getName() + " extends MyBatisBaseDao<"
                + this.poInfo.getName() + ", " + processType(idColumn.getType()) + "> {");

        bw.newLine();
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
        System.out.println("Generate Dao file " + mapperFile.getAbsolutePath());
    }

    protected void buildBaseXml(Table table) throws IOException {
        List columns = table.getColumns();
        File mapperXmlFile = new File(this.baseMapperXmlInfo.getPath(),
                this.baseMapperXmlInfo.getName() + ".xml");
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.newLine();
        bw.write(
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");

        bw.newLine();
        bw.write("<!-- ============================================================== -->");
        bw.newLine();
        bw.write("<!-- ============================================================== -->");
        bw.newLine();
        bw.write("<!-- =======通过SCS工具自动生成，请勿手工修改！======= -->");
        bw.newLine();
        bw.write("<!-- =======本配置文件中定义的节点可在自定义配置文件中直接使用！       ======= -->");
        bw.newLine();
        bw.write("<!-- ============================================================== -->");
        bw.newLine();
        bw.write("<!-- ============================================================== -->");
        bw.newLine();
        bw.write("<mapper namespace=\"" + this.daoInfo.getPackageName() + "."
                + this.daoInfo.getName() + "\">");
        bw.newLine();
        bw.write("\t<!-- 默认开启二级缓存,使用Least Recently Used（LRU，最近最少使用的）算法来收回 -->");
        bw.newLine();
        bw.write("\t<!-- <cache/> -->");
        bw.newLine();

        List primaryKeys = table.getPrimaryKeys();
        PrimaryKey primaryKey = (PrimaryKey) primaryKeys.get(0);

        buildBaseDaoSQL_BaseResultMap(bw, table, primaryKey, columns);
        buildBaseDaoSQL_Base_Column_List(bw, table, primaryKey, columns);
        buildBaseDaoSQL_Base_Select_By_Entity_Where(bw, table, primaryKey, columns);
        buildBaseDaoSQL_Base_Select_By_Entity(bw, table, primaryKey, columns);
        buildBaseDaoSQL_SelectByPrimaryKey(bw, table, primaryKey, columns);
        buildBaseDaoSQL_SelectBatchByPrimaryKeys(bw, table, primaryKey, columns);

        buildBaseDaoSQL_SelectPage(bw, table, primaryKey, columns);
        buildBaseDaoSQL_SelectAll(bw, table, primaryKey, columns);
        buildBaseDaoSQL_DeleteByPrimaryKey(bw, table, primaryKey, columns);
        buildBaseDaoSQL_DeleteBatchByPrimaryKeys(bw, table, primaryKey, columns);
        buildBaseDaoSQL_Insert(bw, table, primaryKey, columns);
        buildBaseDaoSQL_InsertSelective(bw, table, primaryKey, columns);
        buildBaseDaoSQL_UpdateSelectiveByPrimaryKey(bw, table, primaryKey, columns);
        buildBaseDaoSQL_UpdateByPrimaryKey(bw, table, primaryKey, columns);

        bw.write("</mapper>");
        bw.flush();
        bw.close();
        System.out.println("Generate BaseXml file " + mapperXmlFile.getAbsolutePath());
    }

    protected void buildXml(Table table) throws IOException {
        File mapperXmlFile = new File(this.mapperXmlInfo.getPath(),
                this.mapperXmlInfo.getName() + ".xml");
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.newLine();
        bw.write(
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");

        bw.newLine();
        bw.write("<!-- ============================================================== -->");
        bw.newLine();
        bw.write("<!-- ================可直接使用Base配置文件中定义的节点！================ -->");
        bw.newLine();
        bw.write("<!-- ============================================================== -->");
        bw.newLine();
        bw.write("<mapper namespace=\"" + this.daoInfo.getPackageName() + "."
                + this.daoInfo.getName() + "\">");
        bw.newLine();

        bw.write("  <!-- 请在下方添加自定义配置-->");
        bw.newLine();
        bw.newLine();
        bw.newLine();
        bw.write("</mapper>");
        bw.flush();
        bw.close();
        System.out.println("Generate Xml file " + mapperXmlFile.getAbsolutePath());
    }

    protected void buildBaseDaoSQL_BaseResultMap(BufferedWriter bw, Table table,
                                                 PrimaryKey primaryKey, List<Column> columns)
        throws IOException {
        int size = columns.size();
        bw.write("\t<!-- 通用查询结果对象-->");
        bw.newLine();
        bw.write("\t<resultMap id=\"BaseResultMap\" type=\"" + this.poInfo.getPackageName() + "."
                + this.poInfo.getName() + "\">");
        bw.newLine();

        // Postgre, Oracle由于sql查询中使用as不区分大小写，不能写resultMap与属性的对应名
        if (this.genConfig.getDbDriverName().contains("mysql")) {
            for (int i = 0; i < size; ++i) {
                Column column = (Column) columns.get(i);
                if (column.getName().equalsIgnoreCase(primaryKey.getColumnName()))
                    bw.write("\t\t <id ");
                else
                    bw.write("\t\t <result ");

                bw.write("column=\"" + column.getName() + "\" ");
                bw.write("property=\"" + processField(column.getName()) + "\"/> ");
                bw.newLine();
            }
        }
        bw.write("\t</resultMap>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_Base_Column_List(BufferedWriter bw, Table table,
                                                    PrimaryKey primaryKey, List<Column> columns)
        throws IOException {
        int size = columns.size();
        bw.write("\t<!-- 通用查询结果列-->");
        bw.newLine();
        bw.write("\t<sql id=\"Base_Column_List\">");
        bw.newLine();
        bw.write("\t\t");
        for (int i = 0; i < size; ++i) {
            Column column = (Column) columns.get(i);
            bw.write(" " + column.getName());
            if (!this.genConfig.getDbDriverName().contains("mysql")) {
                if (column.getName().contains("_"))
                    bw.write(" AS " + processField(column.getName()));
            }

            if (i != size - 1)
                bw.write(",");

            if (i % 5 == 4) {
                bw.newLine();
                bw.write("\t\t");
            }
        }
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_Base_Select_By_Entity_Where(BufferedWriter bw, Table table,
                                                               PrimaryKey primaryKey,
                                                               List<Column> columns)
        throws IOException {
        int size = columns.size();
        bw.write("\t<!-- 按对象查询记录的WHERE部分 -->");
        bw.newLine();
        bw.write("\t<sql id=\"Base_Select_By_Entity_Where\">");
        bw.newLine();
        for (int i = 0; i < size; ++i) {
            Column column = (Column) columns.get(i);
            bw.write("\t\t<if test=\"" + processField(column.getName()) + " != null and "
                    + processField(column.getName()) + " != ''" + "\">");
            bw.newLine();
            bw.write("\t\t\tand " + column.getName() + " = #{" + processField(column.getName())
                    + "}");
            bw.newLine();
            bw.write("\t\t</if>");
            bw.newLine();
        }
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_Base_Select_By_Entity(BufferedWriter bw, Table table,
                                                         PrimaryKey primaryKey,
                                                         List<Column> columns)
        throws IOException {
        bw.write("\t<!-- 按对象查询记录的SQL部分 -->");
        bw.newLine();
        bw.write("\t<sql id=\"Base_Select_By_Entity\">");
        bw.newLine();
        bw.write("\t\tselect");
        bw.newLine();
        bw.write("\t\t\t<include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\tfrom " + table.getName());
        bw.newLine();
        bw.write("\t\t<where>");
        bw.newLine();
        bw.write("\t\t\t<include refid=\"Base_Select_By_Entity_Where\" />");
        bw.newLine();
        bw.write("\t\t</where>");
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_SelectByPrimaryKey(BufferedWriter bw, Table table,
                                                      PrimaryKey primaryKey, List<Column> columns)
        throws IOException {
        bw.write("\t<!-- 按主键查询一条记录 -->");
        bw.newLine();
        bw.write(
                "\t<select id=\"selectByPrimaryKey\" resultMap=\"BaseResultMap\" parameterType=\"map\">");
        bw.newLine();
        bw.write("\t\tselect");
        bw.newLine();
        bw.write("\t\t\t<include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\tfrom " + table.getName());
        bw.newLine();
        bw.write("\t\twhere " + primaryKey.getColumnName() + " = #{param1}");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_SelectBatchByPrimaryKeys(BufferedWriter bw, Table table,
                                                            PrimaryKey primaryKey,
                                                            List<Column> columns)
        throws IOException {
        bw.write("\t<!-- 按主键List查询多条记录 -->");
        bw.newLine();
        bw.write(
                "\t<select id=\"selectBatchByPrimaryKeys\" resultMap=\"BaseResultMap\" parameterType=\"map\">");
        bw.newLine();
        bw.write("\t\tselect");
        bw.newLine();
        bw.write("\t\t\t<include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\tfrom " + table.getName());
        bw.newLine();
        bw.write("\t\twhere " + primaryKey.getColumnName() + " in");
        bw.newLine();
        bw.write(
                "\t\t<foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" separator=\",\" close=\")\">");
        bw.newLine();
        bw.write("\t\t\t#{item}");
        bw.newLine();
        bw.write("\t\t</foreach>");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_SelectOne(BufferedWriter bw, Table table, PrimaryKey primaryKey,
                                             List<Column> columns)
        throws IOException {
        bw.write("\t<!-- 按对象查询一条记录 -->");
        bw.newLine();
        bw.write("\t<select id=\"selectOne\" resultMap=\"BaseResultMap\" parameterType=\""
                + this.poInfo.getPackageName() + "." + this.poInfo.getName() + "\">");
        bw.newLine();
        bw.write("\t\t<include refid=\"Base_Select_By_Entity\" />");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_SelectPage(BufferedWriter bw, Table table, PrimaryKey primaryKey,
                                              List<Column> columns)
        throws IOException {
        bw.write("\t<!-- 按对象查询一页记录（多条记录） -->");
        bw.newLine();
        bw.write("\t<select id=\"selectPage\" resultMap=\"BaseResultMap\" parameterType=\""
                + this.poInfo.getPackageName() + "." + this.poInfo.getName() + "\">");
        bw.newLine();
        bw.write("\t\t<include refid=\"Base_Select_By_Entity\" />");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_SelectAll(BufferedWriter bw, Table table, PrimaryKey primaryKey,
                                             List<Column> columns)
        throws IOException {
        bw.write("\t<!-- 按对象查询一页记录（多条记录） -->");
        bw.newLine();
        bw.write("\t<select id=\"selectAll\" resultMap=\"BaseResultMap\" parameterType=\""
                + this.poInfo.getPackageName() + "." + this.poInfo.getName() + "\">");
        bw.newLine();
        bw.write("\t\t<include refid=\"Base_Select_By_Entity\" />");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_DeleteByPrimaryKey(BufferedWriter bw, Table table,
                                                      PrimaryKey primaryKey, List<Column> columns)
        throws IOException {
        bw.write("\t<!-- 按主键删除一条记录 -->");
        bw.newLine();
        bw.write("\t<delete id=\"deleteByPrimaryKey\" parameterType=\"map\">");
        bw.newLine();
        bw.write("\t\tdelete from " + table.getName());
        bw.newLine();
        bw.write("\t\twhere " + primaryKey.getColumnName() + " = #{param1}");
        bw.newLine();
        bw.write("\t</delete>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_DeleteBatchByPrimaryKeys(BufferedWriter bw, Table table,
                                                            PrimaryKey primaryKey,
                                                            List<Column> columns)
        throws IOException {
        bw.write("\t<!-- 按主键List删除多条记录 -->");
        bw.newLine();
        bw.write("\t<delete id=\"deleteBatchByPrimaryKeys\" parameterType=\"map\">");
        bw.newLine();
        bw.write("\t\tdelete from " + table.getName());
        bw.newLine();
        bw.write("\t\twhere " + primaryKey.getColumnName() + " in ");
        bw.newLine();
        bw.write(
                "\t\t<foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" separator=\",\" close=\")\">");
        bw.newLine();
        bw.write("\t\t\t#{item}");
        bw.newLine();
        bw.write("\t\t</foreach>");
        bw.newLine();
        bw.write("\t</delete>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_Insert(BufferedWriter bw, Table table, PrimaryKey primaryKey,
                                          List<Column> columns)
        throws IOException {
        Column column;
        int size = columns.size();
        bw.write("\t<!-- 完整插入一条记录-->");
        bw.newLine();
        bw.write("\t<insert id=\"insert\" parameterType=\"" + this.poInfo.getPackageName() + "."
                + this.poInfo.getName() + "\" useGeneratedKeys=\"true\" keyProperty=\"id\">");
        bw.newLine();
        bw.write("\t\tinsert into " + table.getName() + " (");
        for (int i = 0; i < size; ++i) {
            column = (Column) columns.get(i);

            if (!column.getName().equals("id")) {
                bw.write(column.getName());
                if (i != size - 1)
                    bw.write(", ");

                if (i % 5 == 4) {
                    bw.newLine();
                    bw.write("\t\t\t");
                }
            }
        }
        bw.write(")");
        bw.newLine();
        bw.write("\t\tvalues(");
        for (int i = 0; i < size; ++i) {
            column = (Column) columns.get(i);
            if (!column.getName().equals("id")) {
                bw.write("#{" + processField(column.getName()));
                bw.write("}");
                if (i != size - 1)
                    bw.write(", ");

                if (i % 5 == 4) {
                    bw.newLine();
                    bw.write("\t\t\t");
                }
            }
        }
        bw.write(")");
        bw.newLine();
        bw.write("\t</insert>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_InsertSelective(BufferedWriter bw, Table table,
                                                   PrimaryKey primaryKey, List<Column> columns)
        throws IOException {
        Column column;
        int size = columns.size();
        bw.write("\t<!-- 插入一条记录(为空的字段不操作) -->");
        bw.newLine();
        bw.write("\t<insert id=\"insertSelective\" parameterType=\"" + this.poInfo.getPackageName()
                + "." + this.poInfo.getName() + "\" useGeneratedKeys=\"true\" keyProperty=\"id\">");

        bw.newLine();
        bw.write("\t\tinsert into " + table.getName() + "");
        bw.newLine();
        bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
        bw.newLine();
        for (int i = 0; i < size; ++i) {
            column = (Column) columns.get(i);
            if (!column.getName().equals("id")) {
                bw.write("\t\t\t<if test=\"" + processField(column.getName()) + " != null\" >");
                bw.newLine();
                bw.write("\t\t\t\t" + column.getName() + ",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
        }
        bw.write("\t\t</trim>");
        bw.newLine();
        bw.write("\t\tvalues <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
        bw.newLine();
        for (int i = 0; i < size; ++i) {
            column = (Column) columns.get(i);
            if (!column.getName().equals("id")) {
                bw.write("\t\t\t<if test=\"" + processField(column.getName()) + " != null\" >");
                bw.newLine();
                bw.write("\t\t\t\t#{" + processField(column.getName()) + "},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
        }
        bw.write("\t\t</trim>");
        bw.newLine();
        bw.write("\t</insert>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_UpdateSelectiveByPrimaryKey(BufferedWriter bw, Table table,
                                                               PrimaryKey primaryKey,
                                                               List<Column> columns)
        throws IOException {
        int size = columns.size();
        bw.write("\t<!-- 更新一条记录(为空的字段不操作) -->");
        bw.newLine();
        bw.write("\t<update id=\"updateSelectiveByPrimaryKey\" parameterType=\""
                + this.poInfo.getPackageName() + "." + this.poInfo.getName() + "\">");
        bw.newLine();
        bw.write("\t\tupdate " + table.getName());
        bw.newLine();
        bw.write("\t\t<set>");
        bw.newLine();
        for (int i = 0; i < size; ++i) {
            Column column = (Column) columns.get(i);
            if (column.getName().equalsIgnoreCase(primaryKey.getColumnName()))
                continue;

            bw.write("\t\t\t<if test=\"" + processField(column.getName()) + " != null\" >");
            bw.newLine();
            bw.write("\t\t\t\t" + column.getName() + "=#{" + processField(column.getName()) + "},");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
        bw.write("\t\t</set>");
        bw.newLine();
        bw.write("\t\twhere " + primaryKey.getColumnName() + " = #{"
                + processField(primaryKey.getColumnName()) + "}");
        bw.newLine();
        bw.write("\t</update>");
        bw.newLine();
        bw.newLine();
    }

    protected void buildBaseDaoSQL_UpdateByPrimaryKey(BufferedWriter bw, Table table,
                                                      PrimaryKey primaryKey, List<Column> columns)
        throws IOException {
        int size = columns.size();
        bw.write("\t<!-- 完整更新一条记录 -->");
        bw.newLine();
        bw.write("\t<update id=\"updateByPrimaryKey\" parameterType=\""
                + this.poInfo.getPackageName() + "." + this.poInfo.getName() + "\">");
        bw.newLine();
        bw.write("\t\tupdate " + table.getName());
        bw.newLine();
        bw.write("\t\tset ");
        for (int i = 0; i < size; ++i) {
            Column column = (Column) columns.get(i);
            if (column.getName().equalsIgnoreCase(primaryKey.getColumnName()))
                continue;

            bw.write(column.getName() + "=#{" + processField(column.getName()) + "}");
            if (i != size - 1) {
                bw.write(",");
                bw.newLine();

                bw.write("\t\t\t");
            }
        }
        bw.newLine();
        bw.write("\t\twhere " + primaryKey.getColumnName() + " = #{"
                + processField(primaryKey.getColumnName()) + "}");
        bw.newLine();
        bw.write("\t</update>");
        bw.newLine();
        bw.newLine();
    }

    private String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0)))
                    .append(s.substring(1)).toString();
    }
}
