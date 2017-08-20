package com.ximalaya.ops.schedule.web.model.po;

import java.util.Date;

/**
 * Created by nihao on 17/8/17.
 */
public class ScheduleTaskPO {
    private Integer id;
    private String group;
    private String key;
    private String description;
    private Integer type;
    private Long period;
    private String time;
    private Integer createUid;
    private Date createAt;
    private Integer updateUid;
    private Date updateAt;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Integer createUid) {
        this.createUid = createUid;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Integer getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(Integer updateUid) {
        this.updateUid = updateUid;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }


    @Override
    public int hashCode() {
        if(id == null){
            return super.hashCode();
        }
        else{
            return id.hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ScheduleTaskPO){
            ScheduleTaskPO oo = (ScheduleTaskPO) obj;
            if(oo.getId() != null && oo.getId().equals(this.id)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
}
