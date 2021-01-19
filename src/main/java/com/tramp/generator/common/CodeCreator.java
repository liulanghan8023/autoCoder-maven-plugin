package com.tramp.generator.common;

import com.tramp.generator.config.GeneratorXmlConfig;
import com.tramp.generator.entity.DBColumnEnum;
import com.tramp.generator.entity.DBTable;
import com.tramp.generator.entity.GeneratorTemplate;
import com.tramp.generator.entity.Group;
import com.tramp.generator.utils.FreeMarkerUtil;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * @author chenjm1
 * @since 2017/10/24
 */
public class CodeCreator {
    private static Logger log = LoggerFactory.getLogger(CodeCreator.class);
    public static final String PLH = "#";//占位符
    //public static final String PLH="plh";//占位符

    /**
     * 生成代码
     *
     * @param dbTable
     */
    public void generator(DBTable dbTable) {
        Template template;
        Group targetGroup = GeneratorXmlConfig.templates.getTargetGroup();
        if (targetGroup == null) {
            log.error("targetGroup不存在,请核对targetGroupName是否存在");
            System.exit(0);
        }
        List<GeneratorTemplate> templates = targetGroup.getTemplates();
        for (GeneratorTemplate generatorTemplate : templates) {
           /* if (StringUtils.isBlank(generatorTemplate.getPath()) || StringUtils.isBlank(generatorTemplate.getRootPath())) {
                log.error("template标签的rootPath和path不能为空");
                System.exit(0);
            }*/
            String templateName = generatorTemplate.getName();
            if (templateName.contains("Enum.java")) {
                generatorEnum(dbTable, generatorTemplate);
                continue;
            }
            String path = generatorTemplate.getPath().replace(".", "\\").replace("${module}", dbTable.getModule());
            String suffix = templateName.replace(".ftl", "").replace(PLH, "");
            try {
               // String basePath = System.getProperty("user.dir") + "\\src\\main\\";
                String basePath = System.getProperty("user.dir")+"\\";
                String storePath;
                String finalPath = basePath + path;
                if (StringUtils.isBlank(dbTable.getPrefix())) {
                    storePath = finalPath + "/" + FreeMarkerUtil.upperName(dbTable.getName()) + suffix;
                } else {
                    storePath = finalPath + "/" + FreeMarkerUtil.upperName(dbTable.getName().replace(dbTable.getPrefix(), "")) + suffix;
                }
                File file = new File(storePath);
                if (file.exists() && !dbTable.getOverwrite()) {
                    continue;
                }
                if (file.exists() && !generatorTemplate.getOverwrite()) {
                    continue;
                }
                File dir = new File(finalPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                template = FreeMarkerUtil.getTemplate(templateName);
                FreeMarkerContext context = new FreeMarkerContext(dbTable);
                if ("console".equals(generatorTemplate.getOutputMode())) {
                    StringWriter out = new StringWriter();
                    template.process(context, out);
                    log.info("\n*****表名:" + dbTable.getName() + ",模板:" + templateName + "*******************************************\n" + out.toString()
                            + "\n***********************************************************************************************");
                } else {
                    FileWriter out = new FileWriter(file);
                    template.process(context, out);

                }

            } catch (Exception e) {
                log.error("generator error", e);
                System.exit(0);
            }
        }
    }

    private void generatorEnum(DBTable dbTable, GeneratorTemplate generatorTemplate) {
        List<DBColumnEnum> columnEnumList = dbTable.getColumnEnumList();
        if (columnEnumList == null || columnEnumList.size() < 1) {
            return;
        }
        Template template;
        for (DBColumnEnum dbColumnEnum : columnEnumList) {
            String templateName = generatorTemplate.getName();
            String path = generatorTemplate.getPath().replace(".", "\\").replace("${module}", dbTable.getModule());
            String suffix = templateName.replace(".ftl", "");
            String tableEnumName = FreeMarkerUtil.upperName(dbTable.getName().replace(dbTable.getPrefix(), "")) + FreeMarkerUtil.initialStrToUpper(FreeMarkerUtil.upperName(dbColumnEnum.getColumnName().toLowerCase()));
            try {
                //String basePath = System.getProperty("user.dir") + "\\src\\main\\";
                String basePath = System.getProperty("user.dir")+"\\";
                String storePath;
                String finalPath = basePath + path;
                storePath = finalPath + "/" + tableEnumName + suffix;

                File file = new File(storePath);
                if (file.exists() && !dbTable.getOverwrite()) {
                    continue;
                }
                if (file.exists() && !generatorTemplate.getOverwrite()) {
                    continue;
                }
                File dir = new File(finalPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                template = FreeMarkerUtil.getTemplate(templateName);
                try (FileWriter out = new FileWriter(file)) {
                    FreeMarkerContext context = new FreeMarkerContext(dbTable);
                    context.setDbColumnEnum(dbColumnEnum);
                    context.setTableEnumName(tableEnumName);
                    template.process(context, out);
                }
            } catch (Exception e) {
                log.error("generatorEnum error", e);
                System.exit(0);
            }

        }
    }
}
