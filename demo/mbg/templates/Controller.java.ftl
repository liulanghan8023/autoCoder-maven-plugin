package com.tramp.controller;

import com.tramp.${table.module}.service.${className}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
* ${table.remark}控制层
* @author liulanghan
* @since ${dateTime}
*/
@Controller
@RequestMapping("${requestBasePath}")
public class ${className}Controller {

    @Autowired
    private ${className}Service ${variableName}Service;


}
