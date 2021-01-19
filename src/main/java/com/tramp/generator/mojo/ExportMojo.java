package com.tramp.generator.mojo;

import com.tramp.generator.common.CodeCreator;
import com.tramp.generator.common.FreeMarkerContext;
import com.tramp.generator.common.MySqlContext;
import com.tramp.generator.config.GeneratorXmlConfig;
import com.tramp.generator.entity.DBTable;
import com.tramp.generator.entity.GeneratorTemplate;
import com.tramp.generator.entity.Group;
import com.tramp.generator.utils.FreeMarkerUtil;
import com.tramp.generator.utils.MysqlUtils;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * @author chenjm1
 * @since 2019/2/25
 */
@Mojo(name = "4-export", defaultPhase = LifecyclePhase.PACKAGE)
public class ExportMojo extends AbstractMojo {
    private static Logger log = LoggerFactory.getLogger(ExportMojo.class);
    @Parameter(required = true)
    private String configPath;

    @Override
    public void execute() {
        try {
            exportToPath();
        } catch (Exception e) {
            log.error("导出错误", e);
        }
    }

    /**
     * 导出到excel
     */
    private void exportToPath() throws Exception {
        log.info("=====>开始导出表结构");
        long startTime = System.currentTimeMillis();

        //获取全部表
        MySqlContext mySqlContext = new MySqlContext(configPath);
        List<DBTable> allTables = MysqlUtils.getAllTables();
        log.info("数据表共:" + allTables.size() + "个");
        Group targetGroup = GeneratorXmlConfig.templates.getTargetGroup();
        if (targetGroup == null) {
            log.error("targetGroup不存在,请核对targetGroupName是否存在");
            System.exit(0);
        }
        List<GeneratorTemplate> templates = targetGroup.getTemplates();
        for (GeneratorTemplate generatorTemplate : templates) {
            if (StringUtils.isBlank(generatorTemplate.getAbsolutePath())) {
                log.error("template的absolutePath不能为空");
                System.exit(0);
            }
            Template template = FreeMarkerUtil.getTemplate(generatorTemplate.getName());
            FreeMarkerContext context = new FreeMarkerContext(allTables);
            //生成文件
            String name = template.getName().replace(".ftl", "").replace(CodeCreator.PLH, "");

            File dir = new File(generatorTemplate.getAbsolutePath());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(generatorTemplate.getAbsolutePath() + "\\" + name);
            FileWriter out = new FileWriter(file);
            template.process(context, out);
        }
        log.info("=====>导出表结构结束,共耗时:" + (System.currentTimeMillis() - startTime) + "s");

    }

}
