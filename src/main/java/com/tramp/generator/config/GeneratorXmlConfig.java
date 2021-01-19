package com.tramp.generator.config;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.org.apache.xerces.internal.impl.Constants;
import com.tramp.generator.entity.DBTable;
import com.tramp.generator.entity.GeneratorTemplate;
import com.tramp.generator.entity.Group;
import com.tramp.generator.entity.Templates;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author chenjm1
 * @since 2017/10/25
 */
public class GeneratorXmlConfig {
    private static Logger log = LoggerFactory.getLogger(GeneratorXmlConfig.class);

    public static String sysBasePath;
    public static String author;
    public static String remark;
    public static String dataSourceUrl;
    public static String dbName;
    public static String userName;
    public static String password;
    public static String driverClassName;
    //要对比的数据库信息
    public static String compareDataSourceUrl;
    public static String compareDbName;
    public static String compareUserName;
    public static String comparePassword;
    public static String compareDriverClassName;

    public static Map<String, String> types = Maps.newHashMap();
    public static Map<String, String> xmlTypes = Maps.newHashMap();
    public static List<DBTable> tables;
    //public static String templatesPath;
    public static Templates templates;

    static {
        //types 初始化
        types.put("TINYINT", "Boolean");
        types.put("INT", "Integer");
        types.put("INTEGER", "Integer");
        types.put("SMALLINT", "Integer");
        types.put("NUMERIC", "Integer");
        types.put("DECIMAL", "BigDecimal");
        types.put("BIGINT", "Long");
        types.put("FLOAT", "Float");
        types.put("DOUBLE", "Double");
        types.put("CHAR", "String");
        types.put("VARCHAR", "String");
        types.put("TEXT", "String");
        types.put("DATE", "Date");
        types.put("DATETIME", "Date");
        types.put("TIME", "Timestamp");
        types.put("TIMESTAMP", "Timestamp");
        types.put("MEDIUMTEXT", "String");
        types.put("BIT", "Boolean");
        types.put("LONGBLOB", "byte[]");
        types.put("TINYTEXT", "String");

        //xmlType 初始化
        xmlTypes.put("TINYTEXT", "VARCHAR");
        xmlTypes.put("MEDIUMTEXT", "LONGVARCHAR");
        xmlTypes.put("INT", "INTEGER");
        xmlTypes.put("DATETIME", "TIMESTAMP");
        xmlTypes.put("TEXT", "VARCHAR");
        xmlTypes.put("LONGBLOB", "LONGVARBINARY");
        xmlTypes.put("TINYTEXT", "VARCHAR");
    }

