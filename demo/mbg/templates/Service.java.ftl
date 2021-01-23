package com.tramp.${table.module}.service;

import com.tramp.frame.server.base.dao.${className}BaseDao;
import com.tramp.${table.module}.dao.${className}Dao;
import com.tramp.frame.server.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tramp.${table.module}.entity.${className};
import org.springframework.transaction.annotation.Transactional;


/**
* ${table.remark}业务逻辑层
* @author liulanghan
* @since ${dateTime}
*/
@Service
@Transactional
public class ${className}Service extends BaseService<${className}> {

    @Autowired
    private ${className}Dao ${variableName}Dao;

    public ${className}Service(${className}BaseDao ${variableName}Dao) {
        super(${variableName}Dao);
    }

}
