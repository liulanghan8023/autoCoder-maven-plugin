package com.tramp.${table.module}.entity;

import com.tramp.frame.server.base.BaseEntity;
<#list table.columnList as field><#if field.variableName=='updateTime'>import com.tramp.frame.server.base.Recordable;<#break></#if></#list>
<#list table.columnList as field><#if field.javaType=='Date'>import java.util.Date;<#break></#if></#list>
<#list table.columnList as field><#if field.javaType=='BigDecimal'>import java.math.BigDecimal;<#break></#if></#list>

/**
* ${table.remark}
* @author ${author}
* @since ${dateTime}
*/
public class ${className} implements <#list table.columnList as field><#if field.variableName=='updateTime'>Recordable,<#break></#if></#list> BaseEntity{

<#list table.columnList as field>
    private ${field.javaType} ${field.variableName}; //${field.remark}
</#list>

<#list table.columnList as field>
    public ${field.javaType} get${field.className}() {
        return ${field.variableName};
    }

    public void set${field.className}(${field.javaType} ${field.variableName}) {
        this.${field.variableName} = ${field.variableName};
    }

</#list>

}
