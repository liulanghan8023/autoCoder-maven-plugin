package com.tramp.generator.entity;

import com.tramp.generator.config.GeneratorXmlConfig;
import com.tramp.generator.utils.FreeMarkerUtil;
import com.tramp.generator.utils.MysqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字段
 *
 * @author chenjm1
 * @since 2017/10/24
 */
public class DBColumn {
    private static Logger log = LoggerFactory.getLogger(DBColumn.class);

    private String name;
    private String variableName;
    private String className;
    private String type;
    private String javaType;
    private String xmlType; //如mybatis的xml中用到
    private String remark;
    private DBColumnEnum columnEnum;
    private int columnSize;
    private int decimalDigits;
    private int nullable;

    public DBColumn(String name, String type, String remark, int columnSize, int decimalDigits, int nullable) {
        this.name = name;
        this.type = type;
        this.remark = remark;
        this.columnSize = columnSize;
        this.decimalDigits = decimalDigits;
        this.nullable = nullable;

        if (StringUtils.isNotEmpty(remark)) {
            this.remark = remark.trim();
        }
        this.className = FreeMarkerUtil.upperName(name.toLowerCase());
        this.variableName = FreeMarkerUtil.initialStrToLow(FreeMarkerUtil.upperName(name.toLowerCase()));
        this.javaType = GeneratorXmlConfig.types.get(type);
        if (StringUtils.isBlank(javaType)) {
            log.error("column is " + name + ",please add type xml tag:" + type);
            System.exit(0);
        }
        String jdbcType = GeneratorXmlConfig.xmlTypes.get(type);
        if (StringUtils.isBlank(jdbcType)) {
            this.xmlType = type;
        } else {
            this.xmlType = jdbcType;
        }
        DBColumnEnum columnEnum = MysqlUtils.getColumnEnum(name, type, remark);
        this.columnEnum = columnEnum;
        //log.info(name + ":" + type + ":" + javaType + ":" + xmlType + ":" + remark);
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public int getNullable() {
        return nullable;
    }

    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    public DBColumnEnum getColumnEnum() {
        return columnEnum;
    }

    public void setColumnEnum(DBColumnEnum columnEnum) {
        this.columnEnum = columnEnum;
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        DBColumn.log = log;
    }

    public String getXmlType() {
        return xmlType;
    }

    public void setXmlType(String xmlType) {
        this.xmlType = xmlType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
