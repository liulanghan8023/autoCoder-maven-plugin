package com.tramp.generator.mojo;

import com.tramp.generator.common.CodeCreator;
import com.tramp.generator.common.MySqlContext;
import com.tramp.generator.config.GeneratorXmlConfig;
import com.tramp.generator.entity.DBTable;
import com.tramp.generator.entity.GeneratorTemplate;
import com.tramp.generator.entity.Group;
import com.tramp.generator.utils.FreeMarkerUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * @author chenjm1
 * @since 2018/11/21
 */
@Mojo(name = "2-remove", defaultPhase = LifecyclePhase.PACKAGE)
public class RemoveMojo extends AbstractMojo {

    private static Logger log = LoggerFactory.getLogger(RemoveMojo.class);

    @Parameter(required = true)
    private String configPath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (StringUtils.isBlank(configPath)) {
            log.error("configPath is null");
            System.exit(0);
        }
        MySqlContext mySqlContext = new MySqlContext(configPath);
        //获取要执行的group
        Group targetGroup = GeneratorXmlConfig.templates.getTargetGroup();
        if (targetGroup == null) {
            log.error("targetGroup不存在,请核对targetGroupName是否存在");
            System.exit(0);
        }
        if (!targetGroup.getTemplatesCanBeDeleted()) {
            log.error("!!!!!!!!!!!!!请先配置模板可被移除再操作! position: generatorConfiguration>template>group's canBeDeleted");
            System.exit(0);
        }

        List<GeneratorTemplate> templates = targetGroup.getTemplates();
        List<DBTable> tables = GeneratorXmlConfig.tables;
        int total = 0;
        for (DBTable table : tables) {
            log.info("=====>开始移除表:" + table.getName());
            for (GeneratorTemplate template : templates) {
                if ("console".equals(template.getOutputMode())) {
                    continue;
                }
                String suffix = template.getName().replace(".ftl", "").replace(CodeCreator.PLH, "");
                String storePath;
                String basePath = System.getProperty("user.dir") + "\\src\\main\\";
                String path = template.getPath().replace(".", "\\").replace("${module}", table.getModule());
                String finalPath = basePath + path;
                if (StringUtils.isBlank(table.getPrefix())) {
                    storePath = finalPath + "/" + FreeMarkerUtil.upperName(table.getName()) + suffix;
                } else {
                    storePath = finalPath + "/" + FreeMarkerUtil.upperName(table.getName().replace(table.getPrefix(), "")) + suffix;
                }
                File file = new File(storePath);
                if (!file.exists()) {
                    continue;
                }
                total = total + 1;
                log.info("---已移除:" + storePath);
                file.delete();
            }
            log.info("=====>结束移除表:" + table.getName());
        }
        log.info("共移除文件数:" + total + "个");
    }
}
