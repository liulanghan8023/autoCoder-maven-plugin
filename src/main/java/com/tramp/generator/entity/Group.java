package com.tramp.generator.entity;

import java.util.List;

/**
 * @author chenjm1
 * @since 2019/2/26
 */
public class Group {

    private String name;
    private Boolean templatesCanBeDeleted = false;
    private List<GeneratorTemplate> templates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getTemplatesCanBeDeleted() {
        return templatesCanBeDeleted;
    }

    public void setTemplatesCanBeDeleted(Boolean templatesCanBeDeleted) {
        this.templatesCanBeDeleted = templatesCanBeDeleted;
    }

    public List<GeneratorTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<GeneratorTemplate> templates) {
        this.templates = templates;
    }
}
