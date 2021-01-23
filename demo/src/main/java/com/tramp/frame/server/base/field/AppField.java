package com.tramp.frame.server.base.field;

/**
* app表字段
* @author autoCoder
* @since 2021-01-23 10:53:03
*/
public final class AppField extends BaseField {

    private AppField() {
        super();
    }

    public static AppField update() {
        return new AppField();
    }

    public AppField name() {
        this.addField("name");
        return this;
    }

    public AppField status() {
        this.addField("status");
        return this;
    }

    public AppField remark() {
        this.addField("remark");
        return this;
    }

}
