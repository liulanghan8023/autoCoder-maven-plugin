package com.tramp.${table.module}.enums;

/**
 * @author chenjm1
 * @since ${dateTime}
 */
public enum ${tableEnumName}Enum {

    <#list dbColumnEnum.enumValueList as field> ${field.code}("${field.code}", "${field.display}")<#if field_has_next>,</#if></#list>;

    ${tableEnumName}Enum(String code, String display) {
        this.code = code;
        this.display = display;
    }

    private String code;
    private String display;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
