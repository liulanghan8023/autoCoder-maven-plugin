package com.tramp.generator.utils;

import com.google.common.collect.Lists;
import com.tramp.generator.config.GeneratorXmlConfig;
import com.tramp.generator.entity.DBColumn;
import com.tramp.generator.entity.DBColumnEnum;
import com.tramp.generator.entity.DBTable;
import com.tramp.generator.entity.EnumValue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * mysql工具类
 * Created by chen on 2017/10/27.
 */
public class MysqlUtils {
    private static Logger log = LoggerFactory.getLogger(MysqlUtils.class);

    private static String TABLE_SQL = "SELECT TABLE_NAME,TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_NAME = '%s' AND TABLE_SCHEMA = '%s'";
    private static String ALL_TABLE_SQL = "SELECT TABLE_NAME,TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = '%s'";

    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    private static Connection connection = null;

    /**
     * 获取单表
     *
     * @param tableXml
     * @return
     * @throws Exception
     */
    public static DBTable getTable(DBTable tableXml) {
        try {
            String dbTableName;
            String comment;
            try (ResultSet rs = getConnection().createStatement().executeQuery(String.format(TABLE_SQL, tableXml.getName(), GeneratorXmlConfig.dbName))) {
                dbTableName = null;
                comment = null;
                while (rs.next()) {
                    dbTableName = rs.getString(1);
                    comment = rs.getString(2);
                }
            }
            if (StringUtils.isEmpty(dbTableName)) {
                log.error(tableXml.getName() + "表不存在");
                System.exit(0);
            }
            DBTable dbTable = new DBTable();
            dbTable.setName(dbTableName);
            dbTable.setTableNameAbbre(FreeMarkerUtil.initialStrToAbbreviation(dbTableName));
            dbTable.setModule(tableXml.getModule());
            dbTable.setRemark(StringUtils.isBlank(comment) ? "表未注释" : comment);
            dbTable.setRecordable(tableXml.getRecordable());
            dbTable.setPrefix(tableXml.getPrefix());
            dbTable.setOverwrite(tableXml.getOverwrite());
            dbTable.setMethods(tableXml.getMethods());
            dbTable.setRequestBasePath(tableXml.getRequestBasePath());
            DatabaseMetaData databaseMetaData = getConnection().getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(GeneratorXmlConfig.dbName, "%", tableXml.getName(), "%");
            List<DBColumn> columnList = Lists.newArrayList();
            List<DBColumnEnum> columnEnumList = Lists.newArrayList();
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                String typeName = resultSet.getString("TYPE_NAME");
                String remark = resultSet.getString("REMARKS");
                int columnSize = resultSet.getInt("COLUMN_SIZE");
                int decimalDigits = resultSet.getInt("DECIMAL_DIGITS");
                int nullable = resultSet.getInt("NULLABLE");
                DBColumn dbColumn = new DBColumn(columnName, typeName, remark, columnSize, decimalDigits, nullable);

                columnList.add(dbColumn);
                //设置枚举类
                DBColumnEnum columnEnum = getColumnEnum(columnName, typeName, remark);
                if (columnEnum != null) {
                    columnEnumList.add(columnEnum);
                }
            }
            dbTable.setColumnEnumList(columnEnumList);
            dbTable.setColumnList(columnList);
            //获取主键
            ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(GeneratorXmlConfig.dbName, null, tableXml.getName());
            while (primaryKeys.next()) {
                String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
                dbTable.setPrimaryKey(primaryKeyColumnName);
                String upperName = FreeMarkerUtil.upperName(primaryKeyColumnName.toLowerCase());
                dbTable.setPrimaryKeyVariableName(FreeMarkerUtil.initialStrToLow(upperName));
            }
            if (columnList.size() <= 0) {
                log.error(tableXml.getName() + "表无字段");
                System.exit(0);
            }
            return dbTable;
        } catch (Exception e) {
            log.error("查询表发生异常", e);
            System.exit(0);
        }
        return null;
    }

    public static DBColumnEnum getColumnEnum(String columnName, String typeName, String remark) {
        if (StringUtils.isBlank(remark)) {
            return null;
        }
        DBColumnEnum dbColumnEnum = null;
        if (remark.contains("[") && remark.contains("]")) {
            List<String> messageList = FreeMarkerUtil.extractMessageByRegular(remark);
            if (messageList.size() != 1) {
                log.error("枚举类格式错误:" + remark);
                System.exit(0);
            }
            dbColumnEnum = new DBColumnEnum();
            dbColumnEnum.setCodeJavaType(GeneratorXmlConfig.types.get(typeName));
            dbColumnEnum.setColumnName(columnName);
            String message = messageList.get(0);
            String[] enumsArr = message.split(",");
            List<EnumValue> enumValues = Lists.newArrayList();
            for (String enums : enumsArr) {
                String[] valueSplit = enums.split(":");
                if (valueSplit.length < 2) {
                    log.error("枚举类格式错误:" + remark);
                    System.exit(0);
                }
                EnumValue enumValue = new EnumValue();
                String code = valueSplit[0];
                String display = valueSplit[1];
                enumValue.setCode(code);
                enumValue.setDisplay(display);
                enumValues.add(enumValue);
            }
            dbColumnEnum.setEnumValueList(enumValues);
        }
        return dbColumnEnum;


    }

    /**
     * 获取数据库所有表
     *
     * @return
     */
    public static List<DBTable> getAllTables() {
        List<DBTable> list = Lists.newArrayList();
        try {
            try (ResultSet rs = getConnection().createStatement().executeQuery(String.format(ALL_TABLE_SQL, GeneratorXmlConfig.dbName))) {
                coverToDbTable(null, list, rs);
            }
        } catch (Exception e) {
            log.error("查询表是否存在发生异常", e);
            System.exit(0);
        }
        return list;
    }

    /**
     * 获取mysql数据连接
     *
     * @return
     */
    private static Connection getConnection() {
        Connection c = connectionThreadLocal.get();
        if (null == c) {
            initConnection();
        }
        return connectionThreadLocal.get();
    }

    /**
     * 初始化mysql连接
     *
     * @return
     * @throws Exception
     */
    private static void initConnection() {
        try {
            Class.forName(GeneratorXmlConfig.driverClassName);
            connection = DriverManager.getConnection(GeneratorXmlConfig.dataSourceUrl + "/" + GeneratorXmlConfig.dbName, GeneratorXmlConfig.userName,
                    GeneratorXmlConfig.password);
            connectionThreadLocal.set(connection);
        } catch (Exception e) {
            log.info("******************连接信息*******************");
            log.info("url:" + GeneratorXmlConfig.dataSourceUrl + "/" + GeneratorXmlConfig.dbName);
            log.info("user:" + GeneratorXmlConfig.userName);
            log.info("password:" + GeneratorXmlConfig.password);
            log.error("mysql数据库连接异常", e);
            System.exit(0);
        }

    }

    public static void closeConnection() {
        try {
            if (!connectionThreadLocal.get().isClosed()) {
                connectionThreadLocal.get().close();
            }
        } catch (Exception e) {
            log.error("mysql数据库关闭异常", e);
            System.exit(0);
        }
    }

    public static List<DBTable> getAllTables(String dataSourceUrl, String dbName, String userName, String password) {
        Connection mysqlConnection = null;
        try {
            Class.forName(GeneratorXmlConfig.driverClassName);
            mysqlConnection = DriverManager.getConnection(dataSourceUrl + "/" + dbName, userName,
                    password);

        } catch (Exception e) {
            log.error("mysql数据库连接异常:" + dataSourceUrl + ";" + dbName, e);
            System.exit(0);
        }

        List<DBTable> list = Lists.newArrayList();
        Statement statement = null;
        try {
            statement = mysqlConnection.createStatement();
            try (ResultSet rs = statement.executeQuery(String.format(ALL_TABLE_SQL, dbName))) {
                coverToDbTable(mysqlConnection, list, rs);
            }
        } catch (Exception e) {
            log.error("查询表是否存在发生异常", e);
            System.exit(0);
        } finally {
            try {
                statement.close();
                mysqlConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private static void coverToDbTable(Connection mysqlConnection, List<DBTable> list, ResultSet rs) throws SQLException {
        String dbTableName;
        String comment;
        while (rs.next()) {
            DBTable dbTable = new DBTable();
            dbTableName = rs.getString(1);
            comment = rs.getString(2);
            if (StringUtils.isEmpty(dbTableName)) {
                log.error("表不存在");
                System.exit(0);
            }
            dbTable.setOverwrite(true);
            dbTable.setName(dbTableName);
            dbTable.setTableNameAbbre(FreeMarkerUtil.initialStrToAbbreviation(dbTableName));
            dbTable.setRemark(comment);
            DatabaseMetaData databaseMetaData = (mysqlConnection == null ? getConnection() : mysqlConnection).getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(null, "%", dbTableName, "%");
            List<DBColumn> columnList = Lists.newArrayList();
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                String remark = resultSet.getString("REMARKS");
                String typeName = resultSet.getString("TYPE_NAME");
                int columnSize = resultSet.getInt("COLUMN_SIZE");
                int decimalDigits = resultSet.getInt("DECIMAL_DIGITS");
                int nullable = resultSet.getInt("NULLABLE");
                DBColumn dbColumn = new DBColumn(columnName, typeName, remark, columnSize, decimalDigits, nullable);
                columnList.add(dbColumn);
            }
            dbTable.setColumnList(columnList);
            //获取主键
            ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(GeneratorXmlConfig.dbName, null, dbTableName);
            while (primaryKeys.next()) {
                String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
                dbTable.setPrimaryKey(primaryKeyColumnName);
                dbTable.setPrimaryKeyVariableName(FreeMarkerUtil.initialStrToLow(FreeMarkerUtil.upperName(primaryKeyColumnName.toLowerCase())));
            }
            list.add(dbTable);
        }
    }
}