    /**
     * 初始化
     */
    public static void init(String configPath) {
        sysBasePath = System.getProperty("user.dir") + "\\";
        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        try {
            //设置不需要校验头文件
            reader.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.LOAD_EXTERNAL_DTD_FEATURE, false);
        } catch (SAXException e) {
            log.error("init error ", e);
            System.exit(0);
        }
        //读取文件 转换成Document
        try {
            Document document = reader.read(new File(configPath));
            Element rootElement = document.getRootElement();
            Iterator<Element> iterator = rootElement.elementIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                String elementName = element.getName();
                if (ConfigXmlProperties.DATA_SOURCE.equals(elementName)) {
                    dataSourceInit(element);
                } else if (ConfigXmlProperties.COMPARE_DATA_SOURCE.equals(elementName)) {
                    compareDataSourceInit(element);
                } else if (ConfigXmlProperties.TYPES.equals(elementName)) {
                    typesInit(element);
                } else if (ConfigXmlProperties.TEMPLATES.equals(elementName)) {
                    templatesInit(element);
                } else if (ConfigXmlProperties.TABLES.equals(elementName)) {
                    tablesInit(element);
                } else if (ConfigXmlProperties.INFO.equals(elementName)) {
                    infoInit(element);
                } else if (ConfigXmlProperties.XML_TYPES.equals(elementName)) {
                    xmlTypesInit(element);
                }
            }
            if (StringUtils.isBlank(dbName)) {
                log.error("数据库信息配置错误 db is blank,check the dataSource tag");
                System.exit(0);
            }
        } catch (DocumentException e) {
            log.error("init error ", e);
            System.exit(0);
        }
    }

    /**
     * 表初始化
     *
     * @param element
     */
    private static void tablesInit(Element element) {
        if (!ConfigXmlProperties.TABLES.equals(element.getName())) {
            return;
        }
        tables = Lists.newArrayList();
        Iterator<Element> iterator = element.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            DBTable table = new DBTable();
            List<Attribute> list = e.attributes();
            for (Attribute attribute : list) {
                String attributeName = attribute.getName();
                String attributeValue = attribute.getValue();
                if (ConfigXmlProperties.OVERWRITE.equals(attributeName)) {
                    if ("true".equals(attributeValue)) {
                        table.setOverwrite(true);
                    }
                }
                if (ConfigXmlProperties.RECORDABLE.equals(attributeName)) {
                    if ("false".equals(attributeValue)) {
                        table.setRecordable(false);
                    }
                } else if (ConfigXmlProperties.NAME.equals(attributeName)) {
                    table.setName(attributeValue);
                } else if (ConfigXmlProperties.MODULE.equals(attributeName)) {
                    table.setModule(attributeValue);
                } else if (ConfigXmlProperties.PREFIX.equals(attributeName)) {
                    table.setPrefix(attributeValue);
                } else if (ConfigXmlProperties.REQUEST_BASE_PATH.equals(attributeName)) {
                    if (StringUtils.isBlank(attributeValue)) {
                        log.error(ConfigXmlProperties.REQUEST_BASE_PATH + " is blank");
                        System.exit(0);
                    }
                    table.setRequestBasePath(attributeValue);
                } else if (ConfigXmlProperties.METHODS.equals(attributeName)) {
                    if (StringUtils.isNotEmpty(attributeValue)) {
                        table.setMethods(Splitter.on(",").splitToList(attributeValue));
                    } else {
                        log.error(ConfigXmlProperties.METHODS + " is blank");
                        System.exit(0);
                    }
                }
            }
            tables.add(table);
        }
    }

    /**
     * 模板初始化
     *
     * @param element
     */
    private static void templatesInit(Element element) {
        if (!ConfigXmlProperties.TEMPLATES.equals(element.getName())) {
            return;
        }
        templates = new Templates();
        List<Attribute> list = element.attributes();
        for (Attribute attribute : list) {
            String attributeName = attribute.getName();
            String attributeValue = attribute.getValue();
            if (ConfigXmlProperties.PATH.equals(attributeName)) {
                templates.setPath(attributeValue);
            }
            if (ConfigXmlProperties.TARGET_GROUP_NAME.equals(attributeName)) {
                templates.setTargetGroupName(attributeValue);
            }
        }
        List<Group> groupList = getGroupList(element);
        templates.setGroupList(groupList);
    }

    private static List<Group> getGroupList(Element element) {
        List<Group> groupList = Lists.newArrayList();
        Iterator<Element> groupIterator = element.elementIterator();
        while (groupIterator.hasNext()) {
            Element groupElement = groupIterator.next();
            Group group = new Group();
            List<Attribute> attributes = groupElement.attributes();
            for (Attribute attribute : attributes) {
                String attributeName = attribute.getName();
                String attributeValue = attribute.getValue();
                if (ConfigXmlProperties.NAME.equals(attributeName)) {
                    group.setName(attributeValue);
                }
                if (ConfigXmlProperties.CAN_BE_DELETED.equals(attributeName)) {
                    if ("true".equals(attributeValue)) {
                        group.setTemplatesCanBeDeleted(true);
                    } else {
                        group.setTemplatesCanBeDeleted(false);
                    }
                }
                List<GeneratorTemplate> templateList = getGeneratorTemplates(groupElement);
                group.setTemplates(templateList);
            }
            groupList.add(group);
        }
        return groupList;
    }

    private static List<GeneratorTemplate> getGeneratorTemplates(Element groupElement) {
        List<Attribute> list;
        Iterator<Element> templeteIterator = groupElement.elementIterator();
        List<GeneratorTemplate> templateList = Lists.newArrayList();
        while (templeteIterator.hasNext()) {
            Element e = templeteIterator.next();
            GeneratorTemplate template = new GeneratorTemplate();
            list = e.attributes();
            String path = "";
            for (Attribute templateAttribute : list) {
                String templateAttributeName = templateAttribute.getName();
                String templateAttributeValue = templateAttribute.getValue();
                if (ConfigXmlProperties.ROOT_PATH.equals(templateAttributeName)) {
                    template.setRootPath(templateAttributeValue);
                    path += templateAttributeValue;
                } else if (ConfigXmlProperties.PATH.equals(templateAttributeName)) {
                    template.setPath(templateAttributeValue);
                    path = path + "." + templateAttributeValue;
                } else if (ConfigXmlProperties.NAME.equals(templateAttributeName)) {
                    template.setName(templateAttributeValue);

                } else if (ConfigXmlProperties.OVERWRITE.equals(templateAttributeName)) {
                    if ("true".equals(templateAttributeValue)) {
                        template.setOverwrite(true);
                    }
                } else if (ConfigXmlProperties.OUTPUT_MODE.equals(templateAttributeName)) {
                    template.setOutputMode(templateAttributeValue);
                } else if (ConfigXmlProperties.ABSOLUTE_PATH.equals(templateAttributeName)) {
                    template.setAbsolutePath(templateAttributeValue);
                }

            }
            template.setPath(path);
            templateList.add(template);
        }
        return templateList;
    }

    /**
     * xml类型关系初始化
     *
     * @param element
     */
    private static void xmlTypesInit(Element element) {
        Iterator<Element> iterator = element.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            //获取当前节点的所有属性节点
            List<Attribute> list = e.attributes();
            //遍历属性节点
            String name = null;
            String value = null;
            for (Attribute attribute : list) {
                if (ConfigXmlProperties.JDBC_TYPE.equals(attribute.getName())) {
                    name = attribute.getValue();
                } else if (ConfigXmlProperties.XML_TYPE.equals(attribute.getName())) {
                    value = attribute.getValue();
                }
            }
            xmlTypes.put(name, value);
        }
    }

    /**
     * 类型关系初始化
     *
     * @param element
     */
    private static void typesInit(Element element) {
        Iterator<Element> iterator = element.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            //获取当前节点的所有属性节点
            List<Attribute> list = e.attributes();
            //遍历属性节点
            String name = null;
            String value = null;
            for (Attribute attribute : list) {
                if (ConfigXmlProperties.JDBC_TYPE.equals(attribute.getName())) {
                    name = attribute.getValue();
                } else if (ConfigXmlProperties.JAVA_TYPE.equals(attribute.getName())) {
                    value = attribute.getValue();
                }
            }
            types.put(name, value);
        }
    }

    /**
     * 数据库信息初始化
     *
     * @param element
     */
    private static void dataSourceInit(Element element) {
        //获取当前节点的所有属性节点
        List<Attribute> list = element.attributes();
        //遍历属性节点
        for (Attribute attribute : list) {
            String attributeName = attribute.getName();
            String attributeValue = attribute.getValue();
            if (ConfigXmlProperties.URL.equals(attributeName)) {
                dataSourceUrl = attributeValue;
            } else if (ConfigXmlProperties.USER.equals(attributeName)) {
                userName = attributeValue;
            } else if (ConfigXmlProperties.PASSWORD.equals(attributeName)) {
                password = attributeValue;
            } else if (ConfigXmlProperties.DRIVER_CLASS_NAME.equals(attributeName)) {
                driverClassName = attributeValue;
            } else if (ConfigXmlProperties.DB.equals(attributeName)) {
                dbName = attributeValue;
            }
        }
    }

    /**
     * 数据库信息初始化
     *
     * @param element
     */
    private static void compareDataSourceInit(Element element) {
        //获取当前节点的所有属性节点
        List<Attribute> list = element.attributes();
        //遍历属性节点
        for (Attribute attribute : list) {
            String attributeName = attribute.getName();
            String attributeValue = attribute.getValue();
            if (ConfigXmlProperties.URL.equals(attributeName)) {
                compareDataSourceUrl = attributeValue;
            } else if (ConfigXmlProperties.USER.equals(attributeName)) {
                compareUserName = attributeValue;
            } else if (ConfigXmlProperties.PASSWORD.equals(attributeName)) {
                comparePassword = attributeValue;
            } else if (ConfigXmlProperties.DRIVER_CLASS_NAME.equals(attributeName)) {
                compareDriverClassName = attributeValue;
            } else if (ConfigXmlProperties.DB.equals(attributeName)) {
                compareDbName = attributeValue;
            }
        }
    }

    /**
     * info信息初始化
     *
     * @param element
     */
    private static void infoInit(Element element) {
        //获取当前节点的所有属性节点
        List<Attribute> list = element.attributes();
        //遍历属性节点
        for (Attribute attribute : list) {
            String attributeName = attribute.getName();
            String attributeValue = attribute.getValue();
            if (ConfigXmlProperties.AUTHOR.equals(attributeName)) {
                author = attributeValue;
            } else if (ConfigXmlProperties.REMARK.equals(attributeName)) {
                remark = attributeValue;
            }
        }
    }

}
