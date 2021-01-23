package com.tramp.basic.entity;

import com.tramp.frame.server.base.BaseEntity;

import java.util.Date;


/**
* app
* @author autoCoder
* @since 2021-01-23 10:53:03
*/
public class App implements  BaseEntity{

    private String id; //
    private String name; //名称
    private String status; //状态,[EFFECTIVE:有效,INVALID:失效]
    private String remark; //备注
    private Date createTime; //

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
