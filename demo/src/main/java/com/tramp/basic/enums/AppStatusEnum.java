package com.tramp.basic.enums;

/**
 * @author chenjm1
 * @since 2021-01-23 10:34:15
 */
public enum AppStatusEnum {

     EFFECTIVE("EFFECTIVE", "有效"), INVALID("INVALID", "失效");

    AppStatusEnum(String code, String display) {
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
