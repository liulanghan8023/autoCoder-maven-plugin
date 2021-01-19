package com.tramp.generator.entity;

import java.util.List;

/**
 * 表
 *
 * @author chenjm1
 * @since 2017/10/24
 */
public class DBTable {

    private String name;
    private String tableNameAbbre;//表名缩写
    private List<DBColumn> columnList;
    private String remark;
    private Boolean overwrite = false;
    private Boolean recordable = true;
    private String prefix;
    private String module;
    private String requestBasePath;
    private List<String> methods;
    private List<DBColumnEnum> columnEnumList;
    private String primaryKey; //主键
    private String primaryKeyVariableName; //主键,驼峰

    public String getPrimaryKeyVariableName() {
        return primaryKeyVariableName;
    }

    public void setPrimaryKeyVariableName(String primaryKeyVariableName) {
        this.primaryKeyVariableName = primaryKeyVariableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getTableNameAbbre() {
        return tableNameAbbre;
    }

    public void setTableNameAbbre(String tableNameAbbre) {
        this.tableNameAbbre = tableNameAbbre;
    }

    public List<DBColumnEnum> getColumnEnumList() {
        return columnEnumList;
    }

    public void setColumnEnumList(List<DBColumnEnum> columnEnumList) {
        this.columnEnumList = columnEnumList;
    }

    public String getRequestBasePath() {
        return requestBasePath;
    }

    public void setRequestBasePath(String requestBasePath) {
        this.requestBasePath = requestBasePath;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public String getPrefix() {
        return prefix;
    }

    public Boolean getRecordable() {
        return recordable;
    }

    public void setRecordable(Boolean recordable) {
        this.recordable = recordable;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DBColumn> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<DBColumn> columnList) {
        this.columnList = columnList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
