<#assign entityPath="com.tramp.${table.module}.entity.${className}">
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.tramp.frame.server.base.dao.${className}BaseDao">
    <resultMap id="baseResultMap" type="${entityPath}">
        <id column="id" property="id" />
    <#list table.columnList as field>
        <#if field.name != 'id'>
        <result column="${field.name}" property="${field.variableName}"/>
        </#if>
    </#list>
    </resultMap>
    <sql id="baseColumnList">
    <#list table.columnList as field>${field.name}<#if field_has_next>,</#if></#list>
    </sql>
    <select id="get" parameterType="java.lang.String" resultMap="baseResultMap">
        select <include refid="baseColumnList"/>
        from ${table.name}
        where id = ${'#'}{id}
    </select>
    <delete id="delete" parameterType="java.util.Collection">
        delete from ${table.name}
        where 1=1 and id in
        <foreach close=")" collection="list" index="index" item="item" open="(" separator=",">
        ${'#'}{item}
        </foreach>
    </delete>
    <insert id="insert" parameterType="${entityPath}">
        insert into ${table.name} (
    <#list table.columnList as field>
        ${field.name}<#if field_has_next>,</#if>
    </#list>
        )
        values (
    <#list table.columnList as field>
        ${'#'}{${field.variableName}, jdbcType=${field.xmlType}}<#if field_has_next>,</#if>
    </#list>
        )
    </insert>
    <insert id="insertBatch" parameterType="java.util.List">
        insert into ${table.name} (<#list table.columnList as field>${field.name}<#if field_has_next>,</#if></#list>
        )
        values
        <foreach collection="list" item="item" index="index" separator=",">
            (
        <#list table.columnList as field>
            ${'#'}{item.${field.variableName}, jdbcType=${field.xmlType}}<#if field_has_next>,</#if>
        </#list>
            )
        </foreach>
    </insert>
    <update id="update" parameterType="${entityPath}">
        update ${table.name}
        <set>
        <#list table.columnList as field>
            <#if field.name != 'id'&&field.name != 'create_user'&&field.name != 'create_time'>
            ${field.name} = ${'#'}{${field.variableName}, jdbcType=${field.xmlType}}<#if field_has_next>,</#if>
            </#if>
        </#list>
        </set>
        where id = ${'#'}{id}
    </update>
    <update id="updateSelective" parameterType="${entityPath}">
        update ${table.name}
        <set>
    <#list table.columnList as field>
        <#if field.name != 'id'&&field.name != 'create_user'&&field.name != 'create_time'>
            <if test="${field.variableName} != null">
                ${field.name} = ${'#'}{${field.variableName}, jdbcType=${field.xmlType}}<#if field_has_next>,</#if>
            </if>
        </#if>
    </#list>
        </set>
        where id = ${'#'}{id}
    </update>
    <update id="updateBatch" parameterType="java.util.List">
        insert into ${table.name} (<#list table.columnList as field>${field.name}<#if field_has_next>,</#if></#list>
        )
        values
        <foreach collection="list" item="item" index="index" separator=",">
            (
        <#list table.columnList as field>
            ${'#'}{item.${field.variableName}, jdbcType=${field.xmlType}}<#if field_has_next>,</#if>
        </#list>
            )
        </foreach>
        ON DUPLICATE KEY UPDATE
        id = IF(id = VALUES(id),id,NULL)
    <#list table.columnList as field>
        <#if field.name != 'id'>
            ,${field.name} = VALUES(${field.name})
        </#if>
    </#list>
    </update>
    <select id="listByEntity" parameterType="${entityPath}" resultMap="baseResultMap">
        select <include refid="baseColumnList"/>
        from ${table.name}
        where 1=1
    <#list table.columnList as field>
        <#if field.name != 'id'>
            <if test="${field.variableName} != null and ${field.variableName} != ''">
                AND ${field.name} = ${'#'}{${field.variableName}}
            </if>
        </#if>
    </#list>
    </select>
    <update id="updateField">
        update ${table.name}
        <set>
    <#list table.columnList as field>
        <#if field.name != 'id'&&field.name != 'create_user'&&field.name != 'create_time'>
            <if test="param2.${field.variableName}">
                ${field.name} = ${'#'}{param1.${field.variableName}, jdbcType=${field.xmlType}},
            </if>
        </#if>
    </#list>
        </set>
        where id = ${'#'}{param1.id}
    </update>
</mapper>