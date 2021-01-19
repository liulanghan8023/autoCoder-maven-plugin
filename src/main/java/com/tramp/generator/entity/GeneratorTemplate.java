package com.tramp.generator.entity;

/**
 * 模板
 *
 * @author chenjm1
 * @since 2017/10/25
 */
public class GeneratorTemplate {

    private String name;
    private String path;
    private String rootPath;
    private Boolean overwrite = false;
    private String outputMode;
    private String absolutePath;

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }

    public String getOutputMode() {
        return outputMode;
    }

    public void setOutputMode(String outputMode) {
        this.outputMode = outputMode;
    }
}
