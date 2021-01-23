<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.tramp.${table.module}.dao.${className}Dao">
    <resultMap id="baseResultMap" extends="com.tramp.frame.server.base.dao.${className}BaseDao.baseResultMap" type="com.tramp.${table.module}.entity.${className}"></resultMap>
    <sql id="baseColumnList">
        <include refid="com.tramp.frame.server.base.dao.${className}BaseDao.baseColumnList" />
    </sql>

</mapper>