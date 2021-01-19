package com.tramp.generator.entity;

import java.util.List;

/**
 * @author chenjm1
 * @since 2018/9/27
 */
public class DBColumnEnum {


    private String ColumnName;
    private String codeJavaType;
    private List<EnumValue> enumValueList;

    public List<EnumValue> getEnumValueList() {
        return enumValueList;
    }

    public void setEnumValueList(List<EnumValue> enumValueList) {
        this.enumValueList = enumValueList;
    }

    public String getCodeJavaType() {
        return codeJavaType;
    }

    public void setCodeJavaType(String codeJavaType) {
        this.codeJavaType = codeJavaType;
    }

    public String getColumnName() {
        return ColumnName;
    }

    public void setColumnName(String columnName) {
        ColumnName = columnName;
    }

}
