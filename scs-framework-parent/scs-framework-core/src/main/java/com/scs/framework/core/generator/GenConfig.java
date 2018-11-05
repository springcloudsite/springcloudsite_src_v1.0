/*
 * Copyright (c) Pactera Corp.
 * All Rights Reserved.
 */
package com.scs.framework.core.generator;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * GenConfig.
 *
 * @version ScsDI v1.0
 * @author Zhang Ran, 2018年4月19日
 */
public class GenConfig {

    private String basePackage;
    protected String saveDir;
    protected String saveDirForVo;
    protected String saveDirForXml;
    private GenType[] genTypes;
    protected String[] tableNames;
    protected boolean fileOverride;
    protected boolean dbPrefix;
    protected boolean dbColumnUnderline;
    protected String dbDriverName;
    protected String dbUser;
    protected String dbPassword;
    protected String dbUrl;
    protected String dbSchema;

    public GenConfig() {
        this.tableNames = null;

        this.fileOverride = true;

        this.dbPrefix = false;

        this.dbColumnUnderline = false;
    }

    public String getBasePackage() {
        return this.basePackage;
    }

    public String getSaveDir() {
        return this.saveDir;
    }

    public String getSaveDirForVo() {
        return this.saveDirForVo;
    }

    public String getSaveDirForXml() {
        return this.saveDirForXml;
    }

    public GenType[] getGenTypes() {
        return this.genTypes;
    }

    public String[] getTableNames() {
        return this.tableNames;
    }

    public boolean isFileOverride() {
        return this.fileOverride;
    }

    public boolean isDbPrefix() {
        return this.dbPrefix;
    }

    public boolean isDbColumnUnderline() {
        return this.dbColumnUnderline;
    }

    public String getDbDriverName() {
        return this.dbDriverName;
    }

    public String getDbUser() {
        return this.dbUser;
    }

    public String getDbPassword() {
        return this.dbPassword;
    }

    public String getDbUrl() {
        return this.dbUrl;
    }

    public String getDbSchema() {
        return this.dbSchema;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setSaveDir(String saveDir) {
        this.saveDir = saveDir;
    }

    public void setSaveDirForVo(String saveDirForVo) {
        this.saveDirForVo = saveDirForVo;
    }

    public void setSaveDirForXml(String saveDirForXml) {
        this.saveDirForXml = saveDirForXml;
    }

    public void setGenTypes(GenType[] genTypes) {
        this.genTypes = genTypes;
    }

    public void setTableNames(String[] tableNames) {
        this.tableNames = tableNames;
    }

    public void setFileOverride(boolean fileOverride) {
        this.fileOverride = fileOverride;
    }

    public void setDbPrefix(boolean dbPrefix) {
        this.dbPrefix = dbPrefix;
    }

    public void setDbColumnUnderline(boolean dbColumnUnderline) {
        this.dbColumnUnderline = dbColumnUnderline;
    }

    public void setDbDriverName(String dbDriverName) {
        this.dbDriverName = dbDriverName;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void setDbSchema(String dbSchema) {
        this.dbSchema = dbSchema;
    }

    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    protected boolean canEqual(Object other) {
        return other instanceof GenConfig;
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String toString() {
        return "GenConfig(basePackage=" + getBasePackage() + ", saveDir=" + getSaveDir()
                + ", saveDirForVo=" + getSaveDirForVo() + ", saveDirForXml=" + getSaveDirForXml()
                + ", genTypes=" + Arrays.deepToString(getGenTypes()) + ", tableNames="
                + Arrays.deepToString(getTableNames()) + ", fileOverride=" + isFileOverride()
                + ", dbPrefix=" + isDbPrefix() + ", dbColumnUnderline=" + isDbColumnUnderline()
                + ", dbDriverName=" + getDbDriverName() + ", dbUser=" + getDbUser()
                + ", dbPassword=" + getDbPassword() + ", dbUrl=" + getDbUrl() + ", dbSchema="
                + getDbSchema() + ")";
    }
}
