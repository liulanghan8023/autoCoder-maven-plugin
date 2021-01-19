package com.tramp.generator.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tramp.generator.config.GeneratorXmlConfig;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenjm1
 * @since 2017/10/24
 */
public class FreeMarkerUtil {
    private static Logger log = LoggerFactory.getLogger(FreeMarkerUtil.class);

    private FreeMarkerUtil() {
    }

    private static final Configuration CONFIGURATION = new Configuration();

    static {
        try {
            CONFIGURATION.setTemplateLoader(new FileTemplateLoader(new File(GeneratorXmlConfig.sysBasePath + GeneratorXmlConfig.templates.getPath())));
        } catch (IOException e) {
            log.error("templates loader error,please check config!", e);
            System.exit(0);

        }
        CONFIGURATION.setDefaultEncoding("UTF-8");
        CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public static Template getTemplate(String templateName) throws IOException {
        try {
            return CONFIGURATION.getTemplate(templateName);
        } catch (IOException e) {
            throw e;
        }
    }

    public static void clearCache() {
        CONFIGURATION.clearTemplateCache();
    }

    public static String upperName(String tableName) {
        String[] strs = tableName.split("_");
        String str = "";
        if (strs.length >= 0) {
            for (String string : strs) {
                str += initialStrToUpper(string);
            }
        }

        return str;
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String initialStrToUpper(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    public static String initialStrToLow(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 转为缩写
     *
     * @param str
     * @return
     */
    public static String initialStrToAbbreviation(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        List<String> stringList = Splitter.on("_").splitToList(str);
        String result = "";
        for (String s : stringList) {
            if (StringUtils.isNotBlank(s)) {
                result = result + s.substring(0, 1);
            }
        }
        return result;
    }

    /**
     * 使用正则表达式提取中括号中的内容
     *
     * @param msg
     * @return
     */
    public static List<String> extractMessageByRegular(String msg) {
        List<String> list = Lists.newArrayList();
        Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
        Matcher m = p.matcher(msg);
        while (m.find()) {
            list.add(m.group().substring(1, m.group().length() - 1));
        }
        return list;
    }
}
