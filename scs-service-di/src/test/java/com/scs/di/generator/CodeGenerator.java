/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.di.generator;

import java.util.ArrayList;
import java.util.List;

import com.scs.framework.core.generator.GenConfig;
import com.scs.framework.core.generator.GenParam;
import com.scs.framework.core.generator.GenType;
import com.scs.framework.core.generator.Generator;

/**
 * 代码生成器
 *
 * @version SCS v1.0
 * @author Zhang Ran, 2018年4月24日
 */
public class CodeGenerator {

    public static void main(String[] args) {
        List<GenParam> paramList = new ArrayList<GenParam>();
        paramList.add(new GenParam("di", new String[] { "scs_unit_type"}));
        GenConfig gc = new GenConfig();
        gc.setBasePackage("com.scs");
        // mysql 数据库相关配置
        // 设置基本保存目录（Java源代码根目录）
        // gc.setSaveDir("D:/Work/lab_cloud/server/misc/misc-server/src/main/java");
        gc.setSaveDir("D:\\scs-workspace\\scs-service-di\\src\\main\\java");
        //VO为独立项目时可使用setSaveDirForVo来设置不同的目录
        //gc.setSaveDirForVo(new File(gc.getSaveDir(), "../../../../misc-vo/src/main/java").getAbsolutePath());
        gc.setDbDriverName("com.mysql.jdbc.Driver");
        gc.setDbUser("scsdi_dev");
        gc.setDbSchema("scsdi_dev");
        gc.setDbPassword("scsdi_dev");
        gc.setDbUrl("jdbc:mysql://10.10.8.53:3306/scsdi?serverTimezone=UTC");
        // 生成PO\VO（自动覆盖）、BaseMapperXML（覆盖）、Dao（不覆盖）、MapperXML（不覆盖）
        // 支持生成的文件类型
        gc.setGenTypes(
                new GenType[] { GenType.VO, GenType.PO, GenType.DAO, GenType.BASE_MAPPER_XML, GenType.MAPPER_XML });
        Generator generator = new Generator();
        generator.setGenConfig(gc);
        generator.setParamList(paramList);
        generator.generate();
    }
}
