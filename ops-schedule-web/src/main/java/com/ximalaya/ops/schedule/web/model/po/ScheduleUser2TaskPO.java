package com.ximalaya.ops.schedule.web.model.po;

import java.util.Date;

/**
 * Created by nihao on 17/8/17.
 */
public class ScheduleUser2TaskPO {
    private Integer id;
    private Integer userId;
    private Integer taskId;
    private Date createAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
