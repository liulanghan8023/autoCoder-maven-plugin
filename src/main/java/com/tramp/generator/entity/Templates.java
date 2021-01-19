package com.tramp.generator.entity;

import java.util.List;

/**
 * @author chenjm1
 * @since 2019/2/26
 */
public class Templates {
    private String path;
    private String targetGroupName;
    private List<Group> groupList;


    public Group getTargetGroup() {
        if (groupList == null || groupList.size() <= 0) {
            return null;
        }
        for (Group group : this.getGroupList()) {
            if (group.getName().equals(targetGroupName)) {
                return group;
            }
        }
        return null;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTargetGroupName() {
        return targetGroupName;
    }

    public void setTargetGroupName(String targetGroupName) {
        this.targetGroupName = targetGroupName;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
}
