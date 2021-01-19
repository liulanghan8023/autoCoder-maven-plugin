package com.tramp.generator.enums;

/**
 * @author chenjm1
 * @since 2019/2/25
 */
public enum ExportTypeEnum {

    EXCEL("EXCEL", "excel");

    private String code;
    private String display;

    ExportTypeEnum(String code, String display) {
        this.code = code;
        this.display = display;
    }

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
