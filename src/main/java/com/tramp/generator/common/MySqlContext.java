package com.tramp.generator.common;

import com.google.common.collect.Lists;
import com.tramp.generator.config.GeneratorXmlConfig;
import com.tramp.generator.entity.DBTable;
import com.tramp.generator.utils.FreeMarkerUtil;
import com.tramp.generator.utils.MysqlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author chenjm1
 * @since 2017/10/24
 */
public class MySqlContext {
    private static Logger log = LoggerFactory.getLogger(MySqlContext.class);

    private List<DBTable> tableList;

    public MySqlContext(String configPath) {
        this.init(configPath);
    }

    /**
     * 初始化上下文
     */
    private void init(String configPath) {
        GeneratorXmlConfig.init(configPath);
        tableList = Lists.newArrayList();
        List<DBTable> tableXmlList = GeneratorXmlConfig.tables;
        for (DBTable tableXml : tableXmlList) {
            DBTable dbTable = MysqlUtils.getTable(tableXml);
            if (dbTable != null) {
                tableList.add(dbTable);
            }
        }
        if (tableXmlList.size() != tableList.size()) {
            log.error("please check config tables tag");
            System.exit(0);
        }

    }

    public void clear() {
        FreeMarkerUtil.clearCache();
        tableList = null;
    }

    public List<DBTable> getTableList() {
        return tableList;
    }

    public void setTableList(List<DBTable> tableList) {
        this.tableList = tableList;
    }
}
