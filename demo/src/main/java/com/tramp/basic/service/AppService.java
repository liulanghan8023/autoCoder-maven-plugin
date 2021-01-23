package com.tramp.basic.service;

import com.tramp.frame.server.base.dao.AppBaseDao;
import com.tramp.basic.dao.AppDao;
import com.tramp.frame.server.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tramp.basic.entity.App;
import org.springframework.transaction.annotation.Transactional;


/**
* app业务逻辑层
* @author liulanghan
* @since 2021-01-23 10:34:15
*/
@Service
@Transactional
public class AppService extends BaseService<App> {

    @Autowired
    private AppDao appDao;

    public AppService(AppBaseDao appDao) {
        super(appDao);
    }

}
