package com.ximalaya.ops.schedule.service.model;

import java.util.Date;

/**
 * Created by nihao on 17/8/14.
 */
public class SchedulePO {
    private Integer id;
    private String group;
    private String name;
    private Integer concurrentNum;
    private String  cron;
    private Date createAt;
    private Date updateAt;
    private Long createUid;
    private Long updateUid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getConcurrentNum() {
        return concurrentNum;
    }

    public void setConcurrentNum(Integer concurrentNum) {
        this.concurrentNum = concurrentNum;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Long createUid) {
        this.createUid = createUid;
    }

    public Long getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(Long updateUid) {
        this.updateUid = updateUid;
    }
}
