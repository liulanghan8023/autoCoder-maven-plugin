package com.tramp.generator.common;

import com.tramp.generator.config.GeneratorXmlConfig;
import com.tramp.generator.entity.DBColumnEnum;
import com.tramp.generator.entity.DBTable;
import com.tramp.generator.utils.FreeMarkerUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * FreeMarker上下文
 *
 * @author chenjm1
 * @since 2017/10/24
 */
public class FreeMarkerContext {

    private String className;//类名
    private String variableName;//变量名
    private String tableName;//表名
    private String tableNameAbbre;//表名缩写
    private String tableEnumName;//表名
    private DBColumnEnum dbColumnEnum;
    private DBTable table;
    private String dateTime;
    private String author;
    private String remark;
    private String requestBasePath;
    private List<String> methods;
    private List<DBTable> tableList;

    public FreeMarkerContext(List<DBTable> tableList) {
        this.tableList = tableList;
    }

    public FreeMarkerContext(DBTable dbTable) {
        this.table = dbTable;
        if (dbTable != null) {
            this.tableName = dbTable.getName();
            this.tableNameAbbre = FreeMarkerUtil.initialStrToAbbreviation(tableName);
            if (StringUtils.isBlank(dbTable.getPrefix())) {
                this.className = FreeMarkerUtil.upperName(dbTable.getName());
                this.variableName = FreeMarkerUtil.initialStrToLow(FreeMarkerUtil.upperName(tableName));
            } else {
                this.className = FreeMarkerUtil.upperName(dbTable.getName().replace(dbTable.getPrefix(), ""));
                this.variableName = FreeMarkerUtil.initialStrToLow(this.className);
            }
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.dateTime = format.format(date);
            this.author = GeneratorXmlConfig.author;
            this.remark = GeneratorXmlConfig.remark;
            this.requestBasePath = dbTable.getRequestBasePath();
            this.methods = dbTable.getMethods();
        }
    }

    public List<DBTable> getTableList() {
        return tableList;
    }

    public void setTableList(List<DBTable> tableList) {
        this.tableList = tableList;
    }

    public String getTableNameAbbre() {
        return tableNameAbbre;
    }

    public void setTableNameAbbre(String tableNameAbbre) {
        this.tableNameAbbre = tableNameAbbre;
    }

    public String getTableEnumName() {
        return tableEnumName;
    }

    public void setTableEnumName(String tableEnumName) {
        this.tableEnumName = tableEnumName;
    }

    public DBColumnEnum getDbColumnEnum() {
        return dbColumnEnum;
    }

    public void setDbColumnEnum(DBColumnEnum dbColumnEnum) {
        this.dbColumnEnum = dbColumnEnum;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public String getRequestBasePath() {
        return requestBasePath;
    }

    public void setRequestBasePath(String requestBasePath) {
        this.requestBasePath = requestBasePath;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    //首字母大写
    public static String upperName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public DBTable getTable() {
        return table;
    }

    public void setTable(DBTable table) {
        this.table = table;
    }
}
