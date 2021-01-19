package com.tramp.generator.mojo;

import com.tramp.generator.config.GeneratorXmlConfig;
import com.tramp.generator.entity.DBColumn;
import com.tramp.generator.entity.DBTable;
import com.tramp.generator.utils.MysqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author chenjm1
 * @since 2019/3/28
 */
@Mojo(name = "3-compare-db", defaultPhase = LifecyclePhase.PACKAGE)
public class CompareMojo extends AbstractMojo {
    private static Logger log = LoggerFactory.getLogger(CompareMojo.class);

    @Parameter(required = true)
    private String configPath;

    @Override
    public void execute() {
        if (StringUtils.isBlank(configPath)) {
            log.error("configPath is null");
            System.exit(0);
        }
        try {
            GeneratorXmlConfig.init(configPath);
            List<DBTable> tableList = MysqlUtils.getAllTables(GeneratorXmlConfig.dataSourceUrl, GeneratorXmlConfig.dbName, GeneratorXmlConfig.userName, GeneratorXmlConfig.password);
            List<DBTable> compareTableList = MysqlUtils.getAllTables(GeneratorXmlConfig.compareDataSourceUrl, GeneratorXmlConfig.compareDbName, GeneratorXmlConfig.compareUserName, GeneratorXmlConfig.comparePassword);
            StringBuffer result = new StringBuffer();
            for (DBTable dbTable : tableList) {
                StringBuffer sb = new StringBuffer("\r\n*************表:" + dbTable.getName() + "*************");
                Boolean tableExit = false;
                for (DBTable compareDbTable : compareTableList) {
                    if (compareDbTable.getName().equals(dbTable.getName())) {//目标库存在同一个表
                        tableExit = true;
                        //对比字段(名字，类型，长度，是否必填)
                        sb = compareColumn(dbTable, compareDbTable, sb);
                    }
                }
                if (!tableExit) {//目标库不存在
                    sb.append("\r\n不存在");
                }
                if (!sb.toString().equals("\r\n*************表:" + dbTable.getName() + "*************")) {
                    result.append(sb);
                }
            }
            if (StringUtils.isNotBlank(result.toString())) {
                log.info("***************** 对比结果(字段对比项:类型，长度，是否必填)******************\r\n" + result.toString());
            } else {
                log.info("***************** 匹配一致(字段对比项:类型，长度，小数精度，是否必填)******************");
            }
        } catch (Exception e) {
            log.error("compareDb 错误", e);
        }
    }

    /**
     * 对比字段(名字，类型，长度，是否必填)
     *
     * @param dbTable
     * @param sb
     * @param compareDbTable
     */
    private StringBuffer compareColumn(DBTable dbTable, DBTable compareDbTable, StringBuffer sb) {
        List<DBColumn> columnList = dbTable.getColumnList();
        List<DBColumn> compareColumnList = compareDbTable.getColumnList();
        for (DBColumn dbColumn : columnList) {
            Boolean columnExit = false;
            for (DBColumn compareColumn : compareColumnList) {
                if (dbColumn.getName().equals(compareColumn.getName())) {
                    columnExit = true;
                    Boolean notSame = false;
                    if (!dbColumn.getType().equals(compareColumn.getType())) {
                        notSame = true;
                    }
                    if (dbColumn.getColumnSize() != compareColumn.getColumnSize()) {
                        notSame = true;
                    }
                    if (dbColumn.getDecimalDigits() != compareColumn.getDecimalDigits()) {
                        notSame = true;
                    }
                    if (dbColumn.getNullable() != compareColumn.getNullable()) {
                        notSame = true;
                    }
                    if (notSame) {
                        sb.append("\r\n字段 " + dbColumn.getName() + ":");
                    }
                    if (!dbColumn.getType().equals(compareColumn.getType())) {
                        sb.append("  类型不匹配").append(",原:" + dbColumn.getType() + ",目标:" + compareColumn.getType()).append(";");
                    }
                    if (dbColumn.getColumnSize() != compareColumn.getColumnSize()) {
                        sb.append("  长度不匹配").append(",原:" + dbColumn.getColumnSize() + ",目标:" + compareColumn.getColumnSize()).append(";");
                    }
                    if (dbColumn.getDecimalDigits() != compareColumn.getDecimalDigits()) {
                        sb.append("  精度不匹配").append(",原:" + dbColumn.getDecimalDigits() + ",目标:" + compareColumn.getDecimalDigits()).append(";");
                    }
                    if (dbColumn.getNullable() != compareColumn.getNullable()) {
                        sb.append("  是否必填不匹配").append(",原:" + dbColumn.getNullable() + ",目标:" + compareColumn.getNullable()).append(";");
                    }
                }
            }
            if (!columnExit) {
                sb.append("\r\n不存在字段:" + dbColumn.getName());
            }
        }
        return sb;
    }
}
