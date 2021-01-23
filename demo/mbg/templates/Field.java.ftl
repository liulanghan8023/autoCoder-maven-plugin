package com.tramp.frame.server.base.field;

/**
* ${table.remark}表字段
* @author ${author}
* @since ${dateTime}
*/
public final class ${className}Field extends BaseField {

    private ${className}Field() {
        super();
    }

    public static ${className}Field update() {
        return new ${className}Field();
    }
<#list table.columnList as field>
    <#if field.name != 'id'&&field.name != 'create_user'&&field.name != 'create_time'>

    public ${className}Field ${field.variableName}() {
        this.addField("${field.variableName}");
        return this;
    }
    </#if>
</#list>

}
